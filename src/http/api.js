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
    return config
}, async (error) => {
    const originalRequest = error.config
    if ((error.response.status === 401 || error.response.status === 403) && error.config && !error.config._isRetry) {
        console.log('Интерцептор использован')
        originalRequest._isRetry = true
        try {
            const response = await axios.post(`${API_URL}/auth/refresh`, {refresh_token: localStorage.getItem('refreshToken')})
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