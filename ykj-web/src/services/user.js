import request from '../utils/request';
import querystring from 'querystring';

export async function search(access_token, query) {
    return request(`/api/clerks?${querystring.stringify(query)}`, {
        headers: {
            'Authorization': `Bearer ${access_token}`,
        }
    });
}

export async function create(access_token, user) {
    return request(`/api/clerks`, {
        method: "POST",
        headers: {
            'Authorization': `Bearer ${access_token}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user),
    })
}

export async function update(access_token, user) {
    return request(`/api/clerks/${user.id}`, {
        method: "PUT",
        headers: {
            'Authorization': `Bearer ${access_token}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user),
    })
}

export async function view(access_token, id) {
    return request(`/api/clerks/${id}`, {
        headers: {
            'Authorization': `Bearer ${access_token}`,
        }
    })
}

export async function remove(access_token, id) {
    return request(`/api/clerks/${id}`, {
        method: "DELETE",
        headers: {
            'Authorization': `Bearer ${access_token}`,
        }
    })
}