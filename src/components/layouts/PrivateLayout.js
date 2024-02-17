import {useEffect} from "react";
import Store from "../../store/store";
import {Header} from "../snippets/Header";
import {Loading} from "../snippets/Loading";
import {useNavigate} from "react-router-dom";

export const PrivateLayout = ({children}) => {

    const navigate = useNavigate()

    useEffect(() => {
        const checkAuth = async () => {
            await Store.checkAuth()
            if (Store.isAuth === false){
                navigate('/login')
            }
        }
        checkAuth()
    }, []);

    return (
        <>
            <Header/>
            {Store.isLoading === true ? <Loading/> : children}
        </>
    )
}
