import {Header} from "../snippets/header";

export const MainLayout = ({children}) => {
    return (
        <>
            <Header/>
            {children}
        </>
    )
}