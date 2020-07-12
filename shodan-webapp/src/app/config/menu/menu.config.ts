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
        // }, {
        //   title: 'Entrada de Eventos',
        //   root: true,
        //   alignment: 'left',
        //   page: '/events',
        //   translate: 'MENU.EVENTS'
        },
      ]
    }
  };

  public get configs(): any {
    return this.defaults;
  }
}

