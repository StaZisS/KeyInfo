import $api from "../http/api";

export default class AuthService{
    static  async login(email,password){
        return $api.post('auth/login', {email, password})
    }

    static  async logout(){
        return $api.post('auth/logout')
    }
}