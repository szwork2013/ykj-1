import request from '../utils/request';
import querystring from 'querystring';

export async function root(access_token, query) {
  return request(`/api/orders?${querystring.stringify(query)}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  });
}

export async function search(access_token, query) {
  return request(`/api/orders/search?${querystring.stringify(query)}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  });
}

export async function view(access_token, id) {
  return request(`/api/orders/${id}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    },
  })
}

export async function create(access_token, order) {
  return request(`/api/orders`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(order)
  })
}

export async function update(access_token, order) {
  return request(`/api/orders/${id}`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(order)
  })
}

export async function remove(access_token, id) {
  return request(`/api/orders/${id}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  })
}

export async function removeAll(access_token, ids) {
  return request(`/api/orders`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(ids),
  })
}

export async function searchCustomers(access_token, query) {
  return request(`/api/customers/${querystring.stringify(query)}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    },
  })
}

export async function finishOrder(access_token, finishOption) {
  return request(`/api/orders/${finishOption.id}/finish_result`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(finishOption)
  })
}

export async function auditOrder(access_token, id) {
  return request(`/api/orders/${id}/audit_result`, {
    method: 'PATCH',
    headers: {
      'Authorization': `Bearer ${access_token}`,
    },
  })
}

export async function payOrder(access_token, payOption) {
  return request(`/api/orders/${payOption.id}/trade`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payOption)
  })
}

export async function viewGood(access_token, id, goodId) {
  return request(`/api/orders/${id}/goods/${goodId}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    },
  })
}

export async function saveFillOrBack(access_token, fillBackRecord) {
  return request(`/api/orders/${fillBackRecord.id}/back_fill_goods`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(fillBackRecord)
  })
}

export async function uploadOrder(access_token, uploadOption) {
  const formData = new FormData();
  formData.append('orderPicture', uploadOption.orderPicture);
  return request(`/api/orders/${uploadOption.id}`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${access_token}`,
    },
    body: formData,
  })
}

export async function getFollowStatus(access_token, id) {
  return request(`/api/orders/${id}/statuses`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    },
  })
}