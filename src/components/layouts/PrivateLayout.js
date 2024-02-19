import {useEffect} from "react";
import Store from "../../store/store";
import {Header} from "../snippets/Header";
import {Loading} from "../snippets/Loading";
import {useNavigate} from "react-router-dom";
import {useQuery} from "react-query";
import {Navigate} from "react-router";

export const PrivateLayout = ({children}) => {

    const navigate = useNavigate()

    const {data, isLoading, error} = useQuery(['checkAuth'], () => Store.checkAuth(), {
        refetchOnWindowFocus: false,
        keepPreviousData: false
    })

    if (isLoading){
        return (
            <Loading/>
        )
    }

    if (error){
        return(
            <Navigate to={'/login'}/>
        )
    }

    // useEffect(() => {
    //     const checkAuth = async () => {
    //         console.log('Проверка пользователя началась')
    //         await Store.checkAuth()
    //         console.log('Проверка пользователя окончилась = ' + Store.isAuth)
    //         if (Store.isAuth === false) {
    //             navigate('/login')
    //         }
    //     }
    //     checkAuth()
    // });

    return (
        <>
            <Header/>
            {children}
        </>
    )
}
