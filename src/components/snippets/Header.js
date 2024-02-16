import {
    Container,
    DropdownItem,
    Image,
    Nav,
    NavbarBrand,
    NavbarCollapse,
    NavbarToggle,
    NavDropdown,
    NavLink
} from "react-bootstrap";
import Store from "../../store/store";
import {Link, useNavigate} from "react-router-dom";

const {Navbar} = require("react-bootstrap");
import('../../styles/header.css')


export const Header = () => {

    const navigation = useNavigate()
    const handleLogout = async () => {
        try {
            await Store.logout()
            console.log('Выход. isAuth = ' + Store.isAuth)
            navigation('/login')
        } catch (e) {
            console.log(e)
        }
    }

    return (
        <div className='header'>
            <Navbar expand='lg' className='pt-3 pb-3 p-lg-0' style={{backgroundColor: '#0D1B34'}}>
                <Container fluid className='ms-5 me-5'>
                    <Link to={'/'} className='nav-link d-flex align-items-start'>
                        <Image src='../../icons/logo-icon.svg'></Image>
                        <NavbarBrand className='header-logo ms-2'>KeyInfo</NavbarBrand>
                    </Link>
                    <NavbarToggle className='bg-white' aria-controls='basic-navbar-nav'/>
                    <NavbarCollapse id='basic-navbar-nav'>
                        <Nav className='w-100'>
                            {Store.isAuth === false
                                ? <></>
                                :
                                <>
                                    <Link to={'/key'} className='nav-link link-hover p-lg-4'>Ключи</Link>
                                    <Link to={'/application'} className='nav-link link-hover p-lg-4'>Заявки</Link>
                                </>
                            }

                            {Store.isAuth === false ?
                                <Link to={'/login'} className='nav-link link-hover p-lg-4 ms-lg-auto'>Вход</Link> :
                                <Link className='nav-link link-hover p-lg-4 ms-lg-auto'
                                      onClick={handleLogout}>Выход</Link>}
                        </Nav>
                        <Nav>
                        </Nav>
                    </NavbarCollapse>
                </Container>
            </Navbar>
        </div>
    )
}