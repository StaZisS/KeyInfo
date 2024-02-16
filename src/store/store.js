import {makeAutoObservable} from "mobx";
import AuthService from "../services/AuthService";

class Store {
    isAuth = false
    isLoading = false

    constructor() {
        makeAutoObservable(this)
    }

    setAuth(bool) {
        this.isAuth = bool
    }

    setLoading(bool){
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
}

export default new Store()