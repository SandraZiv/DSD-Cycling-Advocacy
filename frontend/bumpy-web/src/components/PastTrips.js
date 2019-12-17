import React, {Component} from 'react';
import {Button} from "react-bootstrap";
import BootstrapTable from 'react-bootstrap-table-next'
import paginationFactory from 'react-bootstrap-table2-paginator';
import {UserModal} from "./UserModal";
import './PastTrips.css'

export class PastTrips extends Component {

    componentDidMount() {
        document.title = "Bumpy - Trips"
    }

    constructor(props) {
        super(props);
        this.state = {
            trips: undefined,
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
        // testing id: 5efa0f9f-ee0a-45c9-ac20-ac4bb76dc83f
        fetch(`/v1/trip/getTripsByDeviceUUID?deviceUUID=${deviceUUID}`)
            .then(response => response.json())
            .then(data => {
                let parsedTrips = JSON.parse(data).map(t => {
                        console.log(t);
                        return {
                            "trip_uuid": t.trip_uuid,
                            "distance": t.distance.toFixed(2),
                            "start_ts": new Date(t.start_ts.$date).toLocaleString(),
                            "end_ts": new Date(t.end_ts.$date).toLocaleString(),
                            "vibration": Math.floor(Math.random() * Math.floor(80))
                        };
                    }
                );

                this.setState({trips: parsedTrips})
            });
    };

    render() {
        const {trips} = this.state;
        let tripTable = "";
        if (trips !== undefined) {
            console.log(trips)

            const columns = [{
                dataField: 'start_ts',
                text: 'Start time',
                sort: true
            }, {
                dataField: 'end_ts',
                text: 'End time',
                sort: true
            }, {
                dataField: 'distance',
                text: 'Distance(km)',
                sort: true
            }, {
                dataField: 'vibration',
                text: 'Average vibration(%)',
                sort: true
            }];

            const defaultSorted = [{
                dataField: 'start_ts',
                order: 'desc'
            }];

            tripTable =
                <BootstrapTable
                    bootstrap4
                    keyField="trip_uuid"
                    data={trips}
                    columns={columns}
                    defaultSorted={defaultSorted}
                    noDataIndication="There is no data for given User"
                    pagination={paginationFactory()}
                />
        }

        return (
            <div>
                <Button className="btn"
                        onClick={() => this.setState({shouldShowModal: true})}>
                    Enter User Identifier
                </Button>
                <UserModal show={this.state.shouldShowModal} onHide={this.onModalClose.bind(this)}/>

                {tripTable}
            </div>
        )
    }
}
