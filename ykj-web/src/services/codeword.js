import request from '../utils/request';
import querystring from 'querystring';

export async function root(access_token, query) {
  return request(`/api/codeWords?${querystring.stringify(query)}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  });
}

export async function search(access_token, query) {
  return request(`/api/codeWords?${querystring.stringify(query)}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
  });
}

export async function view(access_token, id) {
  return request(`/api/codeWords/${id}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
  })
}

export async function create(access_token, codeword) {
  return request(`/api/codeWords`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(codeword)
  })
}

export async function update(access_token, codeword) {
  return request(`/api/codeWords/${id}`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(codeword)
  })
}

export async function remove(access_token, id) {
  return request(`/api/codeWords/${id}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  })
}

export async function removeAll(access_token, ids) {
  return request(`/api/codeWords`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(ids),
  })
}
