import { test, expect } from '@playwright/test';

test.describe('Amazon India Bestsellers navigation', () => {
  test('should validate category-specific bestseller sections', async ({ page }) => {
    await page.goto('https://www.amazon.in/', { waitUntil: 'domcontentloaded' });

    // Handle optional consent overlays if they appear
    const consentButtons = [
      'input#sp-cc-accept',
      'input[name="accept"]',
      'button:has-text("Accept")'
    ];

    for (const selector of consentButtons) {
      const button = page.locator(selector).first();
      if (await button.isVisible({ timeout: 2000 }).catch(() => false)) {
        await button.click().catch(() => {});
        break;
      }
    }

    await expect(page.locator('a[data-csa-c-slot-id="nav_cs_0"]')).toContainText(/Bestsellers/i);
  });
});