import $api from "../http/api";

export default class AuthService{
    static  async login(email,password){
        return $api.post('/auth/login', {email, password})
    }

    static  async logout(refreshToken){
        return $api.post('/auth/logout', {refresh_token:`${refreshToken}`})
    }
}