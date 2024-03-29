import {Login} from "../components/pages/login/Login";
import {Header} from "../components/snippets/Header";
import {Navigate, Route, Routes} from "react-router";
import {MainLayout} from "../components/layouts/MainLayout";
import {PrivateLayout} from "../components/layouts/PrivateLayout";
import {KeysList} from "../components/pages/keysList/KeysList";
import {PeopleList} from "../components/pages/peopleList/PeopleList";
import {ApplicationList} from "../components/pages/applicationList/applicationList";
import {BuildingPage} from "../components/pages/buildings/BuildingPage";

export function Router() {
    return (
        <Routes>
            <Route path='/' element={<PrivateLayout children={<KeysList/>}/>}/>
            <Route path='/login' element={<MainLayout children={<Login/>}/>}/>
            <Route path='/key' element={<PrivateLayout children={<KeysList/>}/>}/>
            <Route path='/application' element={<PrivateLayout children={<ApplicationList/>}/>}/>
            <Route path='/people' element={<PrivateLayout children={<PeopleList/>}/>}/>
            <Route path='/buildings' element={<PrivateLayout children={<BuildingPage/>}/>}/>
            <Route path='*' element={<MainLayout children={<>Not found</>}/>}/>
        </Routes>
    )
}