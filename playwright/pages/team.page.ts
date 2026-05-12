import { Page, Locator } from '@playwright/test';
import BasePage from '../utils/basePage';

export default class TeamPage extends BasePage {
  readonly teamLink: (name: string) => Locator;
  readonly resultsTab: Locator;
  readonly resultsSection: Locator;

  constructor(page: Page) {
    super(page);
    this.teamLink = (name: string) => page.locator(`//a[contains(., "${name}") and contains(@href, "team")]`);
    this.resultsTab = page.locator('//a[contains(., "Results") or contains(., "Match Results")]');
    this.resultsSection = page.locator('//div[contains(@class, "results") or contains(@id, "results")]');
  }

  async openTeam(name: string) {
    const locator = this.teamLink(name);
    await locator.first().click();
    await this.page.waitForLoadState('domcontentloaded');
  }

  async openResults() {
    await this.resultsTab.first().click();
    await this.resultsSection.first().waitFor({ state: 'visible', timeout: 10000 });
  }
}
