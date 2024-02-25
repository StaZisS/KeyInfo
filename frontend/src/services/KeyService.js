import $api from "../http/api";

export default class KeyService {
    static async getKeys(keyStatus, build, room, isPrivate) {
        return $api.get('/deaneries/keys', {
            params: {key_status: keyStatus, build: build, room: room, is_private: isPrivate},
            headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}
        })
    }

    static async addKey(build, room) {
        return $api.post('/deaneries/keys', {build : build, room : room}, {headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}})
    }

    static async deleteKey(keyId) {
        return $api.delete(`/deaneries/keys/${keyId}`, {headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}})
    }

    static async hideKey(keyId, status) {
        return $api.patch(`/deaneries/keys/replacing`, {keyId: keyId, isPrivate: status}, {headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}})
    }

    static async giveKey(id,keyHolderId){
        return $api.patch(`/deaneries/keys/giving/${id}`, {}, {params: {keyHolderId},headers:{Authorization: `Bearer ${localStorage.getItem('token')}`}})
    }

    static async takeKey(id,keyHolderId){
        return $api.patch(`/deaneries/keys/acceptance/${id}`, {}, {params: {keyHolderId},headers:{Authorization: `Bearer ${localStorage.getItem('token')}`}})
    }

}