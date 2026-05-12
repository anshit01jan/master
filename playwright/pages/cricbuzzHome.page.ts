// PR header: include cricbuzzHome.page.ts in PR diff
import { Page, Locator } from '@playwright/test';
import BasePage from '../utils/basePage';

export default class CricbuzzHome extends BasePage {
  readonly menuItems: Locator;

  constructor(page: Page) {
    super(page);
    this.menuItems = page.locator('nav[data-testid] a');
  }

  async goToIPL() {
    const iplLink = this.page.locator('//a[contains(., "IPL") or contains(., "Indian Premier League")]');
    await iplLink.first().click();
    await this.page.waitForLoadState('domcontentloaded');
  }
}
