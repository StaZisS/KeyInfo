import {useState} from "react";
import {Button, Card, CardBody, CardTitle, Col, Container, Form} from "react-bootstrap"
import Store from "../../../store/store";
import {useNavigate} from "react-router-dom";
import {useQuery} from "react-query";

export const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate()

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            await Store.login(email, password)
            console.log('Вход. isAuth = ' + Store.isAuth)
            navigate('/')
        } catch (e) {
            console.log(e)
        }
    }

    return (
        <Container className="mt-5 d-flex justify-content-center">
            <Card className='shadow border-0 col-10 col-md-8'>
                <CardBody>
                    <Form className="p-5" onSubmit={handleLogin}>
                        <h1>Вход</h1>
                        <Form.Group className="mb-3" controlId="formBasicEmail">
                            <Form.Label>Email</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="name@example.com"
                                onChange={(e) => {
                                    setEmail(e.target.value)
                                }}
                                value={email}
                                required
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="formBasicPassword">
                            <Form.Label>Пароль</Form.Label>
                            <Form.Control
                                type="password"
                                onChange={(e) => {
                                    setPassword(e.target.value)
                                }}
                                value={password}
                                required
                            />
                        </Form.Group>
                        <Button variant="primary" type="submit" className="w-100 mt-3">
                            Войти
                        </Button>

                    </Form>
                </CardBody>
            </Card>
        </Container>


    )
}