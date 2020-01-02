import React, {useContext} from 'react';
import {NavLink} from "react-router-dom";
import {Navbar, Nav} from 'react-bootstrap';
import logo from '../images/sindikat-logo.png';
import {UuidContext} from "../Store";
import './Navigation.css';

export const Navigation = () => {
    const [uuid] = useContext(UuidContext);
    let tripPath = '/user';
    if (uuid !== undefined && uuid !== '') {
        tripPath = '/trips/' + uuid;
    }

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
                        <NavLink className="nav-link" to={tripPath}>Trips</NavLink>
                    </Nav.Item>
                    <Nav.Item>
                        <NavLink className="nav-link" to="/fms">Report Issue</NavLink>
                    </Nav.Item>
                </Nav>
            </Navbar.Collapse>
        </Navbar>
    );
};
