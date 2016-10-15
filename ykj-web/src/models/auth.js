import request from '../utils/request';

export default {
  namespace: 'oauth',

  state: {
    authentication: localStorage.getItem('authentication') || false,
    access_token: localStorage.getItem('access_token') || '',
    uid: localStorage.getItem('uid') || '',
  },

  effects: {
    *'token'({ payload }, { select, call, put }) {
      const { username, password } = payload;
      const { data, error } = yield request('/oauth/token', {
        method: 'POST',
        headers: {
          'Authorization': 'Basic dHJ1c3RlZDpzZWNyZXQ=',  // trusted
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `grant_type=password&username=${username}&password=${password}`,
      });
      
      if (error) {
        // TODO send error message to global show
        return false;
      }

      yield put({
        type: 'loginSuccess',
        payload: {
          authentication: true,
          access_token: data.access_token,
          uid: data.uid,
        }
      });

      yield localStorage.setItem('authentication', true);
      yield localStorage.setItem('access_token', data.access_token);
      yield localStorage.setItem('uid', data.uid);
    }
  },

  reducers: {
    loginSuccess(state, { payload }) {
      return { ...state, ...payload };
    }
  }
}
