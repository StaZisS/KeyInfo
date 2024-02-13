import {makeAutoObservable} from "mobx";
import AuthService from "../services/AuthService";

export default class Store{
    user = {}
    isAuth = false

    constructor() {
        makeAutoObservable(this)
    }

    setAuth(bool){
        this.isAuth = bool
    }

    setUser(user){
        this.user = user
    }

    async login(email,password){
        try{
            const response = AuthService.login(email,password)
            localStorage.setItem('token',response.data.accessToken)
            this.setAuth(true)
        }catch (e){
            console.log(e.response.data.message)
        }
    }

    async logout(){
        try{
            const response = AuthService.logout()
            localStorage.removeItem('token')
            this.setAuth(false)
        }catch (e){
            console.log(e.response.data.message)
        }
    }

}