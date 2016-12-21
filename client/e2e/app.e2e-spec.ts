import { VitheakidsAdminPage } from './app.po';

describe('vitheakids-admin App', function() {
  let page: VitheakidsAdminPage;

  beforeEach(() => {
    page = new VitheakidsAdminPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
