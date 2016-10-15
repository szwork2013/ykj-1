
import { routerRedux } from 'dva/router';
import { message } from 'antd';
import request, { parseError } from '../../utils/request';
import { search, view, create, update } from '../../services/installGoods';
import pathToRegexp from 'path-to-regexp';
import querystring from 'querystring';

const mergeQuery = (oldQuery, newQuery) => {
  return {
    ...oldQuery,
    ...newQuery,
    page: (newQuery.page ? newQuery.page - 1 : 0),
  }
}

const initialState = {
  query: {},
  installGoods: [{id:1, name: '123', installNum: 123}],
  current: {},
  pagination: {
    current: 1,
  },
  loading: false,
  submiting: false,
}

export default {
  namespace: 'installGoods',

  state: initialState,

  subscriptions: {
    listSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (pathToRegexp('/order/orders/:id/installations/:id/installGoods').test(location.pathname)) {
          const match = pathToRegexp('/order/orders/:id/installations/:id/installGoods').exec(location.pathname);
          const id = match[1];
          console.log(match)
          dispatch({ type: 'clear' })
          dispatch({
            type: 'setQuery',
            payload: id,
          });
        }
      });
    },
    
    itemSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (pathToRegexp('/order/orders/:id/installations/:id/installGoods/:action+').test(location.pathname)) {
          dispatch({ type: 'clear' })
        }
      })
    }, 
    
    editSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (pathToRegexp('/order/orders/:id/installations/:id/installGoods/edit/:id').test(location.pathname)) {
          const match = pathToRegexp('/order/orders/:id/installations/:id/installGoods/:id').exec(location.pathname);
          const id = match[1];
          dispatch({
            type: 'view',
            payload: id,
          });
        }
      });
    }
  },

  effects: {
    setQuery: [function*({ payload: query }, { put, select }) {
      yield put({
        type: 'toggleLoadding',
        payload: true,
      });

      const { access_token, oldQuery } = yield select( state => {
        return {
          'access_token': state.oauth.access_token,
          'oldQuery': state.goods.query,
        }
      } );
      const { data, error } = yield search(access_token, query);

      if (!error) {
        yield put({
          type: 'setGoods',
          payload: {
            goods: data._embedded && data._embedded.goods || [],
            pagination: {
              current: data.page && data.page.number + 1,
              total: data.page && data.page.totalElements,
            },
          },
        });

        yield put({
          type: 'toggleLoadding',
          payload: false,
        });

        return true;
      }

      yield put({
        type: 'toggleLoadding',
        payload: false,
      });

      const err = yield parseError(error);
      yield message.error(`加载本次安装的商品列表失败:${err.status} ${err.message}`, 3);
      return false;
    }, { type: 'takeLatest' }],
    *view({ payload: id }, { put, select }) {
      yield put({
        type: 'toggleLoadding',
        payload: true,
      });

      const { access_token, goods } = yield select( state => {
        return {
          'access_token': state.oauth.access_token,
          'goods': state.goods.goods,
        }
      } );
      const { data, error } = yield view(access_token, id);

      if (!error) {
        yield put({
          type: 'setCurrent',
          payload: data,
        })
  
        yield put({
          type: 'toggleLoadding',
          payload: false,
        });
	    
        return true;
      }
      
      yield put({
        type: 'toggleLoadding',
        payload: false,
      });

      const err = yield parseError(error);
      yield message.error(`加载本次安装的商品失败:${err.status} ${err.message}`, 3);
      return false;
    },
    *add({ payload }, { put, call, select }) {
      yield put({
        type: 'toggleSubmiting',
        payload: true,
      });
      yield put({
        type: 'setCurrent',
        payload,
      });
      
      const access_token = yield select( state => state.oauth.access_token );
      const { data, error } = yield create(access_token, payload);

      if (!error) {
    	yield message.success('创建本次安装的商品成功', 2);
        yield put(routerRedux.goBack());
      } else {
    	const err = yield parseError(error);
        yield message.error(`创建本次安装的商品失败:${err.status} ${err.message}`, 3);
      }

      yield put({
        type: 'toggleSubmiting',
        payload: false,
      });
    },
    *edit({ payload }, { put, select }) {
      yield put({
        type: 'toggleSubmiting',
        payload: true,
      });
      yield put({
        type: 'setCurrent',
        payload,
      });
      
      const access_token = yield select( state => state.oauth.access_token );
      const { data, error } = yield update(access_token, payload);
      
      if (!error) {
	    yield message.success('更新本次安装的商品成功', 2);
        yield put(routerRedux.goBack());
      } else {
        const err = yield parseError(error);
        yield message.error(`更新本次安装的商品失败:${err.status} ${err.message}`, 3);
      }
      
      yield put({
        type: 'toggleSubmiting',
        payload: false,
      });
    },
  },

  reducers: {
    setQuery(state, { payload: query }) {
      return { ...state, query: mergeQuery(state.query, query) }
    },
    setGoods(state, { payload }) {
      return { ...state, ...payload }
    },
    setCurrent(state, { payload: current }) {
      return { ...state, current }
    },
    setInstallNum(state, { payload }) {
      const installGoods = state.installGoods
      const target = installGoods.map(item => {
        if (item.id == payload.id) {
          return { ...item, installNum: payload.value }
        }
      })
      return { ...state, installGoods: target }
    },
    toggleLoadding(state, { payload: loading }) {
      return { ...state, loading }
    },
    toggleSubmiting(state, { payload: submiting }) {
      return { ...state, submiting }
    },
    clear(state) {
      return initialState
    },
  }

}
