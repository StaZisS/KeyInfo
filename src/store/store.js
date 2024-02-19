import {makeAutoObservable, runInAction} from "mobx";
import AuthService from "../services/AuthService";
import axios from "axios";
import KeyService from "../services/KeyService";

class Store {
    isAuth = false
    isLoading = false

    constructor() {
        makeAutoObservable(this)
    }

    setAuth(bool) {
        this.isAuth = bool
    }

    setLoading(bool) {
        this.isLoading = bool
    }

    async login(email, password) {
        this.setLoading(true)
        try {
            const response = await AuthService.login(email, password)
            runInAction(() => {
                localStorage.setItem('token', response.data.accessToken)
                localStorage.setItem('refreshToken', response.data.refreshToken)
                this.setAuth(true)
            })
        } catch (e) {
            console.log(e)
        }
        finally {
            this.setLoading(false)
        }
    }

    async logout() {
        this.setLoading(true)
        try {
            const response = await AuthService.logout(localStorage.getItem('refreshToken'))
            runInAction(() => {
                localStorage.removeItem('refreshToken')
                localStorage.removeItem('token')
                this.setAuth(false)
            })

        } catch (e) {
            console.log(e)
        }
        finally {
            this.setLoading(false)
        }
    }

    async checkAuth() {
        try {
            this.setLoading(true)
            const response = await AuthService.refreshAccessToken(localStorage.getItem('refreshToken'))
            runInAction(() => {
                localStorage.setItem('token', response.data.accessToken)
                localStorage.setItem('refreshToken', response.data.refreshToken)
                this.setAuth(true)
            })
        } catch (e) {
            console.log(e)
        }
        finally {
            this.setLoading(false)
        }
    }
}

export default new Store()