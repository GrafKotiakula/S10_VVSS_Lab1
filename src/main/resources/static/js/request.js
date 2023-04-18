function _getProp(params, propName, defaultValue) {
    if(propName in params) {
        return params[propName]
    } else {
        return defaultValue;
    }
}

function _getUrl(params) {
    return _getProp(params, 'url', '/')
}

function _getMethod(params) {
    return _getProp(params, 'method', 'GET')
}

function _getHeaders(params) {
    return _getProp(params, 'headers', {})
}

function _isJSON(params) {
    return _getProp(_getHeaders(params), 'Content-Type', undefined) == 'application/json';
}

function _getBody(params) {
    let body = _getProp(params, 'body', null)
    if(_isJSON(params)) {
        body = JSON.stringify(body)
    }
    return body
}

function request(params) {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        xhr.open(_getMethod(params), _getUrl(params))
        for (const [key, value] of Object.entries(_getHeaders(params))) {
            xhr.setRequestHeader(key, value)
        }
        xhr.onload = function () {
            let response = xhr.response
            if(_isJSON(params)) {
                response = JSON.parse(response)
            }
            if (this.status >= 200 && this.status < 300) {
                resolve({
                    status: xhr.status,
                    body: response
                })
            } else {
                reject({
                    status: xhr.status,
                    body: response
                })
            }
        };
        xhr.onerror = function () {
            console.error('SOMETHING WENT WRONG')
            reject(xhr)
        };
        xhr.send(_getBody(params))
    })
}
