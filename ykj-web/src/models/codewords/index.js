
import { routerRedux } from 'dva/router';
import { message } from 'antd';
import request, { parseError } from '../../utils/request';
import { root, search, view, create, update, remove, removeAll } from '../../services/codeword';
import pathToRegexp from 'path-to-regexp';
import querystring from 'querystring';

const initialState = {
  query: {},
  codewords: [],
  current: {},
  codewordTypes: {},
  pagination: {
    current: 1,
  },
  loading: false,
  submiting: false,
  typeId: undefined,
}

export default {
  namespace: 'codewords',

  state: initialState,

  subscriptions: {
    listSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (location.pathname === '/setting/codewords') {
          dispatch({ type: 'clear' })
        }
      });
    },
    
    itemSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (pathToRegexp('/setting/codewords/:action+').test(location.pathname)) {
          
        }
      })
    }, 
    
    editSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (pathToRegexp('/setting/codewords/edit/:id').test(location.pathname)) {
          const match = pathToRegexp('/setting/codewords/edit/:id').exec(location.pathname);
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
      const value = query.codewordTypeValue || '';
      const typeValue = value.substring(0, value.indexOf('-'));
      const typeId = value.substring(value.indexOf('-')+1);

      const { access_token, oldQuery } = yield select( state => {
        return {
          'access_token': state.oauth.access_token,
          'oldQuery': state.codewords.query,
        }
      } );
      const { data, error } = yield search(access_token, {typeValue});
      if (!error) {
        yield put({
          type: 'setSearch',
          payload: query,
        });
        yield put({
          type: 'setCodewords',
          payload: {
            codewords: data._embedded && data._embedded.codewords || [],
            pagination: {
              current: data.page && data.page.number + 1,
              total: data.page && data.page.totalElements,
            },
            typeId
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
      yield message.error(`加载数据字典失败:${err.status} ${err.message}`, 3);
      return false;
    }, { type: 'takeLatest' }],
    *setCodewordsByType({ payload: typeValue }, { put, select }) {
      const access_token = yield select( state => state.oauth.access_token );
      const { data, error } = yield search(access_token, {typeValue});

      if (!error) {
        yield put({
          type: 'setCodeword',
          payload: {
            [typeValue]: data._embedded && data._embedded.codewords || []
          },
        })
        return true;
      }

      const err = yield parseError(error);
      yield message.error(`加载数据字典详情失败:${err.status} ${err.message}`, 3);
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
      
      const {access_token, typeId} = yield select( state => {
        return {
          access_token: state.oauth.access_token,
          typeId: state.codewords.typeId
        }
      } );
      payload.codewordTypeId = typeId;
      const { data, error } = yield create(access_token, payload);
      
      if (!error) {
    	yield message.success('创建数据字典信息成功', 2);
        yield put(routerRedux.goBack());
      } else {
    	const err = yield parseError(error);
        yield message.error(`创建数据字典信息失败:${err.status} ${err.message}`, 3);
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
	    yield message.success('更新数据字典信息成功', 2);
        yield put(routerRedux.goBack());
      } else {
        const err = yield parseError(error);
        yield message.error(`更新数据字典信息失败:${err.status} ${err.message}`, 3);
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
  	      query: state.codewords.query,
        }
      } );
      const { data, error } = yield remove(access_token, id);
      
      if (!error) {
        yield message.success('成功删除数据字典', 2);
        yield put({ type: 'setQuery', payload: query })
        return true;
      }

      const err = yield parseError(error);
      yield message.error(`删除数据字典失败:${err.status} ${err.message}`, 3);
      return false;
    },
  },

  reducers: {
    setSearch(state, { payload: query }) {
      return { ...state, query }
    },
    setCodewords(state, { payload }) {
      return { ...state, ...payload }
    },
    setCodeword(state, { payload }) {
      let codewordTypes = state.codewordTypes;
      codewordTypes = { ...codewordTypes, ...payload };
      return { ...state, codewordTypes }
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
    clear(state) {
      return initialState
    },
    clearTypeId(state) {
      return {...state, typeId: undefined}
    }
  }

}
