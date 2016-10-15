
import { routerRedux } from 'dva/router';
import { message } from 'antd';
import request, { parseError } from '../../utils/request';
import { list, create, update, remove, view } from '../../services/track';
import pathToRegexp from 'path-to-regexp';
import querystring from 'querystring';


const initialState = {
  tracks: [],
  current: {},
  loading: false,
  submiting: false,
  showAddModal: false,
  showEditModal: false,
  editTarget: undefined,
  customerId: undefined,
  customerResponsibleId: undefined,
}

export default {
  namespace: 'tracks',

  state: initialState,

  subscriptions: {
    listSubscriptions({ dispatch, history }) {
      history.listen((location, state) => {
        
      });
    },
    
  },

  effects: {
    *list({ payload }, { put, call, select}) {
      yield put({
        type: 'toggleLoadding',
        payload: true,
      });
      const { access_token } = yield select( state => {
        return {
          'access_token': state.oauth.access_token,
        }
      } );
      const { data, error } = yield list(access_token, payload);
      
      if (!error) {
        yield put({
          type: 'setTracks',
          payload: {
            tracks: data._embedded && data._embedded.customerTracks || [],
            customerId: payload
          },
        })
        yield put({
          type: 'toggleLoadding',
          payload: true,
        });
        return true;
      }
      yield put({
        type: 'toggleLoadding',
        payload: true,
      });
      const err = yield parseError(error);
      yield message.error(`加载跟进记录失败:${err.status} ${err.message}`, 3);
      return false;
    },
    *add({ payload }, { put, call, select }) {
      yield put({
        type: 'toggleSubmiting',
        payload: true,
      });
      const {access_token, customerId, customerResponsibleId} = yield select( state => {
        return {
          access_token: state.oauth.access_token,
          customerId: state.tracks.customerId,
          customerResponsibleId: state.tracks.customerResponsibleId
        }
      });
      payload.customerId = customerId;
      payload.customerResponsibleId = customerResponsibleId;
      const { data, error } = yield create(access_token, payload);

      if (!error) {
  	    yield message.success('创建跟进记录成功', 2);
        yield put({
          type: 'list',
          payload: customerId,
        })
        yield put({
          type: 'toggleSubmiting',
          payload: false,
        });
        yield put({
          type: 'toggleAddModal',
          payload: false,
        });
        return true;
      }
      yield put({
        type: 'toggleSubmiting',
        payload: false,
      });
      const err = yield parseError(error);
      yield message.error(`创建跟进记录失败:${err.status} ${err.message}`, 3);
      return false;
    },
    *view({ payload: id }, { put, select }) {
      yield put({
        type: 'toggleEditModal',
        payload: {
          showEditModal: true,
          editTarget: id
        }
      });
      const {access_token, customerId} = yield select( state => {
        return {
          access_token: state.oauth.access_token,
          customerId: state.tracks.customerId
        }
      } );
      const { data, error } = yield view(access_token, id);

      if (!error) {
        yield put({
          type: 'setTrack',
          payload: data
        })
        return true;
      }
      
      const err = yield parseError(error);
      yield message.error(`更新跟进记录失败:${err.status} ${err.message}`, 3);
      return false;
    },
    *edit({ payload }, { put, select }) {
      const {access_token, customerId, customerResponsibleId, editTarget} = yield select( state => {
        return {
          access_token: state.oauth.access_token,
          editTarget: state.tracks.editTarget,
          customerId: state.tracks.customerId,
          customerResponsibleId: state.tracks.customerResponsibleId
        }
      });
      payload.id = editTarget;
      payload.customerId = customerId;
      payload.customerResponsibleId = customerResponsibleId;
      const { data, error } = yield update(access_token, payload);
      if (!error) {
        yield message.success('更新跟进记录成功', 2);
        yield put({
          type: 'list',
          payload: customerId,
        })
        yield put({
        type: 'toggleEditModal',
        payload: {
          showEditModal: false,
        }
      });
        return true;
      }
      
      const err = yield parseError(error);
      yield message.error(`更新跟进记录失败:${err.status} ${err.message}`, 3);
      return false;
    },
    *deleteOne({ payload }, { put, call, select }) {
      const {access_token, customerId} = yield select( state => {
        return {
          access_token: state.oauth.access_token,
          customerId: state.tracks.customerId,
        }
      });
      const { data, error } = yield remove(access_token, payload);

      if (!error) {
        yield message.success('删除跟进记录成功', 2);
        yield put({ 
          type: 'list',
          payload: customerId
        })
        return true;
      }
      
      const err = yield parseError(error);
      yield message.error(`删除跟进记录失败:${err.status} ${err.message}`, 3);
      return false;
    }
  },

  reducers: {
    setTracks(state, { payload }) {
      return { ...state, ...payload }
    },
    setTrack(state, { payload }) {
      return { ...state, current: payload }
    },
    setCustomerResponsibleId(state, { payload }) {
      return { ...state, customerResponsibleId: payload}
    },
    deleteTrack(state, { payload }) {
      return { ...state, tracks: state.tracks.filter(track => track.id !== payload)}
    },
    updateTrack(state, { payload }) {
      return { ...state, tracks: state.tracks.map(track => {
        if (track.id === payload.id) {
          return payload;
        } else {
          return track;
        }
      })}
    },
    toggleLoadding(state, { payload: loading }) {
      return { ...state, loading }
    },
    toggleSubmiting(state, { payload: submiting }) {
      return { ...state, submiting }
    },
    toggleAddModal(state, { payload }) {
      return { ...state, showAddModal: payload, current: {} }
    },
    toggleEditModal(state, { payload }) {
      return { ...state, ...payload }
    },
    clear(state) {
      return initialState;
    }
  }

}
