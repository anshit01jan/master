import { test, expect } from '@playwright/test';

test.describe('Amazon India Bestsellers navigation', () => {
  test('should verify bestsellers sections for Books and Electronics', async ({ page }) => {
    await page.goto('https://www.amazon.in/', { waitUntil: 'domcontentloaded' });

    // Select Bestsellers from the header menu.
    const bestsellersLink = page.getByRole('link', { name: /Bestsellers/i }).first();
    await expect(bestsellersLink).toBeVisible({ timeout: 15000 });
    await bestsellersLink.click();
    await expect(page).toHaveURL(/bestsellers/i);

    // Verify requested sections are visible on the Bestsellers page.
    await expect(page.getByText('Bestsellers in Beauty', { exact: false })).toBeVisible({ timeout: 15000 });
    await expect(page.getByText('Bestsellers in Garden & Outdoors', { exact: false })).toBeVisible({ timeout: 15000 });

    // Click Books under Any Department.
    const booksFilter = page.getByRole('link', { name: /Books/i }).first();
    await expect(booksFilter).toBeVisible({ timeout: 15000 });
    await booksFilter.click();
    await expect(page.getByText('Bestsellers in Beauty', { exact: false })).toBeVisible({ timeout: 15000 });
    await expect(page.getByText('Bestsellers in Garden & Outdoors', { exact: false })).toBeVisible({ timeout: 15000 });

    // Click Electronics under Any Department.
    const electronicsFilter = page.getByRole('link', { name: /Electronics/i }).first();
    await expect(electronicsFilter).toBeVisible({ timeout: 15000 });
    await electronicsFilter.click();
    await expect(page.getByText('Bestsellers in Electronics', { exact: false })).toBeVisible({ timeout: 15000 });
  });
});
