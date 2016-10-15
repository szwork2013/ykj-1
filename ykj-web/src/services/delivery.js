import request from '../utils/request';
import querystring from 'querystring';

export async function root(access_token, query) {
  return request(`/api/deliverys?${querystring.stringify(query)}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  });
}

export async function search(access_token, payload) {
  return request(`/api/orders/${payload}/deliverys`, {
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
  return request(`/api/deliverys/${delivery.id}`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(delivery)
  })
}

export async function remove(access_token, id) {
  return request(`/api/deliverys/${id}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  })
}

export async function removeAll(access_token, ids) {
  return request(`/api/deliverys`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(ids),
  })
}

export async function statement(access_token, id) {
  return request(`/api/deliverys/${id}/statement`, {
    method: 'PATCH',
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  })
}

export async function cancelStatement(access_token, id) {
  return request(`/api/deliverys/${id}/cancelStatement`, {
    method: 'PATCH',
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  })
}
