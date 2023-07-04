
import axios from 'axios'
import login_data from "../libs/loginData";
import md5 from 'js-md5';
import router from "../router";
import qs from 'qs'

axios.defaults.timeout = 1000000
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded'

// // Request interceptor
axios.interceptors.request.use(
    config => {
        const token = login_data.userId
        token && (config.headers['satoken'] = token)
        return config
    },
    error => {
        return Promise.error(error)
    })

// Response interceptor
axios.interceptors.response.use(
    response => {
        if (response.status === 200) {
            // console.log(response)
            if (response.data.code != 200) {
                // token Invalid jump login page
                if (response.data.code == 3000 || response.data.code == 4000) {
                    // Message.error('Token is invalid, please login again.');
                    router.replace({
                        path: '/login',
                    })
                }
            }
            return Promise.resolve(response)
        } else {
            return Promise.reject(response)
        }
    },
    // The server status code is not 200.
    error => {
        console.log(error)
        if (error.response.data.code == 3000 || error.response.data.code == 4000) {
            // Message.error('Token is invalid, please login again.');
            router.replace({
                path: '/login',
            })
        }
        if (error.response.status) {
            switch (error.response.status) {
                case 401:
                    // Message.error('請登錄');
                    login_data.out();
                    router.replace({
                        path: '/login',
                    })
                    break
                case 403:
                    // Message.error('Token is invalid, please login again.');
                    router.replace({
                        path: '/login',
                    })
                    // Clear token
                    break
                case 404:
                    // Message.error("404, the network request does not exist.");
                    break
                default:
                // Message.error(error.response.data.message);
            }
            return Promise.reject(error.response)
        }
    }
)

/**
 * @param {String} url [Requested url address]
 * @param {Object} params [Parameters carried in the request]
 */
export function get(url, params, loding) {
    return new Promise((resolve, reject) => {
        axios.get(url, {
            params: params
        }).then(res => {
            resolve(res.data)
        }).catch(err => {
            reject(err.data)
        })
    })
}
/**
 * Post method, corresponding to post request.
 * @param {String} url [Requested url address]
 * @param {Object} params [Parameters carried in the request]
 */
export function post(url, params, type) {
    return new Promise((resolve, reject) => {
        let data
        if (!type) {
            data = qs.stringify(params, { arrayFormat: 'repeat' })
        } else {
            data = params, { arrayFormat: 'repeat' }
        }
        axios.post(url, data)
            .then(res => {
                resolve(res.data)
            })
            .catch(err => {
                reject(err.data)
            })
    })
}
/**
 * @param url Interface routing
 * @returns {AxiosPromise}
 */
export function put(url, params, loding) {
    return new Promise((resolve, reject) => {
        axios.put(url, params)
            .then(res => {
                resolve(res.data)
            })
            .catch(err => {
                reject(err.data)
            })
    })
}

/**
 * delete
 * @param url Interface routing
 * @returns {AxiosPromise}
 */
export function del(url, params, loding) {
    return new Promise((resolve, reject) => {
        axios.delete(url, params)
            .then(res => {
                resolve(res.data)
            })
            .catch(err => {
                reject(err.data)
            })
    })
}