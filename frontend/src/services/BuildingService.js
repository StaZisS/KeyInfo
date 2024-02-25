import $api from "../http/api";

export default class BuildingService{
    static async getAccomodations(){
        return $api.get('/audiences/accommodations')
    }
}
