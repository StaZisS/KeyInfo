import axios from "axios";


export const API_URL = 'http://147.45.76.239:8080/api/v1'

const $api = axios.create({
    baseURL: API_URL
})
// $api.interceptors.request.use((config) => {
//     const token = localStorage.getItem('token')
//     if (token) {
//         config.headers.Authorization = `Bearer ${token}`
//     }
//     return config
// })

$api.interceptors.response.use((config) => {
    console.log('Интерцептор не использован')
    return config
}, async (error) => {
    console.log('Интерцептор использован')
    const originalRequest = error.config
    if ((error.response.status === 401 || error.response.status === 403) && error.config && !error.config._isRetry) {
        originalRequest._isRetry = true
        try {
            const response = await axios.post(`${API_URL}/auth/token`, {refresh_token: localStorage.getItem('refreshToken')})
            localStorage.setItem('token', response.data.accessToken)
            localStorage.setItem('refreshToken', response.data.refreshToken)
            return $api.request(originalRequest)
        } catch (e) {
            console.log('Не авторизован')
        }

    }
    throw error;
})


export default $api