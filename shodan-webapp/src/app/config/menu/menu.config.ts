export class MenuConfig {
  public defaults: any = {
    header: {
      self: {},
      items: [
        {
          title: 'Home',
          root: true,
          alignment: 'left',
          page: '/dashboard',
          translate: 'MENU.DASHBOARD'
        },
      ]
    }
  };

  public get configs(): any {
    return this.defaults;
  }
}

