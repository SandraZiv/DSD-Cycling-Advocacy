import React, {Component} from 'react';
import {Button} from "react-bootstrap";
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table'
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
            {"tripUUID": 123, "startTS": "Sandra", "endTS": "end", "distance": 12},
            {"tripUUID": 223, "startTS": "Per", "endTS": "end", "distance": 25}
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
                <BootstrapTable ref='table' data={trips} pagination>
                    <TableHeaderColumn dataField='tripUUID' isKey={true} dataSort={true}>Trip ID</TableHeaderColumn>
                    <TableHeaderColumn dataField='startTS' dataSort={true}>Start time</TableHeaderColumn>
                    <TableHeaderColumn dataField='endTS' dataSort={true}>End time</TableHeaderColumn>
                    <TableHeaderColumn dataField='distance' dataSort={true}>Distance</TableHeaderColumn>
                </BootstrapTable>
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
