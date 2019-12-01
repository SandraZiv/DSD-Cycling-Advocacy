import React, {Component} from 'react';
import {Button, Table} from "react-bootstrap";
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
        // fetch(`/v1/trip/getTripsByDeviceUUID?deviceUUID=${deviceUUID}`)
        //     .then(response => response.json())
        //     .then(json => {
        const data = [
            {"tripUUID": 123, "startTS": "Sandra", "endTS": "end"},
            {"tripUUID": 223, "startTS": "Per", "endTS": "end"}
        ];
        // console.log(json);
        this.setState({trips: data})
        // }
        // );
    }

    render() {
        const {trips} = this.state;
        let tripTable = "";
        if (trips.length !== 0) {
            tripTable =
                <Table striped bordered hover responsive>
                    <thead>
                    <tr>
                        <th>Start time</th>
                        <th>End time</th>
                        <th>Duration(h)</th>
                        <th>Distance(km)</th>
                        <th>Accumulated Vibration</th>
                    </tr>
                    </thead>
                    <tbody>
                    {trips.map(trip =>
                        <tr key={trip.tripUUID}>
                            <td>{trip.startTS}</td>
                            <td>{trip.endTS}</td>
                            <td>2</td>
                            <td>25</td>
                            <td>1225</td>
                        </tr>
                    )}
                    </tbody>
                </Table>
        }

        return (
            <div>
                <Button onClick={() => this.setState({shouldShowModal: true})}>Enter User Identifier</Button>
                <UserModal show={this.state.shouldShowModal} onHide={this.onModalClose.bind(this)}/>

                {tripTable}
            </div>
        )
    }
}
