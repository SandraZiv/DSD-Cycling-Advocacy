import React, {useContext, useEffect} from 'react';
import {Button, Form} from "react-bootstrap";
import {UuidContext} from "../Store";
import './UserUUID.css'

export const UserUUID = (props) => {
    const [uuid, setUuid] = useContext(UuidContext);

    useEffect(() => {
        document.title = "Bumpy - Trips"
    });


    let formLabelText = '';
    if (uuid !== undefined && uuid !== '') {
        formLabelText =
        <div>
            <p>Current User Identifier is <b>{uuid}</b></p>
            <p>Change User by entering new Identifier</p>
        </div>;
    } else {
        formLabelText += 'Enter User Identifier to see past trips';
    }

    return (
        <Form onSubmit={(e) => {
            e.preventDefault();

            setUuid(e.target.deviceUUID.value);
            props.history.push('/user/' + e.target.deviceUUID.value)
        }}>
            <Form.Group controlId="deviceUUID">
                <Form.Label className="padding-bottom-standard" column="">
                    {formLabelText}
                </Form.Label>
                <Form.Control className="enterUUID col-md-3 mx-auto"
                              placeholder="1f577249-04f7-4d10-9e51-78bca31773e7"
                              type="text" name="deviceUUID" required/>
                <Form.Text className="text-muted padding-bottom-standard">
                    User Identifier can be found in mobile app under Settings.
                </Form.Text>
                <Button className="btn" type="submit">Enter</Button>
            </Form.Group>
        </Form>
    )
};
