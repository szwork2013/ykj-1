
import { routerRedux } from 'dva/router';
import { message } from 'antd';
import request, { parseError } from '../../utils/request';
import { root, search, view, create, update, remove, removeAll } from '../../services/${_moduleName}';
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
  ${_moduleName}s: [],
  <#if canDelAll>
  selectedRowKeys: [],
  </#if>
  current: {},
  pagination: {
    current: 1,
  },
  loading: false,
  submiting: false,
  <#if canDelAll>
  batchDelLoading: false,
  </#if>
}

export default {
  namespace: '${_moduleName}s',

  state: initialState,

  subscriptions: {
    listSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (location.pathname === '/${packageName}/${_moduleName}s') {
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
        if (pathToRegexp('/${packageName}/${_moduleName}s/:action+').test(location.pathname)) {
          dispatch({ type: 'clear' })
        }
      })
    }, 
    
    editSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (pathToRegexp('/${packageName}/${_moduleName}s/edit/:id').test(location.pathname)) {
          const match = pathToRegexp('/${packageName}/${_moduleName}s/edit/:id').exec(location.pathname);
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
          'oldQuery': state.${_moduleName}s.query,
        }
      } );
      const { data, error } = yield search(access_token, mergeQuery(oldQuery, query));

      if (!error) {
        yield put({
          type: 'set${moduleName}s',
          payload: {
            ${_moduleName}s: data._embedded && data._embedded.${_moduleName}s || [],
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
      yield message.error(`加载${moduleCName}失败:${"$"}{err.status} ${"$"}{err.message}`, 3);
      return false;
    }, { type: 'takeLatest' }],
    *view({ payload: id }, { put, select }) {
      yield put({
        type: 'toggleLoadding',
        payload: true,
      });

      const { access_token, ${_moduleName}s } = yield select( state => {
        return {
          'access_token': state.oauth.access_token,
          '${_moduleName}s': state.${_moduleName}s.${_moduleName}s,
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
      yield message.error(`加载${moduleCName}详情失败:${"$"}{err.status} ${"$"}{err.message}`, 3);
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
    	yield message.success('创建${moduleCName}信息成功', 2);
        yield put(routerRedux.goBack());
      } else {
    	const err = yield parseError(error);
        yield message.error(`创建${moduleCName}信息失败:${"$"}{err.status} ${"$"}{err.message}`, 3);
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
	    yield message.success('更新${moduleCName}信息成功', 2);
        yield put(routerRedux.goBack());
      } else {
        const err = yield parseError(error);
        yield message.error(`更新${moduleCName}信息失败:${"$"}{err.status} ${"$"}{err.message}`, 3);
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
  	      query: state.${_moduleName}s.query,
        }
      } );
      const { data, error } = yield remove(access_token, id);
      
      if (!error) {
        yield message.success('成功删除${moduleCName}', 2);
        yield put({ type: 'setQuery', payload: query })
        return true;
      }

      const err = yield parseError(error);
      yield message.error(`删除${moduleCName}失败:${"$"}{err.status} ${"$"}{err.message}`, 3);
      return false;
    },
    <#if canDelAll>
    *batchDelete({ payload: ids }, { put, select }) {
      const { access_token, query } = yield select( state => {
          return {
    	    access_token: state.oauth.access_token,
    	    query: state.${_moduleName}s.query,
    	  }
      } );
      const { data, error } = yield removeAll(access_token, ids);
      
      if (!error) {
        yield message.success('成功批量删除${moduleCName}', 2);
        return true;
      }
  
      const err = yield parseError(error);
      yield message.error(`批量删除${moduleCName}失败:${"$"}{err.status} ${"$"}{err.message}`, 3);
      return false;
    },
    </#if>
  },

  reducers: {
    setQuery(state, { payload: query }) {
      return { ...state, query: mergeQuery(state.query, query) }
    },
    set${moduleName}s(state, { payload }) {
      return { ...state, ...payload }
    },
    setCurrent(state, { payload: current }) {
      return { ...state, current }
    },
    <#if canDelAll>
    selectRows(state, { payload: selectedRowKeys }) {
      return { ...state, selectedRowKeys }
    },
    </#if>
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
