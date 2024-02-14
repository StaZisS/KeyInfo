import {Container, Nav, NavbarBrand, NavbarCollapse, NavbarToggle, NavLink} from "react-bootstrap";

const {Navbar} = require("react-bootstrap");

export function Header() {
    return (
        <Navbar expand='lg' className='bg-body-tertiary shadow'>
            <Container fluid className='ms-5 me-5'>
                <NavbarBrand href='#'>KeyInfo</NavbarBrand>
                <NavbarToggle aria-controls='basic-navbar-nav'/>
                <NavbarCollapse id='basic-navbar-nav'>
                    <Nav className='me-auto'>
                        <NavLink href='/key'>Ключи</NavLink>
                        <NavLink href='/application'>Заявки</NavLink>
                    </Nav>
                    <Nav className='justify-content-end'>
                        <NavLink href='/login'>Вход</NavLink>
                    </Nav>
                </NavbarCollapse>
            </Container>
        </Navbar>
    )
}