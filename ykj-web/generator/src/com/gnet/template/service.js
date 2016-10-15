import request from '../utils/request';
import querystring from 'querystring';

export async function root(access_token, query) {
  return request(`${ROOT}?${"$"}{querystring.stringify(query)}`, {
    headers: {
      'Authorization': `Bearer ${'$'}{access_token}`,
    }
  });
}

export async function search(access_token, query) {
  return request(`${SEARCH}?${"$"}{querystring.stringify(query)}`, {
    headers: {
      'Authorization': `Bearer ${'$'}{access_token}`,
    }
  });
}

export async function view(access_token, id) {
  return request(`${VIEW}`, {
    headers: {
      'Authorization': `Bearer ${'$'}{access_token}`,
    },
  })
}

export async function create(access_token, ${_moduleName}) {
  return request(`${ADD}`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${'$'}{access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(${_moduleName})
  })
}

export async function update(access_token, ${_moduleName}) {
  return request(`${EDIT}`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${'$'}{access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(${_moduleName})
  })
}

export async function remove(access_token, id) {
  return request(`${DELETE}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${'$'}{access_token}`,
    }
  })
}

export async function removeAll(access_token, ids) {
  return request(`${DELETE_BATCH}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${'$'}{access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(ids),
  })
}
