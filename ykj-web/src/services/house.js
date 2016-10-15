import request from '../utils/request';
import querystring from 'querystring';

export async function list(access_token, payload) {
  return request(`/api/customers/${payload}/houses`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
  })
}

export async function create(access_token, house) {
  return request(`/api/customer_houses`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(house)
  })
}

export async function update(access_token, payload) {
  return request(`/api/customer_houses/${payload.id}`, {
		method: 'PUT',
		headers: {
			'Authorization': `Bearer ${access_token}`,
			'Content-Type': 'application/json',
		},
	 body: JSON.stringify(payload),
	})
}

export async function remove(access_token, id) {
  return request(`/api/customer_houses/${id}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
  })
}
