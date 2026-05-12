import { Page, Locator } from '@playwright/test';
import BasePage from '../utils/basePage';

export default class GoogleHome extends BasePage {
  readonly searchBox: Locator;
  readonly results: Locator;

  constructor(page: Page) {
    super(page);
    this.searchBox = page.locator('input[name="q"]');
    this.results = page.locator('//div[@id="search"]//a');
  }

  async search(query: string) {
    console.log(`Searching for: ${query}`);
    await this.searchBox.fill(query);
    await this.searchBox.press('Enter');
    await this.results.first().waitFor({ state: 'visible' });
  }

  async clickResultWithText(text: string) {
    const target = this.page.locator(`//div[@id="search"]//a[contains(., "${text}")]`);
    await target.first().click();
  }
}
