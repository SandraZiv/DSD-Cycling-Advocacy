import React, {Component} from 'react';
import {Button} from "react-bootstrap";
import {UserModal} from "./UserModal";

export class PastTrips extends Component {

    constructor(props) {
        super(props);
        this.state = {
            trips: [],
            shouldShowModal: false
        };
    }

    // called when all components are rendered
    // componentDidMount() {
    //     this.getTrips()
    // }

    onModalClose(deviceUUID) {
        this.setState({shouldShowModal: false});
        this.getTrips(deviceUUID);
    }

    getTrips(deviceUUID) {
        fetch(`/v1/trip/getTripsByDeviceUUID?deviceUUID=${deviceUUID}`)
            .then(response => response.json())
            .then(json => {
                    const data = [
                        {"id": 123, "name": "Sandra"},
                        {"id": 223, "name": "Per"}
                    ];
                    console.log(json);
                    this.setState({trips: data})
                }
            );
    }

    render() {
        const {trips} = this.state;

        return (
            <div>
                <Button onClick={() => this.setState({shouldShowModal: true})}>Enter User Identifier</Button>
                <UserModal show={this.state.shouldShowModal} onHide={this.onModalClose.bind(this)}/>

                {trips.map(trip => <p key={trip.id}>{trip.id} - {trip.name}</p>)}
            </div>
        )
    }
}
