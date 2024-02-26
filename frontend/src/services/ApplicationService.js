import $api from "../http/api";

export default class ApplicationService {

    static async getApplications({status = 'IN_PROCESS', start, end, buildId, roomId}) {
        if (start === undefined){
            start = new Date()
            start.setUTCHours(start.getUTCHours() + 7)
            start = start.toISOString()
        }
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

    static async getAcceptedApplications({status = 'ACCEPTED', buildId, roomId}) {
        let currentDate = new Date()
        currentDate.setUTCHours(currentDate.getUTCHours() + 7)
        return $api.get(`deaneries/applications`, {
            params: {
                status: status,
                start: currentDate.toISOString(),
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