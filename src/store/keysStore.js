import {makeAutoObservable, runInAction} from "mobx";
import KeyService from "../services/KeyService";

class KeysStore {

    keys_status = undefined
    is_private = undefined
    build = undefined
    room = undefined

    constructor() {
        makeAutoObservable(this)
    }


    setKeyStatus(status){
        this.keys_status = status
    }

    setBuild(build){
        this.build = build
    }

    setRoom(room){
        this.room = room
    }

    setIsPrivate(bool){
        this.is_private = bool
    }

    async getKeys() {
        try {
            const response = await KeyService.getKeys(this.keys_status, this.build, this.room, this.is_private)
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

    async hideKey(keyId,status){
        try {
            await KeyService.hideKey(keyId, status)
        }catch (e){
            console.log(e)
        }
    }
}

export default new KeysStore()