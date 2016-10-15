
import { routerRedux } from 'dva/router';
import { message } from 'antd';
import request, { parseError } from '../../utils/request';
import { root, search, view, create, update, remove, removeAll, rolePermissions } from '../../services/role';
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
  roles: [],
  current: {},
  pagination: {
    current: 1,
  },
  loading: false,
  submiting: false,
  currentPermissions: [],
}

export default {
  namespace: 'roles',

  state: initialState,

  subscriptions: {
    listSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (location.pathname === '/config/roles') {
          dispatch({
            type: 'setQuery',
            payload: location.query,
            origin: 'urlchange',
          });
        }
      });
    },

    editSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        if (pathToRegexp('/config/roles/edit/:id').test(location.pathname)) {
          const match = pathToRegexp('/config/roles/edit/:id').exec(location.pathname);
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

      const { access_token, oldQuery } = yield select( state => ({
        'access_token': state.oauth.access_token,
        'oldQuery': state.roles.query,
      }) );
      const { data, error } = yield search(access_token, mergeQuery(oldQuery, query));

      if (!error) {
        yield put({
          type: 'setRoles',
          payload: {
            roles: data._embedded && data._embedded.roleResources || [],
            pagination: {
              current: data.page.number + 1,
              total: data.page.totalElements,
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
      yield message.error(`加载角色管理失败:${err.status} ${err.message}`, 3);
      return false;
    }, { type: 'takeLatest' }],
    *view({ payload: id }, { put, select }) {
      yield put({
        type: 'toggleLoadding',
        payload: true,
      });

      const { access_token, roles } = yield select( state => {
        return {
          'access_token': state.oauth.access_token,
          'roles': state.roles.roles,
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
      yield message.error(`加载角色管理详情失败:${err.status} ${err.message}`, 3);
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
    	yield message.success('创建角色管理信息成功', 2);
        yield put(routerRedux.goBack());
      } else {
    	const err = yield parseError(error);
        yield message.error(`创建角色管理信息失败:${err.status} ${err.message}`, 3);
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
	    yield message.success('更新角色管理信息成功', 2);
        yield put(routerRedux.goBack());
      } else {
        const err = yield parseError(error);
        yield message.error(`更新角色管理信息失败:${err.status} ${err.message}`, 3);
      }
      
      yield put({
        type: 'toggleSubmiting',
        payload: false,
      });
    },
    *deleteOne({ payload: id }, { put, select }) {
      const access_token = yield select( state => state.oauth.access_token );
      const { data, error } = yield remove(access_token, id);
      
      if (!error) {
        yield message.success('成功删除', 2);
        return true;
      }

      const err = yield parseError(error);
      yield message.error(`删除失败:${err.status} ${err.message}`, 3);
      return false;
    },
    *permissions({ payload: roleId }, { put, select }) {
      const access_token = yield select( state => state.oauth.access_token );
      
      const { data, error } = yield rolePermissions(access_token, roleId);
      
      if (!error) {
        yield put({
          type: 'setCurrentPermissions',
          payload: data._embedded && data._embedded.roleResources || [],
        })
        return true;
      }

      const err = yield parseError(error);
      yield message.error(`获取角色[编号：${roleId}]权限失败:${err.status} ${err.message}`, 3);
      return false;
    }
  },

  reducers: {
    setQuery(state, { payload: query }) {
      return { ...state, query: mergeQuery(state.query, query) }
    },
    setRoles(state, { payload }) {
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
    clear(state) {
      return { ...state, ...initialState }
    },
    setCurrentPermissions(state, { payload: permissions }) {
      return { ...state, ...permissions }
    },
  }

}
