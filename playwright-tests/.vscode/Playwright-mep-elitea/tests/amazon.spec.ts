import { test, expect, Page, Locator } from '@playwright/test';

const BASE_URL = 'https://www.amazon.in/';
const BESTSELLERS_URL = /amazon\.in\/gp\/bestsellers/;
const DESKTOP_VIEWPORT = { width: 1440, height: 900 };
const MOBILE_VIEWPORT = { width: 390, height: 844 };

const DEPARTMENTS = ['Books', 'Beauty', 'Garden & Outdoors', 'Electronics'] as const;

type Department = (typeof DEPARTMENTS)[number];

class AmazonBestSellersPage {
  constructor(private readonly page: Page) {}

  async gotoHome() {
    await this.page.goto(BASE_URL, { waitUntil: 'domcontentloaded' });
  }

  async openBestSellers() {
    const bestsellersLink = this.page.getByRole('link', { name: /best sellers|bestsellers/i }).first();
    await expect(bestsellersLink).toBeVisible({ timeout: 15000 });
    await bestsellersLink.click();
    await expect(this.page).toHaveURL(BESTSELLERS_URL, { timeout: 20000 });
    await expect(this.page.locator('body')).toContainText(/Bestsellers/i, { timeout: 20000 });
  }

  departmentButton(): Locator {
    return this.page.getByRole('button', { name: /any department|department/i }).first();
  }

  categoryLink(category: Department): Locator {
    return this.page
      .getByRole('link', { name: new RegExp(`^${escapeRegExp(category)}$`, 'i') })
      .first();
  }

  heading(category: Department): Locator {
    return this.page.getByRole('heading', { name: new RegExp(`Bestsellers in ${escapeRegExp(category)}`, 'i') }).first();
  }

  firstProductLink(): Locator {
    return this.page.locator('a[href*="/dp/"]').first();
  }

  productCards(): Locator {
    return this.page.locator('a[href*="/dp/"]');
  }

  nextPageLink(): Locator {
    return this.page.getByRole('link', { name: /next/i }).first();
  }
}

function escapeRegExp(text: string): string {
  return text.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

async function acceptPopupsIfPresent(page: Page) {
  const candidates = [
    page.getByRole('button', { name: /accept/i }).first(),
    page.getByRole('button', { name: /got it/i }).first(),
    page.getByRole('button', { name: /continue/i }).first(),
  ];

  for (const button of candidates) {
    try {
      if (await button.isVisible({ timeout: 2500 })) {
        await button.click();
        return;
      }
    } catch {
      // ignore
    }
  }
}

async function openDepartment(page: Page, category: Department) {
  const amazon = new AmazonBestSellersPage(page);
  const button = amazon.departmentButton();
  await expect(button).toBeVisible({ timeout: 15000 });
  await button.click();

  const link = amazon.categoryLink(category);
  await expect(link).toBeVisible({ timeout: 15000 });
  await link.click();
  await expect(amazon.heading(category)).toBeVisible({ timeout: 15000 });
}

async function extractVisibleRanks(page: Page): Promise<number[]> {
  const anchors = page.locator('a[href*="/dp/"]');
  const count = Math.min(await anchors.count(), 10);
  const ranks: number[] = [];

  for (let i = 0; i < count; i++) {
    const container = anchors.nth(i).locator('xpath=ancestor::*[self::div or self::li][1]');
    const text = (await container.textContent()) ?? '';
    const match = text.match(/#\s*(\d+)/);
    if (match) ranks.push(Number(match[1]));
  }

  return ranks;
}

test.describe('Amazon Bestsellers page navigation', () => {
  test.beforeEach(async ({ page }) => {
    await page.setViewportSize(DESKTOP_VIEWPORT);
  });

  test('navigates to Bestsellers and validates key department headings', async ({ page }) => {
    const amazon = new AmazonBestSellersPage(page);
    await amazon.gotoHome();
    await acceptPopupsIfPresent(page);
    await amazon.openBestSellers();

    await expect(page.getByText(/Bestsellers in Beauty/i)).toBeVisible({ timeout: 15000 });
    await expect(page.getByText(/Bestsellers in Garden & Outdoors/i)).toBeVisible({ timeout: 15000 });
  });

  test('filters bestsellers by category and verifies page heading', async ({ page }) => {
    const amazon = new AmazonBestSellersPage(page);
    await amazon.gotoHome();
    await acceptPopupsIfPresent(page);
    await amazon.openBestSellers();

    for (const category of DEPARTMENTS) {
      await openDepartment(page, category);
      await expect(amazon.heading(category)).toBeVisible({ timeout: 15000 });
    }
  });

  test('validates product sorting by confirming visible bestseller ranks are ascending', async ({ page }) => {
    const amazon = new AmazonBestSellersPage(page);
    await amazon.gotoHome();
    await acceptPopupsIfPresent(page);
    await amazon.openBestSellers();
    await openDepartment(page, 'Books');

    const ranks = await extractVisibleRanks(page);
    expect(ranks.length).toBeGreaterThan(0);

    for (let i = 1; i < ranks.length; i++) {
      expect(ranks[i]).toBeGreaterThanOrEqual(ranks[i - 1]);
    }
  });

  test('supports pagination through bestseller listings', async ({ page }) => {
    const amazon = new AmazonBestSellersPage(page);
    await amazon.gotoHome();
    await acceptPopupsIfPresent(page);
    await amazon.openBestSellers();
    await openDepartment(page, 'Electronics');

    const next = amazon.nextPageLink();
    await expect(next).toBeVisible({ timeout: 15000 });
    await next.click();

    await expect(page).toHaveURL(/page=2|pg=2|2/, { timeout: 15000 });
    await expect(page.locator('body')).toContainText(/Bestsellers/i, { timeout: 15000 });
  });

  test('opens a product detail page and validates product information', async ({ page }) => {
    const amazon = new AmazonBestSellersPage(page);
    await amazon.gotoHome();
    await acceptPopupsIfPresent(page);
    await amazon.openBestSellers();
    await openDepartment(page, 'Electronics');

    const firstProduct = amazon.firstProductLink();
    await expect(firstProduct).toBeVisible({ timeout: 15000 });
    const href = await firstProduct.getAttribute('href');
    expect(href).toContain('/dp/');

    await firstProduct.click();
    await page.waitForLoadState('domcontentloaded');

    await expect(page).toHaveURL(/\/dp\//, { timeout: 15000 });
    await expect(page.locator('h1')).toBeVisible({ timeout: 15000 });
    await expect(page.locator('body')).toContainText(/Add to Cart|Buy Now|product details/i, { timeout: 15000 });
  });

  test('verifies responsive behavior on mobile viewport', async ({ page }) => {
    await page.setViewportSize(MOBILE_VIEWPORT);

    const amazon = new AmazonBestSellersPage(page);
    await amazon.gotoHome();
    await acceptPopupsIfPresent(page);
    await amazon.openBestSellers();

    await expect(page.locator('body')).toBeVisible();
    await expect(page.getByRole('link', { name: /bestsellers/i }).first()).toBeVisible({ timeout: 15000 });
  });

  test('validates page performance and load timing', async ({ page }) => {
    const amazon = new AmazonBestSellersPage(page);
    const start = Date.now();

    await amazon.gotoHome();
    await acceptPopupsIfPresent(page);
    await amazon.openBestSellers();

    const totalMs = Date.now() - start;
    const navigationTiming = await page.evaluate(() => {
      const nav = performance.getEntriesByType('navigation')[0] as PerformanceNavigationTiming | undefined;
      return nav ? Math.round(nav.duration) : 0;
    });

    expect(totalMs).toBeLessThan(20000);
    expect(navigationTiming).toBeGreaterThan(0);

    const resourceCount = await page.evaluate(() => performance.getEntriesByType('resource').length);
    expect(resourceCount).toBeGreaterThan(0);
  });
});
