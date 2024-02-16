import {makeAutoObservable} from "mobx";
import AuthService from "../services/AuthService";
import axios from "axios";

class Store {
    isAuth = false
    isLoading = false

    constructor() {
        console.log(this)
        makeAutoObservable(this)
    }

    setAuth(bool) {
        this.isAuth = bool
    }

    setLoading(bool) {
        this.isLoading = bool
        console.log('Loading = ' + bool)
    }

    async login(email, password) {
        this.setLoading(true)
        try {
            const response = await AuthService.login(email, password)
            console.log(response.data)
            localStorage.setItem('token', response.data.accessToken)
            localStorage.setItem('refreshToken', response.data.refreshToken)
            this.setAuth(true)
        } catch (e) {
            console.log(e)
        }
        this.setLoading(false)
    }

    async logout() {
        this.setLoading(true)
        try {
            const response = await AuthService.logout(localStorage.getItem('refreshToken'))
            localStorage.removeItem('token')
            localStorage.removeItem('refreshToken')
            this.setAuth(false)
        } catch (e) {
            console.log(e)
        }
        this.setLoading(false)
    }

    async checkAuth() {
        try {
            this.setLoading(true)
            const response = await axios.post('http://147.45.76.239:8080/api/v1/auth/token', {refresh_token: localStorage.getItem('refreshToken')}, {headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}})
            localStorage.setItem('token', response.data.accessToken)
            localStorage.setItem('refreshToken', response.data.refreshToken)
            this.setAuth(true)
        } catch (e) {
            this.setAuth(false)
        }
        this.setLoading(false)
        console.log(`Check auth - ${this.isAuth}`)
    }
}

export default new Store()