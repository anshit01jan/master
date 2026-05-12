// PR header: include iplSection.page.ts in PR diff
import { Page, Locator } from '@playwright/test';
import BasePage from '../utils/basePage';

export default class IPLSection extends BasePage {
  readonly tableTab: Locator;

  constructor(page: Page) {
    super(page);
    this.tableTab = page.locator('//a[contains(., "Table") or contains(., "Standings")]');
  }

  async goToTable() {
    await this.tableTab.first().click();
    await this.page.waitForLoadState('domcontentloaded');
  }
}
