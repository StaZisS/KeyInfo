import { Route, Router } from "react-router"
import { Header } from "./snippets/header"
import { Login } from "./pages/login/Login"
import { useState } from "react"

export function App(){
    const [isLoggedIn, setIsLoggedIn]=useState(false);
    return(
        <Router>
            <div>
                <Header/>
                <main>
                    <switch>
                        <Route exact path="/" />
                        <Route exact path="/login" 
                            render={()=> <Login/>}
                        />

                    </switch>
                </main>
            </div>
        </Router>
    )
}