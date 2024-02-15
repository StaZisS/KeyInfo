import {makeAutoObservable} from "mobx";
import AuthService from "../services/AuthService";

 class Store{
    isAuth = false

    constructor() {
        makeAutoObservable(this)
    }

    setAuth(bool){
        this.isAuth = bool
    }

    async login(email,password){
        try{
            const response = await AuthService.login(email,password)
            localStorage.setItem('token',response.data.accessToken)
            localStorage.setItem('refreshToken',response.data.refreshToken)
            this.setAuth(true)
        }catch (e){
            console.log(e)
        }
    }

    async logout(){
        try{
            const response = await AuthService.logout()
            localStorage.removeItem('token')
            localStorage.removeItem('refreshToken')
            this.setAuth(false)
        }catch (e){
            console.log(e)
        }
    }
}

export default new Store()