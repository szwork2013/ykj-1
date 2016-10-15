import request from '../utils/request';
import querystring from 'querystring';

export async function list(access_token, payload) {
  return request(`/api/customers/${payload}/tracks`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
  })
}

export async function view(access_token, payload) {
  return request(`/api/customer_tracks/${payload}`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
  })
}

export async function create(access_token, payload) {
  return request(`/api/customer_tracks`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload)
  })
}

export async function update(access_token, payload) {
  return request(`/api/customer_tracks/${payload.id}`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload)
  })
}

export async function remove(access_token, payload) {
  return request(`/api/customer_tracks/${payload}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
  })
}
