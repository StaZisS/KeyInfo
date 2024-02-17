import {makeAutoObservable, runInAction} from "mobx";
import KeyService from "../services/KeyService";

class KeysStore {
    keys = []

    keys_status = undefined
    build = undefined
    room = undefined

    constructor() {
        makeAutoObservable(this)
    }


    setKeysFilter({keysStatus,build,room}) {
        this.keys_status = keysStatus
        this.build = build
        this.room = room
        console.log(this)
    }

    async getKeys() {
        try {
            const response = await KeyService.getKeys(this.keys_status,this.build,this.room)
            runInAction(() =>{
                this.keys = response.data
            })
            return response
        } catch (e) {
            console.log(e)
        }
    }
}

export default new KeysStore()