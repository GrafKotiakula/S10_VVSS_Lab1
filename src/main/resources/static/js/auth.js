const jwtTokenStorageName = 'Token'
const authPopup = getAuthPopup()
const headerUsername = document.getElementById('header-username')
const notAuthMessage = document.getElementById('not-auth-message')

document.getElementById('header-btn').onclick = e => logout()
tryLogin()

function parseAuthPopupInput(id) {
    const node = document.getElementById(id)
    return {
        container: node,
        input: node.getElementsByTagName('input')[0],
        error: node.getElementsByClassName('err-msg')[0]
    }
}

function getAuthPopup() {
    const states = Object.freeze({ LOG_IN:0, SIGN_UP:1 });
    const form = document.getElementById('auth-form')
    const result = {
        popup: document.getElementById('auth-popup'),
        form: form,
        username: parseAuthPopupInput('username'),
        password: parseAuthPopupInput('password'),
        repeatPassword: parseAuthPopupInput('repeat-password'),
        mainErr: form.getElementsByClassName('main-err')[0],
        submitBtn: form.getElementsByClassName('btn-primary')[0],
        changeBtn: form.getElementsByClassName('btn-secondary')[0],
        state: states.LOG_IN,
        show: function() { this.popup.style.display='block' },
        hide: function() {
            this.popup.style.display='none'
            this.clearInput()
        },
        clearInput: function() {
            this.username.input.value='';
            this.username.error.innerText='';
            this.password.input.value='';
            this.password.error.innerText='';
            this.repeatPassword.input.value='';
            this.repeatPassword.error.innerText='';
            this.mainErr.innerText = ''
        }
    }

    result.repeatPassword.container.style.display = 'none';
    result.form.getElementsByClassName('btn-secondary')[0].onclick = function() {
        tmp = result.submitBtn.value
        result.submitBtn.value = result.changeBtn.value
        result.changeBtn.value = tmp;
        if(result.state === states.LOG_IN) {
            result.repeatPassword.container.style.display='flex'
            result.state = states.SIGN_UP
        } else {
            result.repeatPassword.container.style.display='none'
            result.state = states.LOG_IN
        }
    }

    form.addEventListener('submit', function(event) {
        event.preventDefault()
        if(result.state === states.LOG_IN) {
            authPopupLogin(result)
        } else {
            signup(result)
        }
    })

    return result;
}

function authPopupLogin(authPopup) {
    basicLogin(authPopup.username.input.value, authPopup.password.input.value)
        .then(response => {
            sessionStorage.setItem(jwtTokenStorageName, response.body.token)
            authPopup.hide()
            loginUser(response.body.user)
        })
        .catch(response => {
            if(response.status == 401) {
                authPopup.clearInput()
                authPopup.mainErr.innerText = response.body.message
            } else {
                throw response
            }
        }).catch(response => {
            console.error('Unrecognized response: ' + response)
        })
}

function basicLogin(username, password) {
    return request({ url: '/api/auth/login', method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: {
            username: username,
            password: password
        }
    })
}

function getToken() {
    return sessionStorage.getItem(jwtTokenStorageName)
}

function authenticatedRequest(params) {

    if(typeof params != 'object'){
        params = {}
    }
    if(typeof params.headers != 'object') {
        params.headers = {}
    }
    params.headers['Accept'] = 'application/json'
    params.headers['Content-Type'] = 'application/json'

    const token = getToken()
    if(token == null || token == undefined || token == ""){
        authPopup.show()
        return new Promise((resolve, reject) => resolve(null))
    }

    params.headers['Authorization'] = 'Bearer ' + getToken()

    return request(params)
        .catch(response => {
            if(response.status == 401) {
                if(response.body.code == 1000 || response.body.code == 1001) { // token is expired or invalid
                    authPopup.show()
                } else {
                    console.error('Unknown reason of unauthenticated request: ', response.body)
                }
                return null
            } else {
                return response
            }
        })
}

function tryLogin() {
    authenticatedRequest({url: '/api/user'})
        .then(response => {
            if(response != null && response != undefined){
                loginUser(response.body)
            }
        })
}

function loginUser(user) {
    headerUsername.innerText = user.username
    notAuthMessage.style.display = 'none'
    list.style.display = 'flex'
    loadList(user.list)
}
function logout() {
    sessionStorage.removeItem(jwtTokenStorageName)
    notAuthMessage.style.display = 'flex'
    list.style.display = 'none'
    list.innerHTML = ''
    headerUsername.innerText = ''
    authPopup.show()
}

function signup(authPopup) {
    console.log('Signing up is not implemented yet')
    // TODO: implement signing up
}
