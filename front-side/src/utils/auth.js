import {http} from './http.js'

// 当token失效时间在30分钟内则需要刷新token
const refreshTime = 30 * 60 * 1000

function decodeJwt (token) {
  let base64Url = token.split('.')[1]
  let base64 = base64Url.replace('-', '+').replace('_', '/')
  return JSON.parse(window.atob(base64))
}

function refreshToken (token) {
  let authHeader = {Authorization: 'Bearer ' + token}
  http.post('auth/refreshToken', {oldToken: token}, {headers: authHeader})
    .then(function (res) {
      if (res.data.code === 200) {
        let newToken = res.data.data
        localStorage.setItem('token', newToken)
      }
    })
    .catch(function (err) {
      console.error('刷新token失败')
      console.error(err)
    })
}

// 获取jwt token，并且检查token是否失效。
// 如果没失效且即将在30分钟内失效则返回token，然后更新token；
// 如果已经失效则返回false
function getAndRefreshJwt () {
  let token = localStorage.getItem('token')
  if (!token) {
    return false
  }
  let tokenPayload = decodeJwt(token)
  // jwt的exp是秒数，created是毫秒数，坑爹啊
  let expTime = tokenPayload.exp * 1000
  let nowTime = new Date().getTime()
  if (expTime < nowTime) {
    return false
  } else {
    if ((expTime - nowTime) < refreshTime) {
      refreshToken(token)
    }
    return token
  }
}

export {getAndRefreshJwt}
