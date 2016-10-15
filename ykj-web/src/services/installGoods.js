import request from '../utils/request';
import querystring from 'querystring';

export async function search(access_token, payload) {
  return request(`/api/installations/${payload}/installation_goods`, {
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
  return request(`/api/installations/${installation.installId}/installation_goods`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(installation)
  })
}
