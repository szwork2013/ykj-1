import request from '../../utils/request';
import { routerRedux } from 'dva/router';
import { list, create, update, remove } from '../../services/tag';
import { parseError } from '../../utils/request';

import { message } from 'antd';

const initialState = {
    customerId: undefined,
    loading: false,
    tags: [],
    visible: false,
}
export default {

    namespace: 'tags',

    state: {
    customerId: undefined,
    loading: false,
    tags: [],
    visible: false,
},

    effects: {
        *list({ payload }, { put, call, select }) {
            const access_token = yield select(state => state.oauth.access_token);
            const { data, error } = yield list(access_token, payload);
            if (!error) {
                yield put({
                    type: 'setTags',
                    payload: {
                        tags: data._embedded && data._embedded.tagses || [],
                        customerId: payload
                    },
                })
                return true
            }
            const err = yield parseError(error);
            yield message.error(`加载标签失败:${err.status} ${err.message}`, 3);
            return false;
        },
        *add({ payload }, { put, call, select }) {
            const {access_token, customerId} = yield select(state => {
                return {
                    access_token: state.oauth.access_token,
                    customerId: state.tags.customerId
                }
            });
            const tag = {
                name: payload,
                customerId
            }
            const { data, error } = yield create(access_token, tag);
            if (!error) {
                yield message.success('添加标签成功', 2);
                yield put({
                    type: 'list',
                    payload: customerId
                })
                return true
            }
            const err = yield parseError(error);
            yield message.error(`添加标签失败:${err.status} ${err.message}`, 3);
            return false;
        },
        *deleteOne({ payload }, { put, call, select }) {
            const {access_token, customerId} = yield select(state => {
                return {
                    access_token: state.oauth.access_token,
                    customerId: state.tags.customerId
                }
            });
            const { data, error } = yield remove(access_token, payload);

            if (!error) {
                yield message.success('删除标签成功', 2);
                yield put({
                    type: 'deleteTag',
                    payload
                });
                return true;
            }
            const err = yield parseError(error);
            yield message.error(`删除标签失败:${err.status} ${err.message}`, 3);
            return false;
        },
    },

    reducers: {
        setTags(state, { payload }) {
            return {...state, ...payload}
        },
        addTag(state, { payload }) {
            state.tags.push({
                name: payload,
                id: new Date().getTime(),
            });
            return {...state, tags: state.tags }
        },
        updateTag(state, { payload }) {
            return {...state, tags: state.tags.map(tag => {
                        if(tag.id === payload.id){
                            return payload;
                        }
                    }) }
        },
        deleteTag(state, { payload }) {
            return {...state, tags: state.tags.filter(tag => tag.id !== payload)}
        },
        show(state, { payload }) {
            return {...state, visible: true}
        },
        hide(state, { payload }) {
            return {...state, visible: false}
        },
        clear(state) {
            return {
                customerId: undefined,
                loading: false,
                tags: [],
                visible: false,
            }
        }
    },
}


