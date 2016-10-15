import request from '../../utils/request';
import pathToRegexp from 'path-to-regexp';
import querystring from 'querystring';
import { message } from 'antd';
import { routerRedux } from 'dva/router';
import { search, view, create, update, remove, enabled, disabled, setCurrentByOrderId } from '../../services/customer';
import { parseError } from '../../utils/request';

const mergeQuery = (oldQuery, newQuery) => {
  return {
    ...newQuery,
    page: (newQuery.page ? newQuery.page - 1 : 0),
  }
}

const initialState = {
    query: {},
    current: {},
    customers: [],
    loading: false,
    pagination: {
        current: 1,
    },
    customerResponsibleId: undefined,
}

export default {

    namespace: 'customers',

    state: initialState,

    subscriptions: {
        listSubscriptions({ dispatch, history }) {
            history.listen(location => {
                if (location.pathname === '/customers/customers') {
                    dispatch({ type: 'clear' })
                    dispatch({
                        type: 'setQuery',
                        payload: location.query,
                        origin: 'urlchange',
                    });
                }
            });
        },
        itemSubscriptions({ dispatch, history }) {
            history.listen((location, state) => {
                if (pathToRegexp('/customers/customers/:action+').test(location.pathname)) {
                    dispatch({ type: 'clear' })
                    dispatch({ type: 'houses/clear' })
                    dispatch({ type: 'tags/clear' })
                    dispatch({ type: 'tracks/clear' })
                }
            })
        }, 
        editSubscriptions({ dispatch, history }) {
            history.listen(location => {
                if (pathToRegexp('/customers/customers/edit/:id').test(location.pathname)) {
                    const match = pathToRegexp('/customers/customers/edit/:id').exec(location.pathname);
                    const id = match[1];
                    dispatch({
                        type: 'view',
                        payload: id,
                    });
                    dispatch({
                        type: 'tags/list',
                        payload: id
                    })
                    dispatch({
                        type: 'codewords/setCodewordsByType',
                        payload: 'SEX'
                    });
                    dispatch({
                        type: 'codewords/setCodewordsByType',
                        payload: 'CUSTOMER_NEED_TIME'
                    });
                } 
            });
        },
        addSubscriptions({ dispatch, history }) {
            history.listen(location => {
                if (location.pathname === '/customers/customers/add') {
                    dispatch({
                        type: 'tags/clearTags',
                    });
                    dispatch({
                        type: 'codewords/setCodewordsByType',
                        payload: 'SEX'
                    });
                    dispatch({
                        type: 'codewords/setCodewordsByType',
                        payload: 'CUSTOMER_NEED_TIME'
                    });
                    dispatch({
                        type: 'codewords/setCodewordsByType',
                        payload: 'CUSTOMER_SOURCE'
                    });
                }
            });
        },
        housesSubscription({ dispatch, history }) {
            history.listen(location => {
                if (pathToRegexp('/customers/customers/:id/houses').test(location.pathname)) {
                    const match = pathToRegexp('/customers/customers/:id/houses').exec(location.pathname);
                    const id = match[1];
                    dispatch({ type: 'houses/clear' })
                    dispatch({
                        type: 'view',
                        payload: id,
                    });
                    dispatch({
                        type: 'houses/list',
                        payload: id,
                    });
                } 
            });
        },
        tracksSubscription({ dispatch, history }) {
            history.listen(location => {
                if (pathToRegexp('/customers/customers/:id/tracks').test(location.pathname)) {
                    const match = pathToRegexp('/customers/customers/:id/tracks').exec(location.pathname);
                    const id = match[1];
                    dispatch({
                        type: 'view',
                        payload: id,
                    });
                    dispatch({
                        type: 'tracks/list',
                        payload: id,
                    });
                } 
            });
        },
    },

    effects: {
        *setQuery({ payload }, { put, call, select }) {
            yield put({
                type: 'toggleLoadding',
                payload: true,
            });
            const query = { [payload.type]: payload.value }
            const { access_token, oldQuery } = yield select( state => ({
                'access_token': state.oauth.access_token,
                'oldQuery': state.customers.query,
            }) );
            const { data, error } = yield call(search, mergeQuery(oldQuery, query), access_token);

            if (!error) {
                yield put({
                    type: 'setCustomers',
                    payload: {
                        customers: data._embedded && data._embedded.customers || [],
                        pagination: data._embedded && data._embedded.page || {}
                    },
                    query: payload
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
            yield message.error(`加载列表信息失败:${err.status} ${err.message}`, 3);
            return false;
        },
        *view({ payload }, { put, call, select }) {
            const { access_token, customers } = yield select(state => {
                return {
                    'access_token': state.oauth.access_token,
                    'customers': state.customers.customers,
                }
            });
            const { data, error } = yield call(view, payload, access_token);

            if (!error) {
                yield put({
                    type: 'setCustomer',
                    payload: data
                })
                yield put({
                    type: 'tracks/setCustomerResponsibleId',
                    payload: data.customerResponsibleId
                })
                yield put({
                    type: 'setCustomerResponsibleId',
                    payload: data.customerResponsibleId
                })
                return true;
            }
            
            const err = yield parseError(error);
            yield message.error(`加载客户信息失败:${err.status} ${err.message}`, 3);
            return false;
        },
        *add({ payload }, { put, call, select }) {
            const {access_token, customerResponsibleId} = yield select(state => {
                return {
                    access_token: state.oauth.access_token,
                    customerResponsibleId: state.customers.customerResponsibleId
                }
            });
            payload.customerResponsibleId = customerResponsibleId;
            const { data, error } = yield create(payload, access_token);

            if (!error) {
                yield message.success('添加客户信息成功', 2);
                yield put(routerRedux.goBack());
                return true;
            }

            const err = yield parseError(error);
            yield message.error(`添加客户信息失败:${err.status} ${err.message}`, 3);
            return false;
        },
        *update({ payload }, { put, call, select }) {
            const {access_token, customerResponsibleId} = yield select(state => {
                return {
                    access_token: state.oauth.access_token,
                    customerResponsibleId: state.customers.customerResponsibleId
                }
            });
            payload.customerResponsibleId = customerResponsibleId;
            const { data, error } = yield call(update, payload, access_token);

            if (!error) {
                yield message.success('更新客户信息成功', 2);
                yield put(routerRedux.goBack());
                return true;
            }

            const err = yield parseError(error);
            yield message.error(`更新客户信息失败:${err.status} ${err.message}`, 3);
            return false;
        },
        *deleteOne({ payload }, { put, call, select }) {
            const {access_token, query} = yield select(state => {
                return {
                    access_token: state.oauth.access_token,
                    query: state.customers.query,
                }
            });
            const { data, error } = yield call(remove, payload, access_token);

            if (!error) {
                yield message.success('删除客户信息成功', 2);
                yield put({ type: 'setQuery', payload: query })
                return true;
            }

            const err = yield parseError(error);
            yield message.error(`删除客户信息失败:${err.status} ${err.message}`, 3);
            return false;
        },
        *enabled({ payload }, { put, call, select }) {
            const {access_token, query} = yield select(state => {
                return {
                    access_token: state.oauth.access_token,
                    query: state.customers.query,
                }
            });
            const { data, error } = yield enabled(access_token, payload);

            if (!error) {
                yield message.success('客户设置有效成功', 2);
                yield put({ type: 'setQuery', payload: query })
                return true;
            }

            const err = yield parseError(error);
            yield message.error(`客户设置有效失败:${err.status} ${err.message}`, 3);
            return false;
        },
        *disabled({ payload }, { put, call, select }) {
            const {access_token, query} = yield select(state => {
                return {
                    access_token: state.oauth.access_token,
                    query: state.customers.query,
                }
            });
            const { data, error } = yield disabled(access_token, payload);

            if (!error) {
                yield message.success('客户设置无效成功', 2);
                yield put({ type: 'setQuery', payload: query })
                return true;
            }

            const err = yield parseError(error);
            yield message.error(`客户设置无效失败:${err.status} ${err.message}`, 3);
            return false;
        },
         *setCurrentByOrderId({ payload: orderId }, { put, select }) {
            const { access_token, orders } = yield select( state => {
                return {
                    access_token: state.oauth.access_token,
                    orders: state.orders.orders,
                }
            });

            const { data, error } = yield setCurrentByOrderId(access_token, orderId);

            if (!error) {
                yield message.success('获取客户信息成功', 2);
                yield put({
                    type: 'setCustomer',
                    payload: data
                })
                return true;
            }

            const err = yield parseError(error);
            yield message.error(`获取客户信息失败:${err.status} ${err.message}`, 3);
            return false;
        }
    },

    reducers: {
        setQuery(state, { payload: query }) {
            return { ...state, query: mergeQuery(state.query, query) }
        },
        setCustomers(state, { payload }) {
            return {...state, ...payload}
        },
        setCustomer(state, { payload }) {
            return {...state, current: payload}
        },
        addCustomer(state, { payload }) {
            return {...state, customers: state.customers.push(payload) }
        },
        deleteCustomer(state, { payload }) {
            return {...state, customers: state.customers.filter(customer => customer.id !== payload), messgae: {}}
        },
        toggleLoadding(state, { payload: loading }) {
            return { ...state, loading }
        },
        setCustomerResponsibleId(state, { payload }) {
            return { ...state, customerResponsibleId: payload }
        }
    },
}


