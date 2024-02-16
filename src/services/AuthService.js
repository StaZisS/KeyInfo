import $api from "../http/api";
import axios from "axios";

export default class AuthService {
    static async login(email, password) {
        return $api.post('/auth/login', {email, password})
    }

    static async logout(refreshToken) {
        return $api.post('/auth/logout', {refresh_token: refreshToken}, {headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}})
    }

    static async refreshAccessToken(refreshToken) {
        return $api.post('/auth/refresh', {refresh_token: refreshToken}, {headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}})
    }
}