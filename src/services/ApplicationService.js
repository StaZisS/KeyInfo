import $api from "../http/api";

export default class ApplicationService {

    static async getApplications({status = 'IN_PROCESS', start, end, buildId, roomId}) {
        return $api.get(`deaneries/applications`, {
            params: {
                status: status,
                start: start,
                end: end,
                build_id: buildId,
                room_id: roomId,
            },
            headers: {
                Authorization: `Bearer ${localStorage.getItem('token')}`
            }
        })
    }

    static async acceptApplication(id) {
        return $api.post(`/deaneries/application/${id}/accept`, {}, {headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}})
    }
    static async declineApplication(id) {
        return $api.post(`/deaneries/application/${id}/decline`, {}, {headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}})
    }

}