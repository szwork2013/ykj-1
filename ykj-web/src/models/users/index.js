
import { routerRedux } from 'dva/router';
import { message } from 'antd';
import request, { parseError } from '../../utils/request';
import { search, create, update, view, remove } from '../../services/user';
import pathToRegexp from 'path-to-regexp';

const mergeQuery = (oldQuery, newQuery) => {
    return {
        ...oldQuery,
        ...newQuery,
        page: newQuery.page ? newQuery.page - 1 : 0,
    }
}

const initialState = {
    query: {},
    users: [],
    current: {},
    pagination: {
        current: 1,
    },
    loading: false,
    submiting: false,
}

export default {
    namespace: 'users',

    state: initialState,

    subscriptions: {
        listSubscription({dispatch, history}) {
            history.listen((location, state) => {
                if (location.pathname === '/config/users') {
                    dispatch({
                        type: 'query',
                        payload: location.query,
                    });
                }
            });
        },

        editSubscription({dispatch, history}) {
            history.listen((location, state) => {
                if(pathToRegexp('/config/users/edit/:id').test(location.pathname)) {
                    const match = pathToRegexp('/config/users/edit/:id').exec(location.pathname);
                    const id = match[1];
                    dispatch({
                        type: 'view',
                        payload: id,
                    });
                }
            })
        }

    },

    effects: {
        *'query'({payload:query}, { put, select}) {    
            yield put({
                type: 'toggleLoading',
                payload: true,
            });

            const { access_token, oldQuery } = yield select( (state) => {
                return {
                    "access_token": state.oauth.access_token,
                    "oldQuery": state.users.query,
                }
            });
            const { data, error } = yield search(access_token, mergeQuery(oldQuery, query));
           
            if (!error) {
                yield put({
                    type: "setUser",
                    payload: {
                        users: data._embedded && data._embedded.clerks || [],
                        pagination: {
                            current: data.page.number + 1,
                            total: data.page.totalElements,
                        }
                    },
                });

                yield put({
                    type: 'toggleLoading',
                    payload: false,
                });

                return true;
            }

            yield put({
                type: 'toggleLoading',
                payload: false,
            })

            const err = yield parseError(error);
            yield message.error(`加载用户失败:${err.status} ${err.message}`, 3);
            return false;
        },

        *'add'({payload}, {select, put, call}) {
            yield put({
                type: 'toggleSubmiting',
                payload: true,
            });

            const { access_token } = yield select( (state) => {
                return {
                    "access_token": state.oauth.access_token,
                }
            });

            const { data, error } = yield create(access_token, payload);
            if (!error) {
                yield message.success('创建员工信息成功', 2);
                yield put({
                    type: 'toggleSubmiting',
                    payload: false,
                });
                yield put(routerRedux.goBack());
            } else {
                const err = parseError(error);
                yield message.error(`创建员工信息失败:${err.status} ${err.message}`, 2);
                yield put({
                    type: 'toggleSubmiting',
                    payload: false,
                });
                return false;
            }
        },

        *'view'({ payload:id }, {select, put, call}) {
            yield put({
                type: 'toggleLoadding',
                payload: true,
            });

            const { access_token } = yield select( state => {
                return {
                    'access_token':state.oauth.access_token,
                }
            });

            const { data, error } = yield view(access_token, id);

            if (!error) {
                yield put({
                    type: 'setCurrent',
                    payload: data,
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
            yield message.error(`加载管理员管理详情失败:${err.status} ${err.message}`, 3);
            return false;
        },

        *'update'({payload}, {select, put, call}) {
            yield put({
                type: "toggleSubmiting",
                payload: true,
            });

            const { access_token } = yield select( state => {
                return {
                    'access_token':state.oauth.access_token,
                }
            });
            
            const { data, error } =yield update(access_token, payload);
            if(!error) {
                yield message.success(`更新员工信息成功`, 2);
                yield put(routerRedux.goBack());
                yield put({
                    type: "toggleSubmiting",
                    payload: false,
                });
                return true;
            } else {
                const err = yield parseError(error);
                yield message.error(`更新员工信息失败：${err.status} ${err.message}`, 2);
                yield put({
                    type: "toggleSubmiting",
                    payload: false,
                });
                return false;
            }
        },

        *'delete'({ payload:id }, {select, put, call}) {
             const { access_token } = yield select( state => {
                return {
                    'access_token':state.oauth.access_token,
                }
            });
            
            const { data, error } = yield remove(access_token, id);
            if(!error) {
                yield message.success(`成功删除`, 2);
                yield put({ type: 'query', payload: {} })
                return true;
            }

            const err = yield parseError(error);
            yield message.error(`删除失败：${err.status} ${err.message}`, 3);
            return false;
        }
    },

    reducers: {
        setQuery(state, { payload:query }) {
            return { ...state, query: mergeQuery(state.query, query) }
        },
        setUser(state, { payload }) {
            return { ...state, ...payload}
        },
        toggleLoadding(state, { payload: loading }) {
            return { ...state, loading }
        },
        toggleSubmiting(state, { payload: submiting }) {
            return { ...state, submiting }
        },
        setCurrent(state, { payload: current}) {
            return { ...state, current }
        },
        clear(state) {
            return { ...state, ...initialState}
        },
    }
}