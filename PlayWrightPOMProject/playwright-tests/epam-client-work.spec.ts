import { test, expect } from '@playwright/test';

test('navigate to EPAM client work page from Services menu', async ({ page }) => {
  await page.goto('https://www.epam.com/');

  await page.getByRole('link', { name: 'Services' }).first().click();
  await page.getByRole('link', { name: 'Explore Our Client Work' }).first().click();

  await expect(page.getByText('Client Work', { exact: true })).toBeVisible();
});
