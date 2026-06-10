import { test, expect } from '@playwright/test';

test.describe('Amazon Bestsellers navigation', () => {
  test('should navigate from Bestsellers to Books and Electronics sections', async ({ page }) => {
    await page.goto('https://www.amazon.in/');
    await page.waitForLoadState('domcontentloaded');

    // Open Bestsellers from the header menu
    await page.getByRole('link', { name: 'Bestsellers', exact: true }).click();
    await page.waitForLoadState('domcontentloaded');

    // Click Books under Any Department
    await page.locator('a[href="/gp/bestsellers/books/ref=zg_bs_nav_books_0"]').click();
    await page.waitForLoadState('domcontentloaded');

    await expect(page.getByText('Bestsellers in Beauty', { exact: true })).toBeVisible();
    await expect(page.getByText('Bestsellers in Garden & Outdoors', { exact: true })).toBeVisible();

    // Click Electronics under Any Department
    await page.locator('a[href="/gp/bestsellers/electronics/ref=zg_bs_nav_electronics_0"]').click();
    await page.waitForLoadState('domcontentloaded');

    await expect(page.getByText('Bestsellers in Electronics', { exact: true })).toBeVisible();
  });
});
