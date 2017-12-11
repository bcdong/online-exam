import {http} from './http'

function downloadFile (url, token, callback) {
  let authHeader = {Authorization: 'Bearer ' + token}
  http.get(url, {headers: authHeader, responseType: 'arraybuffer'})
    .then(function (res) {
      let blob = new Blob([res.data], {type: 'application/octet-stream'})
      let url = window.URL.createObjectURL(blob)
      let linkElement = document.createElement('a')
      linkElement.setAttribute('href', url)
      let filename = ''
      let disp = res.headers['content-disposition']
      if (disp) {
        filename = disp.match(/filename="(.+)"/)[1]
        filename = decodeURIComponent(filename)
      }
      linkElement.setAttribute('download', filename)
      let clickEvent = new MouseEvent('click', {
        'view': window,
        'bubbles': true,
        'cancelable': false
      })
      linkElement.dispatchEvent(clickEvent)
    })
    .catch(function (err) {
      callback(err)
    })
}

export {downloadFile}
