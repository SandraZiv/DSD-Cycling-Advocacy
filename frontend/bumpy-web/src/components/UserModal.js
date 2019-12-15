import React, {Component} from 'react';
import {Modal, Button, Form} from "react-bootstrap";

export class UserModal extends Component {
    constructor(props) {
        super(props);
    }

    handleSubmit(event) {
        event.preventDefault();
        this.props.onHide(event.target.deviceUUID.value);
    }

    render() {
        return (
            <Modal
                {...this.props}
                size="md"
                aria-labelledby="contained-modal-title-vcenter"
                centered
            >
                <Modal.Header closeButton>
                    <Modal.Title id="contained-modal-title-vcenter">
                        View past trips
                    </Modal.Title>
                </Modal.Header>
                <Form onSubmit={this.handleSubmit.bind(this)}>
                    <Modal.Body>
                        <Form.Group controlId="deviceUUID">
                            <Form.Label column="">User Identifier</Form.Label>
                            <Form.Control type="text" name="deviceUUID" placeholder="dfsdfg6523-fsdf52s-56s2a"
                                          required/>
                            <Form.Text className="text-muted">
                                User Identifier can be found in mobile app under Settings.
                            </Form.Text>
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Form.Group>
                            <Button bsClass="btn" type="submit">Enter</Button>
                        </Form.Group>
                    </Modal.Footer>
                </Form>
            </Modal>
        )
    }
}