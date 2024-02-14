import { useState } from "react";
import { Button, Col, Form } from "react-bootstrap"

export const Login = () => {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');

    const handleLoginChange = (e) => {
        setLogin(e.target.value)
    }
    
    const handlePasswordChange = (e) => {
        setPassword(e.target.value)
    }

    const handleLogIn=(e)=>{
        e.preventDefault();
        console.log(login, password);
    }
    return (
            <Col sm md={10} lg={8} className="mx-auto shadow mt-5 " >
            <Form className="p-5" onSubmit={handleLogIn}>
            <h1>Вход</h1>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                    <Form.Label>Email</Form.Label>
                    <Form.Control 
                        type="email" 
                        placeholder="name@example.com" 
                        onChange={handleLoginChange}
                        value={login}
                        required
                        />
                </Form.Group>

                <Form.Group className="mb-3" controlId="formBasicPassword">
                    <Form.Label>Пароль</Form.Label>
                    <Form.Control
                        type="password" 
                        onChange={handlePasswordChange}
                        value={password}
                        required
                        />
                </Form.Group>
                <Button variant="primary" type="submit" className="w-100 mt-3" >
                    Войти
                </Button>
                
            </Form>

        </Col>
        

    )
}