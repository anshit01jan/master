import { Page, Locator, expect } from '@playwright/test';

export default class BasePage {
  readonly page: Page;

  constructor(page: Page) {
    this.page = page;
  }

  async navigate(url: string) {
    console.log(`Navigating to: ${url}`);
    await this.page.goto(url, { waitUntil: 'domcontentloaded' });
  }

  async click(locator: Locator) {
    await locator.waitFor({ state: 'visible', timeout: 10000 });
    await locator.click();
  }

  async type(locator: Locator, text: string) {
    await locator.fill(text);
  }

  async expectVisible(locator: Locator, message?: string) {
    await expect(locator).toBeVisible({ timeout: 10000 });
    if (message) console.log(message);
  }
}
