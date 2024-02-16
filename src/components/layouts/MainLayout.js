import {Header} from "../snippets/header";
import {observer} from "mobx-react-lite";
import Store from "../../store/store";

export const MainLayout = observer(({children}) => {
    return (
        <>
            <Header/>
            {Store.isLoading === true ? <>Loading...</> : children}
        </>
    )
})