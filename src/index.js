import React, {createContext, useContext} from 'react';
import ReactDOM from 'react-dom/client';
import 'bootstrap/dist/css/bootstrap.min.css';
import {QueryClient, QueryClientProvider} from "react-query";
import {RouterProvider} from "react-router";
import {Router, router} from "./router/Router";
import Store from "./store/store";
import './styles/global.css'
import {BrowserRouter} from "react-router-dom";


const queryClient = new QueryClient()
const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
    <QueryClientProvider client={queryClient}>
        {/*Компонент для роутинга */}
        <BrowserRouter>
            <Router/>
        </BrowserRouter>
    </QueryClientProvider>
);