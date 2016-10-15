import request from '../../utils/request';
import querystring from 'querystring';
import { routerRedux } from 'dva/router';
import { query } from '../../services/permission';

export default {
  namespace: 'permission',
  state: {
    permissions: [], // 用于存权限列表
    permissionMap: {} // 用于存分组后的权限，key为分组编号,value为permission的详细内容
  },
  effects: {
    *'query'({ payload={} }, { select, call, put }) {
      const permissions = yield query();
      payload.permissions = permissions;
      
      yield put( { type: 'savePermissions', payload } );
      yield put( { type: 'savePermissionMap', payload } );
    }
  },
  reducers: {
    'savePermissions'( state, { payload={}} ) {
      const permissions = payload.permissions || [];
      return {...state, permissions }
    },
    'savePermissionMap'( state, { payload } ) {
      let permissionMap = {};
      const permissions = payload.permissions || [];
      permissions.forEach((permission) => {
        permission.id = `${permission.id}`
        let tempPermissions = permissionMap[permission.groupId];
        if(tempPermissions) {
          tempPermissions.push(permission);
        } else {
          tempPermissions = [permission];
          permissionMap[permission.groupId] = tempPermissions;
        }
      })

      return {...state, permissionMap}
    }
  }
}
