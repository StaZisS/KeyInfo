import {Header} from "../snippets/Header";
import {observer} from "mobx-react-lite";
import Store from "../../store/store";
import {Loading} from "../snippets/Loading";

export const MainLayout = observer(({children}) => {
    return (
        <>
            <Header/>
            {Store.isLoading === true ? <Loading/> : children}
        </>
    )
})