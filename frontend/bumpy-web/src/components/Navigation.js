import React, {useContext} from 'react';
import {NavLink} from "react-router-dom";
import {Navbar, Nav} from 'react-bootstrap';
import logo from '../images/sindikat-logo.png';
import {ShortUuidContext} from "../Store";
import './Navigation.css';

export const Navigation = () => {
    const [shortUuid] = useContext(ShortUuidContext);

    return (
        <Navbar expand="lg">
            <Navbar.Brand href="http://sindikatbiciklista.hr/">
                <img src={logo}
                     width="210"
                     height="65"
                     className="d-inline-block align-top"
                     alt="Bumpy logo"/>
            </Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav"/>
            <Navbar.Collapse id="basic-navbar-nav" className="justify-content-end">
                <Nav className="ml-auto">
                    <Nav.Item>
                        <NavLink className="nav-link" to="/" exact={true}>Home</NavLink>
                    </Nav.Item>
                    <Nav.Item>
                        <NavLink className="nav-link" to={`/user/${shortUuid}`}>Trips</NavLink>
                    </Nav.Item>
                    <Nav.Item>
                        <NavLink className="nav-link" to="/login">User</NavLink>
                    </Nav.Item>
                </Nav>
            </Navbar.Collapse>
        </Navbar>
    );
};
