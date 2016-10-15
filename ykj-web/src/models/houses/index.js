
import { routerRedux } from 'dva/router';
import { message } from 'antd';
import request, { parseError } from '../../utils/request';
import { list, create, update, remove, removeAll } from '../../services/house';
import pathToRegexp from 'path-to-regexp';
import querystring from 'querystring';

const initialState = {
  houses: [],
  loading: false,
  submiting: false,
  visible: false,
  customerId: undefined,
}

export default {
  namespace: 'houses',

  state: initialState,

  subscriptions: {
    listSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        
      });
    },
    
  },

  effects: {
    *list({ payload }, { put, call, select}) {
      const { access_token } = yield select( state => {
        return {
          'access_token': state.oauth.access_token,
        }
      } );
      const { data, error } = yield list(access_token, payload);
      if (!error) {
        yield put({
          type: 'setHouses',
          payload:{ 
            houses: data._embedded && data._embedded.customerHouseProperties || [], 
            customerId: payload
          },
        })
        return true;
      }
      
      const err = yield parseError(error);
      yield message.error(`加载房产信息失败:${err.status} ${err.message}`, 3);
      return false;
    },
    *add({ payload }, { put, call, select }) {
      const {access_token, customerId} = yield select( state => {
        return {
          access_token: state.oauth.access_token,
          customerId: state.houses.customerId
        }
      } );
      payload.customerId = customerId;
      const { data, error } = yield create(access_token, payload);

      if (!error) {
  	    yield message.success('创建房产信息成功', 2);
        yield put({
          type: 'list',
          payload: customerId,
        })
        return true;
      }

      const err = yield parseError(error);
      yield message.error(`创建房产信息失败:${err.status} ${err.message}`, 3);
      return false;
    },
    *edit({ payload }, { put, select }) {
      const {access_token, customerId} = yield select( state => {
        return {
          access_token: state.oauth.access_token,
          customerId: state.houses.customerId
        }
      });

      payload.customerId = customerId;
      const { data, error } = yield update(access_token, payload);
      
      if (!error) {
        yield message.success('更新房产信息成功', 2);
        yield put({
          type: 'updateHouse',
          payload,
        })
        return true;
      }
      
      const err = yield parseError(error);
      yield message.error(`更新房产信息失败:${err.status} ${err.message}`, 3);
      return false;
    },
    *deleteOne({ payload }, { put, call, select }) {
      const {access_token, customerId} = yield select( state => {
        return {
          access_token: state.oauth.access_token,
          customerId: state.houses.customerId
        }
      });
      const { data, error } = yield remove(access_token, payload);

      if (!error) {
        yield message.success('删除房产信息成功', 2);
        yield put({
          type: 'list',
          payload: customerId,
        })
        return true;
      }
      
      const err = yield parseError(error);
      yield message.error(`删除房产信息失败:${err.status} ${err.message}`, 3);
      return false;
    }
  },

  reducers: {
    setHouses(state, { payload }) {
      return { ...state, ...payload }
    },
    setHouse(state, { payload }) {
      state.houses.push(payload)
      return { ...state }
    },
    deleteHouse(state, { payload }) {
      return { ...state, houses: state.houses.filter(house => house.id !== payload)}
    },
    updateHouse(state, { payload }) {
      return { ...state, houses: state.houses.map(house => {
        if (house.id === payload.id) {
          return payload;
        } else {
          return house;
        }
      })}
    },
    toggleLoadding(state, { payload: loading }) {
      return { ...state, loading }
    },
    toggleSubmiting(state, { payload: submiting }) {
      return { ...state, submiting }
    },
    show(state, { payload }) {
      return { ...state, visible: true }
    },
    hide(state, { payload }) {
      return { ...state, visible: false }
    },
    clear(state) {
      return initialState;
    }
  }

}
