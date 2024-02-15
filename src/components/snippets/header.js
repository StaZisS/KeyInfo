import {CardImg, Container, Image, Nav, NavbarBrand, NavbarCollapse, NavbarToggle, NavLink} from "react-bootstrap";
import Store from "../../store/store";
import {useNavigate} from "react-router-dom";

const {Navbar} = require("react-bootstrap");
import('../../styles/header.css')


export function Header() {

    const navigate = useNavigate()
    const handleLogout = async () => {
        try {
            await Store.logout()
            console.log(Store.isAuth)
            navigate('/login')
        }catch (e){
            console.log(e)
        }
    }

    return (
        <div className='header'>
            <Navbar expand='lg' className='p-0' style={{backgroundColor: '#0D1B34'}}>
                <Container fluid className='ms-5 me-5'>
                    <NavLink href='/'>
                        <Image src='../../icons/logo-icon.svg'></Image>
                        <NavbarBrand className='header-logo ms-2'>KeyInfo</NavbarBrand>
                    </NavLink>
                    <NavbarToggle aria-controls='basic-navbar-nav'/>
                    <NavbarCollapse id='basic-navbar-nav'>
                        <Nav className='w-100'>
                            <NavLink className='link-hover p-lg-4' href='/key'>Ключи</NavLink>
                            <NavLink className='link-hover p-lg-4' href='/application'>Заявки</NavLink>
                            {Store.isAuth === false ?
                                <NavLink className='link-hover p-lg-4 ms-lg-auto'  href='/login'>Вход</NavLink> :
                                <NavLink className='link-hover p-lg-4 ms-lg-auto'
                                         onClick={handleLogout}>Выход</NavLink>}
                        </Nav>
                        <Nav>
                        </Nav>
                    </NavbarCollapse>
                </Container>
            </Navbar>
        </div>
    )
}