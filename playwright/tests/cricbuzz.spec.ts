// PR header: include cricbuzz.spec.ts in PR diff
import { test, expect } from '@playwright/test';
import GoogleHome from '../pages/googleHome.page';
import CricbuzzHome from '../pages/cricbuzzHome.page';
import IPLSection from '../pages/iplSection.page';
import TeamPage from '../pages/team.page';

test.describe('Cricbuzz IPL Results navigation', () => {
  test('should navigate to RR results via Google search', async ({ page }) => {
    console.log('Test start: Launching browser and navigating to Google');
    const google = new GoogleHome(page);
    await google.navigate('https://www.google.com');
    await expect(page).toHaveURL(/google/);

    await google.search('cricbuzz.com');
    await expect(page.locator('#search')).toBeVisible();

    // Click Cricbuzz result
    await google.clickResultWithText('Cricbuzz');
    const cric = new CricbuzzHome(page);
    await expect(page).toHaveURL(/cricbuzz/);

    // Navigate to IPL
    await cric.goToIPL();
    const ipl = new IPLSection(page);
    await expect(page).toHaveURL(/ipl|IPL/);

    // Go to Table and click RR
    await ipl.goToTable();
    const team = new TeamPage(page);
    await team.openTeam('RR');
    await expect(page).toHaveURL(/team|RR/);

    // Open Results and validate
    await team.openResults();
    await team.expectVisible(team.resultsSection, 'Results section is visible');

    console.log('Test end: Closing browser');
  });
});
