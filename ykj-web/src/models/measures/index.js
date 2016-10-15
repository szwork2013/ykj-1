
import { routerRedux } from 'dva/router';
import { message } from 'antd';
import request, { parseError } from '../../utils/request';
import { root, search, view, create, update, remove, removeAll, statement, cancelStatement, upload } from '../../services/measure';
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
    measures: [],
    current: {},
    pagination: {
        current: 1,
    },
    loading: false,
    submiting: false,
    customerId: undefined,
    fileList: [{
        id: '12qweqweqwewqeqeqw',
        serviceId: '123123',
        status: 0
    }]
}

export default {
    namespace: 'measures',

    state: initialState,

    subscriptions: {
        listSubscriptions({ dispatch, history }) {
            history.listen((location, state) => {
                if (pathToRegexp('/order/orders/:id/measures').test(location.pathname)) {
                    const match = pathToRegexp('/order/orders/:id/measures').exec(location.pathname);
                    const id = match[1];
                    dispatch({ type: 'clear' })
                    dispatch({
                        type: 'setQuery',
                        payload: id,
                    });
                    dispatch({
                        type: 'customers/setCurrentByOrderId',
                        payload: id
                    })
                }
            });
        },

        itemSubscriptions({ dispatch, history }) {
            history.listen((location, state) => {
                if (pathToRegexp('/order/orders/:id/measures/:action+').test(location.pathname)) {
                    dispatch({ type: 'clear' })
                }
            })
        },

        editSubscriptions({ dispatch, history }) {
            history.listen((location, state) => {
                if (pathToRegexp('/order/orders/:id/measures/edit/:id').test(location.pathname)) {
                    const match = pathToRegexp('/order/orders/:id/measures/edit/:id').exec(location.pathname);
                    const orderId = match[1];
                    const id = match[2];
                    dispatch({
                        type: 'view',
                        payload: id,
                    });
                    dispatch({
                        type: 'customers/setCurrentByOrderId',
                        payload: orderId
                    })
                }
            });
        },

        addSubscriptions({ dispatch, history }) {
            history.listen((location, state) => {
                if (pathToRegexp('/order/orders/:id/measures/add').test(location.pathname)) {
                    const match = pathToRegexp('/order/orders/:id/measures/add').exec(location.pathname);
                    const id = match[1];
                    dispatch({
                        type: 'customers/setCurrentByOrderId',
                        payload: id
                    })
                }
            });
        }
    },

    effects: {
        setQuery: [function* ({ payload: query }, { put, select }) {
            yield put({
                type: 'toggleLoadding',
                payload: true,
            });

            const { access_token, oldQuery } = yield select(state => {
                return {
                    'access_token': state.oauth.access_token,
                    'oldQuery': state.measures.query,
                }
            });
            const { data, error } = yield search(access_token, query);

            if (!error) {
                yield put({
                    type: 'setMeasures',
                    payload: {
                        measures: data._embedded && data._embedded.orderSers || [],
                        pagination: {
                            current: data.page && data.page.number + 1,
                            total: data.page && data.page.totalElements,
                        },
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
            yield message.error(`加载测量安排失败:${err.status} ${err.message}`, 3);
            return false;
        }, { type: 'takeLatest' }],
        *view({ payload: id }, { put, select }) {
            yield put({
                type: 'toggleLoadding',
                payload: true,
            });

            const { access_token, measures } = yield select(state => {
                return {
                    'access_token': state.oauth.access_token,
                    'measures': state.measures.measures,
                }
            });
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
            yield message.error(`加载测量安排详情失败:${err.status} ${err.message}`, 3);
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

            const access_token = yield select(state => state.oauth.access_token);
            const { data, error } = yield create(access_token, payload);

            if (!error) {
                yield message.success('创建测量安排信息成功', 2);
                yield put(routerRedux.goBack());
            } else {
                const err = yield parseError(error);
                yield message.error(`创建测量安排信息失败:${err.status} ${err.message}`, 3);
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

            const {access_token, measure} = yield select(state => {
                return {
                    access_token: state.oauth.access_token,
                    measure: state.measures.current,
                }
            });
            let newMeasure = { ...measure, ...payload };
            const { data, error } = yield update(access_token, newMeasure);

            if (!error) {
                yield message.success('更新测量安排信息成功', 2);
                yield put(routerRedux.goBack());
            } else {
                const err = yield parseError(error);
                yield message.error(`更新测量安排信息失败:${err.status} ${err.message}`, 3);
            }

            yield put({
                type: 'toggleSubmiting',
                payload: false,
            });
        },
        *deleteOne({ payload: id }, { put, select }) {
            const { access_token, query } = yield select(state => {
                return {
                    access_token: state.oauth.access_token,
                    query: state.measures.query,
                }
            });
            const { data, error } = yield remove(access_token, id);

            if (!error) {
                yield message.success('成功删除测量安排', 2);
                yield put({ type: 'setQuery', payload: query })
                return true;
            }

            const err = yield parseError(error);
            yield message.error(`删除测量安排失败:${err.status} ${err.message}`, 3);
            return false;
        },
        *statement({ payload: id }, { put, select }) {
            const access_token = yield select(state => state.oauth.access_token);

            const { data, error } = yield statement(access_token, id);

            if (!error) {
                yield message.success('成功确认结算标识', 2);
                yield put({ type: 'setQuery', payload: query })
                return true;
            }

            const err = yield parseError(error);
            yield message.error(`确认结算标识失败:${err.status} ${err.message}`, 3);
            return false;
        },
        *cancelStatement({ payload: id }, { put, select }) {
            const access_token = yield select(state => state.oauth.access_token);

            const { data, error } = yield cancelStatement(access_token, id);

            if (!error) {
                yield message.success('成功取消结算标识', 2);
                yield put({ type: 'setQuery', payload: query })
                return true;
            }

            const err = yield parseError(error);
            yield message.error(`取消结算标识失败:${err.status} ${err.message}`, 3);
            return false;
        },
        *upload({ payload }, { put, select }) {
            const access_token = yield select(state => state.oauth.access_token);
            let fileList = payload.fileList;
            let file = payload.file
            console.log(file, fileList)
            const { data, error } = yield upload(access_token, file);
            if (!error) {
                yield message.success('上传附件成功', 2);
                payload.replace({...file, status: 2, serviceId: 'data'}, fileList)
                console.log(fileList, 'new')
                yield put({
                    type: 'setFileList',
                    payload: fileList       
                })
                return true;
            }   
            const newFileList = payload.replace({...file, status: 3}, payload.fileList);
            yield put({
                type: 'setFileList',
                payload: newFileList
            })

            const err = yield parseError(error);
            yield message.error(`上传附件失败:${err.status} ${err.message}`, 3);
            return false;
        }
},

reducers: {
    setQuery(state, { payload: query }) {
        return { ...state, query: mergeQuery(state.query, query) }
    },
    setMeasures(state, { payload }) {
        return { ...state, ...payload }
    },
    setCurrent(state, { payload: current }) {
        return { ...state, current }
    },
    setFileList(state, { payload }) {
        return { ...state, fileList: payload }
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
}

}
