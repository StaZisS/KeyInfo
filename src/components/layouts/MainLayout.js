import {Header} from "../snippets/Header";
import {observer} from "mobx-react-lite";
import Store from "../../store/store";
import {Loading} from "../snippets/Loading";
import {useEffect} from "react";

export const MainLayout = observer(({children}) => {

    useEffect(() => {
        const checkAuth = async () =>{
            await Store.checkAuth()
        }
        checkAuth()
    }, []);

    return (
        <>
            <Header/>
            {Store.isLoading === true ? <Loading/> : children}
        </>
    )
})