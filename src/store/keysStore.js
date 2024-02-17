import {makeAutoObservable, runInAction} from "mobx";
import KeyService from "../services/KeyService";

class KeysStore {
    keys = []
    keysFilter = {
        key_status: undefined,
        build : undefined,
        room : undefined
    }

    constructor() {
        makeAutoObservable(this)
    }

    setKeys(keys){
        this.keys = keys
    }

    setKeysFilter(filterParams) {
        this.keysFilter = filterParams
    }

    async getKeys() {
        try {
            const response = await KeyService.getKeys(this.keysFilter.key_status, this.keysFilter.build, this.keysFilter.room)
            this.setKeys(response.data)
        } catch (e) {
            console.log(e)
        }
    }
}

export default new KeysStore()