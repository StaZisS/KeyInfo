import {Login} from "../components/pages/login/Login";
import {Header} from "../components/snippets/Header";
import {Route, Routes} from "react-router";
import {MainLayout} from "../components/layouts/MainLayout";

export function Router() {
    return (
        <Routes>
            <Route path='/' element={<MainLayout children={<>Main</>}/>}/>
            <Route path='/login' element={<MainLayout children={<Login/>}/>}/>
            <Route path='/key' element={<MainLayout children={<>keys</>}/>}/>
            <Route path='/application' element={<MainLayout children={<>application</>}/>}/>
            <Route path='*' element={<MainLayout children={<>Not found</>}/>}/>
        </Routes>
    )
}