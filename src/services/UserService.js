import $api from "../http/api";

export default class UserService {

    static async getUsers(name, email) {
        return $api.get('/users', {headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}})
    }

    static async addRole(userId, clientRoles) {
        return $api.patch(`/roles/${userId}/status`, {clientRoles}, {headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}})
    }
}