import React, {useContext, useEffect} from 'react';
import {Button, Form} from "react-bootstrap";
import {ShortUuidContext} from "../Store";
import './UserUUID.css'

export const UserUUID = (props) => {
    const [shortUuid, setShortUuid] = useContext(ShortUuidContext);

    useEffect(() => {
        document.title = "Bumpy - Trips"
    });

    const validateUUID = (uuid) => {
        try {
            let uuidUpperCase = uuid.toUpperCase();
            fetch(`/v1/device/getLongDeviceUUID?shortDeviceUUID=${uuidUpperCase}`)
                .then(response => {
                    if (response.ok) {
                        setShortUuid(uuidUpperCase);
                        props.history.push(`/user/${uuidUpperCase}`);
                    } else {
                        alert("UUID not valid")
                    }
                })
        } catch (e) {
        }
    };

    const showLogoutAlert = () => {
        if (window.confirm('Are you sure you wish to log out?')) {
            setShortUuid('');
        }
    };


    let formLabelText = '';
    if (shortUuid !== undefined && shortUuid !== '') {
        formLabelText =
        <div>
            <p>Current User Identifier is <b>{shortUuid}</b></p>
            <Button className="btn padding-bottom-standard" onClick={e => showLogoutAlert()}>
                Log out
            </Button>
            <p>Change User by entering new Identifier</p>
        </div>;
    } else {
        formLabelText += 'Enter User Identifier to see past trips';
    }

    return (
        <Form onSubmit={(e) => {
            e.preventDefault();
            validateUUID(e.target.deviceUUID.value)
        }}>
            <Form.Group controlId="deviceUUID">
                <Form.Label className="padding-bottom-standard" column="">
                    {formLabelText}
                </Form.Label>
                <Form.Control className="enterUUID col-md-2 mx-auto"
                              placeholder="Identifier"
                              type="text" name="deviceUUID" required/>
                <Form.Text className="text-muted padding-bottom-standard">
                    User Identifier can be found in mobile app under Settings.
                </Form.Text>
                <Button className="btn" type="submit">Enter</Button>
            </Form.Group>
        </Form>
    )
};
