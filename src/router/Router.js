import {Login} from "../components/pages/login/Login";
import {Header} from "../components/snippets/Header";
import {Route, Routes} from "react-router";
import {MainLayout} from "../components/layouts/MainLayout";
import {Keys} from "../components/pages/keys/Keys";
import {PrivateLayout} from "../components/layouts/PrivateLayout";

export function Router() {
    return (
        <Routes>
            <Route path='/' element={<PrivateLayout children={<>Main</>}/>}/>
            <Route path='/login' element={<MainLayout children={<Login/>}/>}/>
            <Route path='/key/:key_status?/:build?/:room?' element={<PrivateLayout children={<Keys/>}/>}/>
            <Route path='/application' element={<PrivateLayout children={<>application</>}/>}/>
            <Route path='*' element={<MainLayout children={<>Not found</>}/>}/>
        </Routes>
    )
}