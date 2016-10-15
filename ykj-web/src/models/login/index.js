import pathToRegexp from 'path-to-regexp';

export default {

  namespace: 'login',

  state: {
    username: '',
    password: '',
    loading: false,
  },

  effects: {
    *submit({ payload }, { put }) {
      const resp = yield put({
        type: 'oauth/token',
        payload,
      })
    }
  },

  reducers: {
    setUsername(state, action) {
      return { ...state, username: action.payload };
    },
    setPassword(state, action) {
      return { ...state, password: action.payload };
    },
  },

}
