import { test, expect } from '@playwright/test';

test.describe('Amazon India Bestsellers navigation', () => {
  test('should validate category-specific bestseller sections', async ({ page }) => {
    await page.goto('https://www.amazon.in/', { waitUntil: 'domcontentloaded' });

    // Handle optional consent overlays if they appear
    const consentCandidates = [
      'input#sp-cc-accept',
      'input[name="accept"]',
      'button:has-text("Accept")',
      'button:has-text("I agree")'
    ];

    for (const selector of consentCandidates) {
      const button = page.locator(selector).first();
      if (await button.isVisible({ timeout: 2000 }).catch(() => false)) {
        await button.click().catch(() => {});
        break;
      }
    }

    const bestsellersLink = page.getByRole('link', { name: /Bestsellers/i });
    await expect(bestsellersLink).toBeVisible();
    await bestsellersLink.click();

    await expect(page).toHaveURL(/bestseller|bestsellers/i);

    const booksTab = page.getByRole('link', { name: /Books/i });
    await expect(booksTab).toBeVisible();
    await booksTab.click();

    await expect(page.getByText(/Bestsellers in Beauty/i)).toBeVisible();
    await expect(page.getByText(/Bestsellers in Garden & Outdoors/i)).toBeVisible();

    const electronicsTab = page.getByRole('link', { name: /Electronics/i });
    await expect(electronicsTab).toBeVisible();
    await electronicsTab.click();

    await expect(page.getByText(/Bestsellers in Electronics/i)).toBeVisible();
  });
});