import React, {useContext, useEffect, useState} from 'react';
import BootstrapTable from 'react-bootstrap-table-next'
import paginationFactory from 'react-bootstrap-table2-paginator';
import {dateFormat} from "../dateformat";
import {UuidContext} from "../Store";
import './PastTrips.css'

export const PastTrips = (props) => {
    const [, setUuid] = useContext(UuidContext);
    const [trips, setTrips] = useState(undefined);

    useEffect(() => {
        document.title = "Bumpy - Trips";

        // parse URL to get UUID
        let uuid = props.location.pathname.substring('/trips/'.length);
        setUuid(uuid);

        // testing id: 5efa0f9f-ee0a-45c9-ac20-ac4bb76dc83f
        fetch(`/v1/trip/getTripsByDeviceUUID?deviceUUID=${uuid}`)
            .then(response => response.json())
            .then(data => {
                setTrips(data.map(function (trip) {
                    trip.startTS = dateFormat(new Date(trip.startTS), "dddd, mmmm dS, yyyy, HH:MM");
                    // trip.startTS = new Date(trip.startTS).toLocaleString()
                    // trip.endTS = new Date(trip.endTS).toUTCString();
                    return trip
                }));
            });
    });

    let tripTable = "";
    if (trips !== undefined) {
        // console.log(trips)

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
            text: 'Distance(km)',
            sort: true
            // }, {
            //     dataField: 'vibration',
            //     text: 'Average vibration(%)',
            //     sort: true
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
            {tripTable}
        </div>
    )
};
