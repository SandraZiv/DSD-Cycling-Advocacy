import React, {useContext, useEffect} from 'react';
import {Button, Form} from "react-bootstrap";
import {UuidContext} from "../Store";
import {withRouter} from "react-router-dom";
import './UserUUID.css'

export const UserUUID = (props) => {
    const [, setUuid] = useContext(UuidContext);

    useEffect(() => {
        document.title = "Bumpy - Trips"
    });

    return (
        <Form onSubmit={(e) => {
            e.preventDefault();

            setUuid(e.target.deviceUUID.value);
            props.history.push('/trips/' + e.target.deviceUUID.value)
        }}>
            <Form.Group controlId="deviceUUID">
                <Form.Label className="padding-bottom-standard" column="">Enter User Identifier</Form.Label>
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

export default withRouter(UserUUID)
