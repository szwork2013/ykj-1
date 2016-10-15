import request from '../utils/request';
import querystring from 'querystring';

export async function root(access_token, query) {
  return request(`/api/designs?${querystring.stringify(query)}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  });
}

export async function search(access_token, payload) {
  return request(`/api/orders/${payload}/designs`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  });
}

export async function view(access_token, id) {
  return request(`/api/designs/${id}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    },
  })
}

export async function create(access_token, design) {
  return request(`/api/designs`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(design)
  })
}

export async function update(access_token, design) {
  return request(`/api/designs/${design.id}`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(design)
  })
}

export async function remove(access_token, id) {
  return request(`/api/designs/${id}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  })
}

export async function removeAll(access_token, ids) {
  return request(`/api/designs`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(ids),
  })
}

export async function statement(access_token, id) {
  return request(`/api/designs/${id}/statement`, {
    method: 'PATCH',
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  })
}

export async function cancelStatement(access_token, id) {
  return request(`/api/designs/${id}/cancelStatement`, {
    method: 'PATCH',
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  })
}
