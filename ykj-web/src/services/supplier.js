import request from '../utils/request';
import querystring from 'querystring';

export async function root(access_token, query) {
  return request(`/api/suppliers?${querystring.stringify(query)}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  });
}

export async function search(access_token, query) {
  return request(`/api/suppliers/search?${querystring.stringify(query)}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  });
}

export async function view(access_token, id) {
  return request(`/api/suppliers/${id}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    },
  })
}

export async function create(access_token, supplier) {
  return request(`/api/suppliers`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(supplier)
  })
}

export async function update(access_token, supplier) {
  return request(`/api/suppliers/${supplier.id}`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(supplier)
  })
}

export async function remove(access_token, id) {
  return request(`/api/suppliers/${id}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  })
}

export async function removeAll(access_token, ids) {
  return request(`/api/suppliers`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(ids),
  })
}
