import {makeAutoObservable, runInAction} from "mobx";
import KeyService from "../services/KeyService";

class KeysStore {
    keys = []

    constructor() {
        makeAutoObservable(this)
    }

    setKeys(keys){
        this.keys = keys
    }

    async getKeys(keyStatus, build, room) {
        try {
            const response = await KeyService.getKeys(keyStatus, build, room)
            this.setKeys(response.data)
        } catch (e) {
            console.log(e.response.data)
        }
    }
}

export default new KeysStore()