
import { routerRedux } from 'dva/router';
import { message } from 'antd';
import request, { parseError } from '../../utils/request';
import { root, search, view, create, update, remove, removeAll, searchCustomers, 
       finishOrder, auditOrder, payOrder, viewGood, saveFillOrBack, uploadOrder, getFollowStatus } from '../../services/order';
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
  orders: [],
  current: {},
  currentGoodInfo: {},
  pagination: {
    current: 1,
  },
  loading: false,
  submiting: false,
  modalShowWin: false,
  currentOrderId: undefined,
  finishModalShow: false,
  AuditModalShow: false,
  payModalShow: false,
  UploadModalShow: false,
  FollowModalShow: false,
  goodsEditing: false,
  queryCustomers: [],
  updateOrderGoods: [],
  followStatus: [],
}

export default {
  namespace: 'orders',

  state: initialState,

  subscriptions: {

    overListSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (location.pathname === '/order/orders/overOrders') {
          location.query.type = 2;
          dispatch({ type: 'clear' });
          dispatch({
            type: 'setQuery',
            payload: location.query,
          });
        }
      });
    },

    listSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (location.pathname === '/order/orders') {
          dispatch({ type: 'clear' })
          dispatch({
            type: 'setQuery',
            payload: location.query,
          });
        }
      });
    },
    
    itemSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (pathToRegexp('/order/orders/:action+').test(location.pathname)) {
          dispatch({ type: 'clear' })
        }
      })
    }, 
    
    editSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (pathToRegexp('/order/orders/edit/:id').test(location.pathname)) {
          const match = pathToRegexp('/order/orders/edit/:id').exec(location.pathname);
          const id = match[1];
          dispatch({
            type: 'view',
            payload: id,
          });
        }
      });
    },

    enterOutSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (pathToRegexp('/order/orders/enterOut/:id/:goodId').test(location.pathname)) {
          const match = pathToRegexp('/order/orders/enterOut/:id/:goodId').exec(location.pathname);
          const orderId = match[1];
          const goodId = match[2];
          dispatch({
            type: 'viewGoodInfo',
            payload: {
              id: orderId,
              goodId: goodId,
            },
          });
        }
      });
    },

    printSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (pathToRegexp('/order/orders/print/:id').test(location.pathname)) {
          const match = pathToRegexp('/order/orders/print/:id').exec(location.pathname);
          const id = match[1];
          // dispatch({
            
          // });
        }
      });
    },
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
          'oldQuery': state.orders.query,
        }
      } );
      const { data, error } = yield search(access_token, mergeQuery(oldQuery, query));

      if (!error) {
        yield put({
          type: 'setOrders',
          payload: {
            orders: data._embedded && data._embedded.orders || [],
            pagination: {
              current: data.page && data.page.number + 1,
              total: data.page && data.page.totalElements,
            }
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
      yield message.error(`加载订单管理失败:${err.status} ${err.message}`, 3);
      return false;
    }, { type: 'takeLatest' }],
    *view({ payload: id }, { put, select }) {
      yield put({
        type: 'toggleLoadding',
        payload: true,
      });

      const { access_token } = yield select( state => {
        return {
          'access_token': state.oauth.access_token,
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
      yield message.error(`加载订单管理详情失败:${err.status} ${err.message}`, 3);
      return false;
    },
    *add({ payload }, { put, call, select }) {
      yield put({
        type: 'toggleSubmiting',
        payload: true,
      });
      
      const access_token = yield select( state => state.oauth.access_token );
      const { data, error } = yield create(access_token, payload);

      if (!error) {
    	yield message.success('创建订单管理信息成功', 2);
        yield put(routerRedux.goBack());
      } else {
    	const err = yield parseError(error);
        yield message.error(`创建订单管理信息失败:${err.status} ${err.message}`, 3);
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
      
      const access_token = yield select( state => state.oauth.access_token );
      const { data, error } = yield update(access_token, payload);
      
      if (!error) {
	      yield message.success('更新订单管理信息成功', 2);
        yield put(routerRedux.goBack());
      } else {
        const err = yield parseError(error);
        yield message.error(`更新订单管理信息失败:${err.status} ${err.message}`, 3);
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
  	      query: state.orders.query,
        }
      } );
      const { data, error } = yield remove(access_token, id);
      
      if (!error) {
        yield message.success('成功删除订单', 2);
        yield put({ type: 'setQuery', payload: query })
        return true;
      }

      const err = yield parseError(error);
      yield message.error(`删除订单管理失败:${err.status} ${err.message}`, 3);
      return false;
    },
    *queryCustomer({ payload: query }, { put, select }) {
      yield put({
        type: 'toggleLoadding',
        payload: true,
      });

      const { access_token } = yield select( state => {
        return {
  	      access_token: state.oauth.access_token,
        }
      } );
      const { data, error } = yield searchCustomers(access_token, query);

      if (!error) {
        yield put({
          type: 'toggleLoadding',
          payload: false,
        });

        yield put({
          type: 'setQueryCustomers',
          payload: data,
        }) 
        return true;
      }
      
      yield put({
        type: 'toggleLoadding',
        payload: false,
      });

      const err = yield parseError(error);
      yield message.error(`加载客户信息失败:${err.status} ${err.message}`, 3);
      return false;
    },
    *finish({ payload: dataSourse }, {select, put}) {
      yield put({
        type: 'toggleSubmiting',
        payload: true,
      });

      const { access_token, finishOption, query } = yield select( state => {
        return {
  	      access_token: state.oauth.access_token,
          finishOption: {
            id: state.orders.currentOrderId,
            finishDate: dataSourse,
          },
          query: state.orders.query,
        }
      } );
      const { data, error } = yield finishOrder(access_token, finishOption);

      if (!error) {
        yield message.success('完成操作成功', 2);
        yield put({ type: 'setQuery', payload: query })
        return true;
      }

      const err = yield parseError(error);
      yield message.error(`完成操作失败:${err.status} ${err.message}`, 3);

      yield put({
        type: 'toggleSubmiting',
        payload: false,
      });

      yield put({
        type: 'toggleFinishModal',
        payload: {
          finishModalShow: false,
        },
      });

      return false;
    },
    *audit({ payload }, {select, put}) {

      const { access_token, id, query } = yield select( state => {
        return {
  	      access_token: state.oauth.access_token,
          id: state.orders.currentOrderId,
          query: state.orders.query,
        }
      } );
      const { data, error } = yield auditOrder(access_token, id);

      if (!error) {
        yield message.success('审核操作成功', 2);
        yield put({ type: 'setQuery', payload: query })
        return true;
      }

      const err = yield parseError(error);
      yield message.error(`审核操作失败:${err.status} ${err.message}`, 3);

      yield put({
        type: 'toggleAuditModal',
        payload: {
          AuditModalShow: false, 
        },
      });

      return false;
    },
    *pay({ payload: dataSourse }, {select, put}) {
      yield put({
        type: 'toggleSubmiting',
        payload: true,
      });

      const { access_token, payOption, query } = yield select( state => {
        return {
  	      access_token: state.oauth.access_token,
          payOption: {
            id: state.orders.currentOrderId,
            payData: dataSourse,
          },
          query: state.orders.query,
        }
      } );
      const { data, error } = yield payOrder(access_token, payOption);

      if (!error) {
        yield message.success('付款操作成功', 2);
        yield put({ type: 'setQuery', payload: query })
        return true;
      }

      const err = yield parseError(error);
      yield message.error(`付款操作失败:${err.status} ${err.message}`, 3);

      yield put({
        type: 'toggleSubmiting',
        payload: false,
      });

      yield put({
        type: 'toggleFinishModal',
        payload: {
          payModalShow: false,
        },
      });

      return false;
    },
    *viewGoodInfo({ payload: dataSourse }, {select, put}) {
      yield put({
        type: 'toggleLoadding',
        payload: true,
      });

      const { access_token, id, goodId } = yield select( state => {
        return {
          access_token: state.oauth.access_token,
          id: dataSourse.id,
          goodId: dataSourse.goodId,
        }
      } );
      const { data, error } = yield viewGood(access_token, id, goodId);

      if (!error) {
        yield put({
          type: 'setCurrentGoodInfo',
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
      yield message.error(`加载商品详情失败:${err.status} ${err.message}`, 3);
      return false;
    },
    *saveEnterOut({ payload }, {select, put}) {
      yield put({
        type: 'toggleSubmiting',
        payload: true,
      });
      
      const access_token = yield select( state => state.oauth.access_token );
      const { data, error } = yield saveFillOrBack(access_token, payload);

      if (!error) {
    	  yield message.success('创建退补货信息成功', 2);
        yield put(routerRedux.goBack());
      } else {
    	const err = yield parseError(error);
        yield message.error(`创建退补货信息失败:${err.status} ${err.message}`, 3);
      }

      yield put({
        type: 'toggleSubmiting',
        payload: false,
      });
    },
    *uploadOrder({ payload }, {select, put}) {
      yield put({
        type: 'toggleSubmiting',
        payload: true,
      });

      const { access_token, uploadOption, query } = yield select( state => {
        return {
  	      access_token: state.oauth.access_token,
          uploadOption: {
            id: state.orders.currentOrderId,
            orderPicture: payload,
          },
          query: state.orders.query,
        }
      } );
      console.log(uploadOption);
      const { data, error } = yield uploadOrder(access_token, uploadOption);

      if (!error) {
        yield message.success('上传纸质订单操作成功', 2);
        yield put({ type: 'setQuery', payload: query })
        return true;
      }

      const err = yield parseError(error);
      yield message.error(`上传纸质订单操作失败:${err.status} ${err.message}`, 3);

      yield put({
        type: 'toggleSubmiting',
        payload: false,
      });

      yield put({
        type: 'toggleUploadModal',
        payload: {
          UploadModalShow: false,
        },
      });

      return false;

    },
    *queryFollowStatus({payload: dataSourse}, {select, put}) {
      const { access_token, id, query } = yield select( state => {
        return {
  	      access_token: state.oauth.access_token,
          id: dataSourse.currentOrderId,
          query: state.orders.query,
        }
      } );
      
      const { data, error } = yield getFollowStatus(access_token, id);

      if (!error) {
        yield put({
          type: 'toggleFollowModal',
          payload: dataSourse,
        });
        
        return true;
      }

      const err = yield parseError(error);
      yield message.error(`跟踪订单状态失败:${err.status} ${err.message}`, 3);
      return false;
    }
  },

  reducers: {
    setQuery(state, { payload: query }) {
      return { ...state, query: mergeQuery(state.query, query) }
    },
    setOrders(state, { payload }) {
      return { ...state, ...payload }
    },
    setCurrent(state, { payload: current }) {
      return { ...state, current }
    },
    setCurrentGoodInfo(state, { payload: currentGoodInfo }) {
      return { ...state, currentGoodInfo }
    },
    toggleLoadding(state, { payload: loading }) {
      return { ...state, loading }
    },
    toggleSubmiting(state, { payload: submiting }) {
      return { ...state, submiting }
    },
    toggleModalShow(state, { payload: modalShowWin }) {
      return { ...state, modalShowWin }
    },
    toggleFinishModal(state, { payload }) {
      return { ...state, ...payload }
    },
    toggleAuditModal(state, { payload }) {
      return { ...state, ...payload }
    },
    togglePayModal(state, { payload }) {
      return { ...state, ...payload }
    },
    toggleUploadModal(state, { payload }) {
      return { ...state, ...payload }
    },
    toggleFollowModal(state, { payload }) {
      return { ...state, ...payload }
    },
    toggleOrdergoodsState(state, { payload }) {
      return { ...state, ...payload }
    },
    setQueryCustomers(state, { payload }) {
      return { ...state, ...payload }
    },
    setUpdateGoods(state, { payload }) {
      return { ...state, ...payload }
    },
    clear(state) {
      return initialState
    },
  }

}
