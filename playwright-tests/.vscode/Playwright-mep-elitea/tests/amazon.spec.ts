import { test, expect } from '@playwright/test';

test.describe('Amazon Bestsellers flow', () => {
  test('opens Bestsellers and verifies key departments', async ({ page }: { page: any }) => {
    await page.goto('https://www.amazon.in/');

    await page.getByRole('link', { name: /best sellers/i }).click();

    await expect(page.getByText(/Bestsellers in Beauty/i)).toBeVisible();
    await expect(page.getByText(/Bestsellers in Garden & Outdoors/i)).toBeVisible();

    const departmentToggle = page.getByRole('button', { name: /any department/i }).first();
    await departmentToggle.click();
    await page.getByRole('menuitem', { name: /Books/i }).click();
    await expect(page.getByText(/Bestsellers in Books/i)).toBeVisible();

    await departmentToggle.click();
    await page.getByRole('menuitem', { name: /Electronics/i }).click();
    await expect(page.getByText(/Bestsellers in Electronics/i)).toBeVisible();
  });
});
