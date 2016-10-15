
import { routerRedux } from 'dva/router';
import { message } from 'antd';
import request, { parseError } from '../../utils/request';
import { root, search, view, create, update, remove, removeAll, statement, cancelStatement } from '../../services/delivery';
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
  deliverys: [{id:1, name: '123', isClear:true}],
  current: {id:1, name: '123', servicePosition: '13123', cost: 123},
  pagination: {
    current: 1,
  },
  loading: false,
  submiting: false,
  readonly: true,
  customerId: undefined,
}

export default {
  namespace: 'deliverys',

  state: initialState,

  subscriptions: {
    listSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (pathToRegexp('/order/orders/:id/deliverys').test(location.pathname)) {
          const match = pathToRegexp('/order/orders/:id/deliverys').exec(location.pathname);
          const id = match[1];
          dispatch({ type: 'clear' })
          dispatch({
            type: 'setQuery',
            payload: id,
          });
          dispatch({
            type: 'customers/setCurrentByOrderId',
            payload: id
          })
        }
      });
    },
    
    itemSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (pathToRegexp('/order/deliverys/:action+').test(location.pathname)) {
          dispatch({ type: 'clear' })
        }
      })
    }, 
    
    editSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (pathToRegexp('/order/orders/:id/deliverys/edit/:id').test(location.pathname)) {
          const match = pathToRegexp('/order/orders/:id/deliverys/edit/:id').exec(location.pathname);
          const id = match[1];
          dispatch({
            type: 'view',
            payload: id,
          });
        }
      });
    },

    addSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (pathToRegexp('/order/orders/:id/deliverys/add').test(location.pathname)) {
          const match = pathToRegexp('/order/orders/:id/deliverys/add').exec(location.pathname);
          const id = match[1];
          dispatch({
            type: 'toggleReadonly',
            payload: false,
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
          'oldQuery': state.deliverys.query,
        }
      } );
      const { data, error } = yield search(access_token, query);

      if (!error) {
        yield put({
          type: 'setMeasures',
          payload: {
            deliverys: data._embedded && data._embedded.orderSers || [],
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
      yield message.error(`加载测量安排失败:${err.status} ${err.message}`, 3);
      return false;
    }, { type: 'takeLatest' }],
    *view({ payload: id }, { put, select }) {
      yield put({
        type: 'toggleLoadding',
        payload: true,
      });

      const { access_token, deliverys } = yield select( state => {
        return {
          'access_token': state.oauth.access_token,
          'deliverys': state.deliverys.deliverys,
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
      yield message.error(`加载测量安排详情失败:${err.status} ${err.message}`, 3);
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
    	yield message.success('创建测量安排信息成功', 2);
        yield put(routerRedux.goBack());
      } else {
    	const err = yield parseError(error);
        yield message.error(`创建测量安排信息失败:${err.status} ${err.message}`, 3);
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
	      yield message.success('更新测量安排信息成功', 2);
        yield put({
          type: 'toggleReadonly',
          payload: true
        });
      } else {
        const err = yield parseError(error);
        yield message.error(`更新测量安排信息失败:${err.status} ${err.message}`, 3);
      }
      
      yield put({
        type: 'toggleSubmiting',
        payload: false,
      });
    },
    *deleteOne({ payload: id }, { put, select }) {
	    const { access_token, query } = yield select( state => {
        return {
  	      access_token: state.oauth.access_token,
  	      query: state.deliverys.query,
        }
      } );
      const { data, error } = yield remove(access_token, id);
      
      if (!error) {
        yield message.success('成功删除测量安排', 2);
        yield put({ type: 'setQuery', payload: query })
        return true;
      }

      const err = yield parseError(error);
      yield message.error(`删除测量安排失败:${err.status} ${err.message}`, 3);
      return false;
    },
    *statement({ payload: id }, { put, select }) {
	    const access_token = yield select( state => state.oauth.access_token );

      const { data, error } = yield statement(access_token, id);
      
      if (!error) {
        yield message.success('成功确认结算标识', 2);
        yield put({ type: 'setQuery', payload: query })
        return true;
      }

      const err = yield parseError(error);
      yield message.error(`确认结算标识失败:${err.status} ${err.message}`, 3);
      return false;
    },
    *cancelStatement({ payload: id }, { put, select }) {
	    const access_token = yield select( state => state.oauth.access_token );

      const { data, error } = yield cancelStatement(access_token, id);
      
      if (!error) {
        yield message.success('成功取消结算标识', 2);
        yield put({ type: 'setQuery', payload: query })
        return true;
      }

      const err = yield parseError(error);
      yield message.error(`取消结算标识失败:${err.status} ${err.message}`, 3);
      return false;
    },
  },

  reducers: {
    setQuery(state, { payload: query }) {
      return { ...state, query: mergeQuery(state.query, query) }
    },
    setMeasures(state, { payload }) {
      return { ...state, ...payload }
    },
    setCurrent(state, { payload: current }) {
      return { ...state, current }
    },
    toggleLoadding(state, { payload: loading }) {
      return { ...state, loading }
    },
    toggleSubmiting(state, { payload: submiting }) {
      return { ...state, submiting }
    },
    toggleReadonly(state, { payload: readonly }) {
      return { ...state, readonly }
    },
    clear(state) {
      return initialState
    },

  }

}
