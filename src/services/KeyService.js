import $api from "../http/api";

export default class KeyService {
    static async getKeys(keyStatus, build, room) {
        return $api.get('/deaneries/keys', {
            params: {key_status: keyStatus, build: build, room: room},
            headers: {Authorization: `Bearer ${localStorage.getItem('token')}`, 'Content-Type': 'Application/json'}
        })
    }

}