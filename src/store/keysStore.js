import {makeAutoObservable, runInAction} from "mobx";
import KeyService from "../services/KeyService";

class KeysStore {

    keys_status = undefined
    build = undefined
    room = undefined

    constructor() {
        makeAutoObservable(this)
    }


    setKeysFilter({keysStatus, build, room}) {
        this.keys_status = keysStatus
        this.build = build
        this.room = room
    }

    async getKeys() {
        try {
            const response = await KeyService.getKeys(this.keys_status, this.build, this.room)
            return response
        } catch (e) {
            console.log(e)
        }
    }

    async addKey(build, room) {
        try {
            await KeyService.addKey(build, room)
        } catch (e) {
            console.log(e)
        }
    }

    async deleteKey(keyId){
        try {
            await KeyService.deleteKey(keyId)
        }catch (e){
            console.log(e)
        }
    }
}

export default new KeysStore()