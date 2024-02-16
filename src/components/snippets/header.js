import {CardImg, Container, Image, Nav, NavbarBrand, NavbarCollapse, NavbarToggle, NavLink} from "react-bootstrap";

const {Navbar} = require("react-bootstrap");
import('../../styles/header.css')


export function Header() {
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
                                <NavLink className='link-hover p-lg-4' href='/application'>Пользователи</NavLink>
                                <NavLink className='link-hover p-lg-4 ms-lg-auto' href='/login'>Вход</NavLink>
                            </Nav>
                            <Nav>
                            </Nav>
                    </NavbarCollapse>
                </Container>
            </Navbar>
        </div>
    )
}