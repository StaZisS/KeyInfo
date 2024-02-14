import React from 'react';
import ReactDOM from 'react-dom/client';
import 'bootstrap/dist/css/bootstrap.min.css';
import {QueryClient, QueryClientProvider} from "react-query";
import {RouterProvider} from "react-router";
import {router} from "./router/Router";

const queryClient = new QueryClient()
const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
    <React.StrictMode>
        <QueryClientProvider client={queryClient}>
            {/*Компонент для роутинга */}
            <RouterProvider router={router}/>
        </QueryClientProvider>
    </React.StrictMode>
);