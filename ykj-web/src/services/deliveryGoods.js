import request from '../utils/request';
import querystring from 'querystring';

export async function search(access_token, payload) {
  return request(`/api/deliverys/${payload}/delivery_goods`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  });
}

export async function view(access_token, id) {
  return request(`/api/deliverys/${id}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    },
  })
}

export async function create(access_token, delivery) {
  return request(`/api/deliverys`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(delivery)
  })
}

export async function update(access_token, delivery) {
  return request(`/api/deliverys/${deliveryGoods.deliveryId}/delivery_goods`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(deliveryGoods)
  })
}
