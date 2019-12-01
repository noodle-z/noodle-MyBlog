import {Message} from 'element-ui';
import axios from 'axios';
import router from '.././router/index';

axios.interceptors.request.use(config=> {
  var token = sessionStorage.getItem("token");
  if (token){
    config.headers['token'] = token;
  }
  return config;
}, err=> {
  Message.error({message: '请求超时!'});
  return Promise.resolve(err);
});
axios.interceptors.response.use(data=> {
  if (data.status && data.status == 200 && data.data.status == 'error') {
  }
  return data;
}, err=> {
  if (err.response && (err.response.status == 504||err.response.status == 404)) {
    Message.error({message: '我也不知道这是哪~~'});
  } else if (err.response && err.response.status == 403) {
    Message.error({message: err.response.data.message});
  } else if (err.response && err.response.status == 401) {
    Message.error({message: err.response.data});
    localStorage.removeItem("token");
    router.push("/login")
  } else {
    Message.error({message: '未知错误!'});
  }
  return Promise.resolve(err);
});

let base = 'http://localhost:8080';

export const postRequest = (url, params) => {
  return axios({
    method: 'post',
    url: `${base}${url}`,
    data: params,
    transformRequest: [function (data) {
      let ret = '';
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    headers: {
      'Content-Type': 'application/json'
    }
  });
};
export const uploadFileRequest = (url, params) => {
  return axios({
    method: 'post',
    url: `${base}${url}`,
    data: JSON.stringify(params),
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
};
export const putRequest = (url, params) => {
  return axios({
    method: 'put',
    url: `${base}${url}`,
    data: JSON.stringify(params),
    headers: {
      'Content-Type': 'application/json'
    }
  });
};
export const deleteRequest = (url) => {
  return axios({
    method: 'delete',
    url: `${base}${url}`
  });
};
export const getRequest = (url) => {
  return axios({
    method: 'get',
    url: `${base}${url}`
  });
};
