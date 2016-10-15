import request from '../utils/request';
import querystring from 'querystring';

export async function search(query, access_token) {
	return request(`/api/customers/search?${querystring.stringify(query)}`, {
		method: 'GET',
		headers: {
			'Authorization': `Bearer ${access_token}`,
			'Content-Type': 'application/json',
		}
	})
}

export async function view(payload, access_token) {
	return request(`/api/customers/${payload}`, {
		headers: {
			'Authorization': `Bearer ${access_token}`,
			'Content-Type': 'application/json',
		}
	})
}

export async function create(payload, access_token) {
	return request(`/api/customers`, {
		method: 'POST',
		headers: {
			'Authorization': `Bearer ${access_token}`,
			'Content-Type': 'application/json',
		},
	 body: JSON.stringify(payload),
	})
}

export async function update(payload, access_token) {
	return request(`/api/customers/${payload.id}`, {
		method: 'PUT',
		headers: {
			'Authorization': `Bearer ${access_token}`,
			'Content-Type': 'application/json',
		},
	 body: JSON.stringify(payload),
	})
}

export async function remove(payload, access_token) {
	return request(`/api/customers/${payload}`, {
		method: 'DELETE',
		headers: {
			'Authorization': `Bearer ${access_token}`,
			'Content-Type': 'application/json',
		},
	})
}

export async function enabled(access_token, payload) {
	return request(`/api/customers/${payload}/enabled`, {
		method: 'PATCH',
		headers: {
			'Authorization': `Bearer ${access_token}`,
			'Content-Type': 'application/json',
		}
	})
}

export async function disabled(access_token, payload) {
	return request(`/api/customers/${payload}/disabled`, {
		method: 'PATCH',
		headers: {
			'Authorization': `Bearer ${access_token}`,
			'Content-Type': 'application/json',
		}
	})
}

export async function setCurrentByOrderId(access_token, payload) {
	return request(`/api/orders/${payload}/customer`, {
		headers: {
			'Authorization': `Bearer ${access_token}`,
			'Content-Type': 'application/json',
		}
	})
}
