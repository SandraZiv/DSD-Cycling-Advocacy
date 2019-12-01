import React, {Component} from 'react';
import {NavLink} from "react-router-dom";
import {Navbar, Nav} from 'react-bootstrap';
import logo from '../images/sindikat-logo.png';
import './Navigation.css';

export class Navigation extends Component {
    render() {
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
                            <NavLink className="nav-link" to="/trips">Trips</NavLink>
                        </Nav.Item>
                        <Nav.Item>
                            <NavLink className="nav-link" to="/fms">Report Issue</NavLink>
                        </Nav.Item>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        )
    }
}