import request from '../utils/request';
import querystring from 'querystring';

export async function list(access_token, payload) {
	return request(`/api/customers/${payload}/tags`, {
		method: 'GET',
		headers: {
			'Authorization': `Bearer ${access_token}`,
			'Content-Type': 'application/json',
		}
	})
}

export async function create(access_token, payload) {
	return request(`/api/customers/${payload.customerId}/tags`, {
		method: 'POST',
		headers: {
			'Authorization': `Bearer ${access_token}`,
			'Content-Type': 'application/json',
		},
	 body: JSON.stringify(payload),
	})
}

export async function remove(access_token, payload) {
	return request(`/api/customer_tags/${payload}`, {
		method: 'DELETE',
		headers: {
			'Authorization': `Bearer ${access_token}`,
			'Content-Type': 'application/json',
		},
	})
}