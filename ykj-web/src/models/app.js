
import request from '../utils/request';
import codewords from '../codewords.json';

export default {
  namespace: 'app',

  state: {
    menu_current: undefined,
    menu_open_keys: [],
    group: {
      id: '0',
      name: '绿城集团'
    },
    codewords,
  },

  subscriptions: {
    setup({ dispatch, history }) {
      history.listen(location => {
        let menuOpenKeys = [];
        const paths = location.pathname.split('/');
        if (paths.length > 1) {
          menuOpenKeys = paths.slice(1, paths.length - 1).map( item => `/${item}` );
        }

        dispatch({
          type: 'changeMenu',
          payload: {
            menu_current: location.pathname,
            menu_open_keys: menuOpenKeys,
          }
        });
      });
    },
  },

  reducers: {
    changeMenu(state, { payload }) {
      return { ...state, ...payload }
    },
    toggleMenu(state, { payload }) {
      return { ...state, ...payload }
    }
  }
}
