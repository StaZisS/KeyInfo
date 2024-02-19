import {useEffect} from "react";
import Store from "../../store/store";
import {Header} from "../snippets/Header";
import {Loading} from "../snippets/Loading";
import {useNavigate} from "react-router-dom";

export const PrivateLayout = ({children}) => {

    const navigate = useNavigate()

    //Необходимо для тех, кто переходит по ссылке
    useEffect(() => {
        const checkAuth = async () => {
            console.log('Проверка пользователя началась')
            await Store.checkAuth()
            console.log('Проверка пользователя окончилась = ' + Store.isAuth)
            if (Store.isAuth === false){
                navigate('/login')
            }
        }
        checkAuth()
    });

    return (
        <>
            <Header/>
            {Store.isLoading === true ? <Loading/> : children}
        </>
    )
}
