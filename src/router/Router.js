import {Header} from "../components/snippets/header";

const {createBrowserRouter} = require("react-router-dom");

export const router = createBrowserRouter([
    {
        path: '/',
        element: <><Header></Header></>
    }
    //TODO добавление путей здесь
])