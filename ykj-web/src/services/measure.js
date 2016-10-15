import request, {requestGetHeaders} from '../utils/request';
import querystring from 'querystring';

export async function root(access_token, query) {
  return request(`/api/measures?${querystring.stringify(query)}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  });
}

export async function search(access_token, payload) {
  return request(`/api/orders/${payload}/measures`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  });
}

export async function view(access_token, id) {
  return request(`/api/measures/${id}`, {
    headers: {
      'Authorization': `Bearer ${access_token}`,
    },
  })
}

export async function create(access_token, measure) {
  return request(`/api/measures`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(measure)
  })
}

export async function update(access_token, measure) {
  return request(`/api/measures/${measure.id}`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(measure)
  })
}

export async function remove(access_token, id) {
  return request(`/api/measures/${id}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  })
}

export async function removeAll(access_token, ids) {
  return request(`/api/measures`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${access_token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(ids),
  })
}

export async function statement(access_token, id) {
  return request(`/api/measures/${id}/statement`, {
    method: 'PATCH',
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  })
}

export async function cancelStatement(access_token, id) {
  return request(`/api/measures/${id}/cancelStatement`, {
    method: 'PATCH',
    headers: {
      'Authorization': `Bearer ${access_token}`,
    }
  })
}

export async function upload(access_token, payload) {
  let formData = new FormData();
  formData.append('file', payload.fileData);
  return requestGetHeaders(`/api/measures/attachment_upload?fileType=file`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${access_token}`,
    },
    body: formData,
  })
}
