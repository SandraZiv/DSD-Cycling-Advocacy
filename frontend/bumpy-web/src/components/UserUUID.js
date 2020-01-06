import React, {useContext, useEffect} from 'react';
import {Button, Form} from "react-bootstrap";
import {ShortUuidContext} from "../Store";
import './UserUUID.css'

export const UserUUID = (props) => {
    const [shortUuid, setShortUuid] = useContext(ShortUuidContext);

    useEffect(() => {
        document.title = "Bumpy - Trips"
    });

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
            <Button className="btn padding-bottom-standard" type="submit" onClick={e => showLogoutAlert()}>
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

            let deviceUuid = e.target.deviceUUID.value;
            setShortUuid(deviceUuid);
            props.history.push(`/user/${deviceUuid}`);
        }}>
            <Form.Group controlId="deviceUUID">
                <Form.Label className="padding-bottom-standard" column="">
                    {formLabelText}
                </Form.Label>
                <Form.Control className="enterUUID col-md-2 mx-auto"
                              placeholder="Y4IKLY"
                              type="text" name="deviceUUID" required/>
                <Form.Text className="text-muted padding-bottom-standard">
                    User Identifier can be found in mobile app under Settings.
                </Form.Text>
                <Button className="btn" type="submit">Enter</Button>
            </Form.Group>
        </Form>
    )
};
