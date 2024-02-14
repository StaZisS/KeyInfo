import { Login } from "../components/pages/login/Login";
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
        element: <><Header/></>
    },
    {
        path: '/application',
        element: <><Header/></>
    }
])