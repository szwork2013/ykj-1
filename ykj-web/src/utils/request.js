import fetch from 'dva/fetch';
import { routerRedux } from 'dva/router';

const ROOT_PATH = 'https://localhost:8443';
const ROOT_API_PATH = `${ROOT_PATH}/api`;
const TOKEN_API_PATH = `${ROOT_PATH}/oauth/token`;
const CLIENT_ID = 'public';

function isApiUrl(url) {
  if (url.startsWith(ROOT_PATH)) {
    return url;
  }
  
  return `${ROOT_PATH}${url}`;
}

function parseText(response) {
  return response.text();
}

function parseJSON(text) {
  if (text.replace(/(^\s*)|(\s*$)/g, "").length === 0) {
    return Promise.resolve({});
  }
  return Promise.resolve(JSON.parse(text));
}

function checkStatus(response) {
  if (response.status >= 200 && response.status < 300) {
    return response;
  }
  if (response.status === 401) {
    routerRedux.replace('/login');
    return false;
  }

  const error = new Error(response.statusText);
  error.response = response;
  throw error;
}

function request(url, options) {
  return fetch(isApiUrl(url), options)
    .then(checkStatus)
    .then(parseText)
    .then(parseJSON)
    .then((data) => ({ data }))
    .catch((error) => ({ error }));
}

function requestGetHeaders(url, options) {
  return fetch(isApiUrl(url), options)
    .then(checkStatus)
    .then(parseHeaders)
    .then((data) => ({ data }))
    .catch((error) => ({ error }));
}

function parseHeaders(response) {
  return response.headers;
}

function parseError(error) {
  try {
    return error.response.json();
  } catch (err) {
    return Promise.resolve({
      timestamp: new Date().getTime(),
      message: 'unkown error',
      status: error.response && error.response.status,
    });
  }

}

/**
 * Requests a URL, returning a promise.
 *
 * @param  {string} url       The URL we want to request
 * @param  {object} [options] The options we want to pass to "fetch"
 * @return {object}           An object containing either "data" or "err"
 */
export {
  request as default,
  requestGetHeaders,
  parseError,
}
