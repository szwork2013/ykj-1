import pathToRegexp from 'path-to-regexp'
import { query, save, update, remove, view, zoneRoles, areaRoles, groupRoles } from '../../services/role';

export default {
  namespace: 'role',
  state: {
    list: [],
    pageNumber: 1,
    loading: true,
    submiting: false,
    detail: {},
    detailStatus: 0, // 0：正常状态，1：提交中，2：提交成功，3：提交失败
    byOrgIds: [],
  },
  subscriptions: {
    setup({ dispatch, history }) {
      let keys = [];
      history.listen(({ pathname }, state) => {
        if ('/config/role' === pathname) {
          dispatch({ type: 'query' })
        } else if ('/config/role/add' === pathname) {
          dispatch({ type: 'initDetail' })
          dispatch({ type: 'permission/query' })
        } else if (pathToRegexp('/config/role/edit/:id').test(pathname)) {
          const match = pathToRegexp('/config/role/edit/:id', keys);
          const params = match.exec(pathname);
          const roleId = params[params.length - 1];

          dispatch({ type: 'permission/query' })
          dispatch({ type: 'view', payload: { id: roleId } });
        }
      });
    }
  },
  effects: {
    *'query'({ payload }, { select, call, put }) {
      yield put({ type: 'startLoad' })
      const access_token = yield select( state => state.oauth.access_token );
      const result = yield query(access_token, payload);

      if(result.error) {
        yield put({ type: 'endLoad' })
        // todo: to warn user the error message
      } else {
        yield put({
          type: 'querySuccess', 
          payload: {
            list: (result.data._embedded || {}).roleResources || [],
            page: result.data.page
          }
        })
      }
    },
    *'save'({ payload }, { select, call, put }) {
      const roleDetail = payload.roleDetail;
      const callback = payload.callback; // 回调方法

      yield put({type: 'startSubmit'});
      const access_token = yield select( state => state.oauth.access_token );
      const result = yield save(roleDetail, access_token);

      if(!result.msg) {
        // todo 用于提示成功消息
        yield put({ type: 'message/showMessage', payload: { type: 'success', message: `保存角色[${roleDetail.name}]成功`} })
        yield put({ type: 'endSubmit', payload: { detailStatus: 2 } })
      } else {
        // todo 用于提示失败消息
        yield put({ type: 'message/showMessage', payload: { type: 'error', message: `保存角色[${roleDetail.name}]失败`} })
        yield put({ type: 'endSubmit', payload: { detailStatus: 3 } })
      }

      if(callback && typeof callback === "function") {
        callback(result);
      }
    },
    *'update'({ payload }, { select, call, put }) {
      const roleDetail = payload.roleDetail;
      const callback = payload.callback; // 回调方法

      yield put({type: 'startSubmit'});
      const access_token = yield select( state => state.oauth.access_token );
      const result = yield update(roleDetail, access_token);

      if(!result.msg) {
        // todo 用于提示成功消息
        yield put({ type: 'message/showMessage', payload: { type: 'success', message: `更新角色[${roleDetail.name}]成功`} })
        yield put({ type: 'endSubmit', payload: { detailStatus: 2 } })
      } else {
        // todo 用于提示失败消息
        yield put({ type: 'message/showMessage', payload: { type: 'error', message: `更新角色[${roleDetail.name}]失败`} })
        yield put({ type: 'endSubmit', payload: { detailStatus: 3 } })
      }

      if(callback && typeof callback === "function") {
        callback(result);
      }
    },
    *'delete'({ payload }, {select, call, put}) {
      const access_token = yield select( state => state.oauth.access_token );
      const result = yield remove(payload.id, access_token);

      if(result.data) {
        // todo 提示删除成功
        const dispatch = payload.dispatch;
        dispatch({ type: 'role/query', payload: payload.formData });
      } else {
        // todo 提示删除错误
      }
    },
    *'view'({payload}, {select, call, put}) {
      const access_token = yield select( state => state.oauth.access_token );
      const result = yield view(payload.id, access_token);
      if(result.msg) {
        // todo 提示查看错误
      } else {
        const detail = result.data;
        yield put({ type: 'editDetail', payload:{ ...detail } })
      }
    },
    *byOrgId({ payload }, { select, put }) {
      const { orgId, roleType } = payload;
      const access_token = yield select( state => state.oauth.access_token );
      switch(roleType) {
        case '1':
          const { data: groupRolesPayload, error: error1 } = yield groupRoles(access_token);
          
          if (!error1) {
            yield put({
              type: 'setOrgIds',
              payload: groupRolesPayload,
            })
          }
          break;
        case '2':
          const { data: areaRolesPayload, error: error2 } = yield areaRoles(access_token);

          if (!error2) {
            yield put({
              type: 'setOrgIds',
              payload: areaRolesPayload,
            })
          }
          break;
        case '3':
          const { data: zoneRolesPayload, error: error3 } = yield zoneRoles(access_token);

          if (!error3) {
            yield put({
              type: 'setOrgIds',
              payload: zoneRolesPayload,
            })
          }
          break;
        case '4':
          const { data: zoneAllRolesPayload, error: error4 } = yield zoneRoles(access_token);

          if (!error4) {
            yield put({
              type: 'setOrgIds',
              payload: zoneAllRolesPayload,
            })
          }
          break;
      }
    }
  },
  reducers: {
    'startLoad'(state) {
      return {...state, loading: true}
    },
    'endLoad'(state) {
      return {...state, loading: false}
    },
    'querySuccess'(state, { payload }) {
      return {...state, list: payload.list, loading: false}
    },
    'startSubmit'(state) {
      return {...state, submiting: true, detailStatus: 1}
    },
    'editDetail'(state, { payload }) {
      let detail = {};
      Object.assign(detail, state.detail, payload);
      return {...state, detail}
    },
    'endSubmit'(state, { payload }) {
      return {...state, submiting: false, detailStatus: payload.detailStatus}
    },
    'initDetail'(state) {
      return {...state, detail: {}, detailStatus: 0, submiting: false}
    },
    setOrgIds( state, { payload } ) {
      return { ...state, byOrgIds: payload }
    }
  }
}