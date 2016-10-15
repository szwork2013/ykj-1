import request from '../utils/request';
import querystring from 'querystring';

export async function root(access_token, query) {
  return request(`/api/installations?${querystring.stringify(query)}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  });
}

export async function search(access_token, payload) {
  return request(`/api/orders/${payload}/installations`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  });
}

export async function view(access_token, id) {
  return request(`/api/installations/${id}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    },
  })
}

export async function create(access_token, installation) {
  return request(`/api/installations`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(installation)
  })
}

export async function update(access_token, installation) {
  return request(`/api/installations/${installation.id}`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(installation)
  })
}

export async function remove(access_token, id) {
  return request(`/api/installations/${id}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  })
}

export async function removeAll(access_token, ids) {
  return request(`/api/installations`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(ids),
  })
}

export async function statement(access_token, id) {
  return request(`/api/installations/${id}/statement`, {
    method: 'PATCH',
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  })
}

export async function cancelStatement(access_token, id) {
  return request(`/api/installations/${id}/cancelStatement`, {
    method: 'PATCH',
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  })
}
