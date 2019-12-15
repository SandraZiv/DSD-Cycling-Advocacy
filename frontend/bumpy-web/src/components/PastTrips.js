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
        fetch(`/v1/trip/getTripsByDeviceUUID?deviceUUID=${deviceUUID}`)
            .then(response => response.json())
            .then(json => {
                    const data = [
                        {
                            "tripUUID": "fdsf1dsf#4s-df1sdf1sd2f3f",
                            "startTS": "2019-11-25T11:36:04Z",
                            "endTS": "2019-11-25T12:00:51Z",
                            "distance": 20.0,
                            "vibration": 125
                        },
                        {
                            "tripUUID": "affsf52-df1sdf1sd2f3f-asf",
                            "startTS": "2019-11-25T11:21:17Z",
                            "endTS": "2019-11-25T11:36:00Z",
                            "distance": 16.7,
                            "vibration": 191
                        },
                        {
                            "tripUUID": "df1sdf1sd2f3fs56sf-sssfee",
                            "startTS": "2019-10-25T11:36:04Z",
                            "endTS": "2019-10-25T12:00:51Z",
                            "distance": 42.3,
                            "vibration": 203
                        },
                    ];
                    console.log(json);
                    this.setState({trips: data})
                }
            );
    };

    render() {
        const {trips} = this.state;
        let tripTable = "";
        if (trips !== undefined) {
            const columns = [{
                dataField: 'startTS',
                text: 'Start time',
                sort: true
            }, {
                dataField: 'endTS',
                text: 'End time',
                sort: true
            }, {
                dataField: 'distance',
                text: 'Distance',
                sort: true
            }, {
                dataField: 'vibration',
                text: 'Accumulated vibration',
                sort: true
            }];

            const defaultSorted = [{
                dataField: 'startTS',
                order: 'desc'
            }];

            tripTable =
                <BootstrapTable
                    bootstrap4
                    keyField="tripUUID"
                    data={trips}
                    columns={columns}
                    defaultSorted={defaultSorted}
                    noDataIndication="There is no data for given User"
                    pagination={paginationFactory()}
                />
        }

        return (
            <div>
                <Button bsClass="btn"
                        onClick={() => this.setState({shouldShowModal: true})}>
                    Enter User Identifier
                </Button>
                <UserModal show={this.state.shouldShowModal} onHide={this.onModalClose.bind(this)}/>

                {tripTable}
            </div>
        )
    }
}
