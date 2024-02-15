import {Login} from "../components/pages/login/Login";
import {Header} from "../components/snippets/header";
import {Route, Routes} from "react-router";

export function Router() {
    return (
        <Routes>
            <Route path='/' element={<Header/>}/>
            <Route path='login' element={
                <>
                    <Header/>
                    <Login/>
                </>
            }/>
        </Routes>
    )
}