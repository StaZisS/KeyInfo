import React, {createContext, useContext} from 'react';
import ReactDOM from 'react-dom/client';
import 'bootstrap/dist/css/bootstrap.min.css';
import {QueryClient, QueryClientProvider} from "react-query";
import {RouterProvider} from "react-router";
import {router} from "./router/Router";
import Store from "./store/store";
import './styles/global.css'

const store = new Store()

export const Context = createContext({
    store,
})

const queryClient = new QueryClient()
const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
    <Context.Provider value={{store}}>
        <QueryClientProvider client={queryClient}>
            {/*Компонент для роутинга */}
            <RouterProvider router={router}/>
        </QueryClientProvider>
    </Context.Provider>
);