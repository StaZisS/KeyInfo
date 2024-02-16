import {useEffect} from "react";
import {useParams} from "react-router";
import {useQuery} from "react-query";
import KeysStore from "../../../store/keysStore";
import {Loading} from "../../snippets/Loading";
import KeyService from "../../../services/KeyService";
import keysStore from "../../../store/keysStore";

export const Keys = () => {
    const {key_status, build, room} = useParams()

    const {data, isLoading, error} = useQuery('dataKeys', () => {
        KeysStore.getKeys(key_status, build, room)
    })

    if (isLoading) {
        return (
            <Loading/>
        )
    }

    if (error) {
        console.log('ОШибка фетча ключей')
        return (
            <>
                Error
            </>
        )
    }

    // useEffect(() => {
    //     const fetchKeys = async () =>{
    //         await KeysStore.getKeys(key_status,build,room)
    //     }
    //     fetchKeys()
    // }, []);

    return (
        <>
            Keys
        </>
    )

}