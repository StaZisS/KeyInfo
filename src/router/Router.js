import { Application } from "../components/pages/applicationList/applicationList";
import { Keys } from "../components/pages/keysList/keysList";
import { Login } from "../components/pages/login/Login";
import { People } from "../components/pages/peopleList/peopleList";
import {Header} from "../components/snippets/header";

const {createBrowserRouter} = require("react-router-dom");

export const router = createBrowserRouter([
    {
        path: '/',
        element: <><Header/></>
    },
    {
        path: '/login',
        element: <><Header/><Login/></>
    },
    {
        path: '/key',
        element: <><Header/><Application/></>
    },
    {
        path: '/key/application',
        element: <><Header/></>
    },
    {
        path: '/users',
        element: <><Header/><People/></>
    }
])