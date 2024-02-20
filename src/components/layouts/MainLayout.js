import {Header} from "../snippets/Header";
import {observer} from "mobx-react-lite";
import Store from "../../store/store";
import {Loading} from "../snippets/Loading";
import {Navigate} from "react-router";

export const MainLayout = observer(({children}) => {

    if (Store.isAuth === true){
        return (
            <Navigate to={'/'}/>
        )
    }

    return (
        <>
            <Header/>
            {Store.isLoading === true ? <Loading/> : children}
        </>
    )
})