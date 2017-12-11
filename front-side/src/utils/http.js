import axios from 'axios'
import qs from 'qs'

var root = '/api/v1'

var http = axios.create({
  baseURL: root,
  transformRequest: [function (data) {
    data = qs.stringify(data)
    return data
  }],
  headers: {'Content-Type': 'application/x-www-form-urlencoded'},
  timeout: 10000
})

var httpJson = axios.create({
  baseURL: root,
  headers: {'Content-Type': 'application/json'},
  timeout: 10000
})

export {http, httpJson}

// function apiAxios (method, url, params, success, failure) {
//   console.log(params)
//   axios({
//     method: method,
//     url: url,
//     data: method === 'POST' || method === 'PUT' ? params : null,
//     params: method === 'GET' || method === 'DELETE' ? params : null,
//     baseURL: root,
//     withCredentials: false
//   })
//     .then(function (res) {
//       if (res.data.code === 200) {
//         if (success) {
//           success(res.data)
//         }
//       } else {
//         if (failure) {
//           failure(res.data)
//         } else {
//           console.error('Error: ' + JSON.stringify(res.data))
//         }
//       }
//     })
//     .catch(function (err) {
//       if (err.response) {
//         // if (err.response.status === 401) {
//         //
//         // }
//         console.error('Api error: Http code is: ' + err.response.status)
//         console.error(err.response)
//       } else if (err.request) {
//         console.error('Api error: no response is received')
//         console.error(err.request)
//       } else {
//         console.error('Api error: there is error when setting up request.\nMessage is\n' + err.message)
//       }
//     })
// }
