import React, {useContext, useEffect, useState} from 'react';
import BootstrapTable from 'react-bootstrap-table-next'
import paginationFactory from 'react-bootstrap-table2-paginator';
import {formatDateDefault} from "../dateformat";
import {UuidContext} from "../Store";
import {Link} from "react-router-dom";
import {Button} from 'react-bootstrap';
import {formatFloat} from "../utils";
import './PastTrips.css'

export const PastTrips = (props) => {
    const [uuid, setUuid] = useContext(UuidContext);
    const [trips, setTrips] = useState(undefined);

    const fetchData = async (urlUUID, signal) => {
        // let urlUUID = 5efa0f9f-ee0a-45c9-ac20-ac4bb76dc83f;
        try {
            await fetch(`/v1/trip/getTripsByDeviceUUID?deviceUUID=${urlUUID}`, {signal: signal})
                .then(response => response.json())
                .then(data => {
                    setTrips(data.map(function (trip) {
                        trip.startTS = formatDateDefault(trip.startTS);
                        trip.endTS = formatDateDefault(trip.endTS);
                        trip.distance = formatFloat(trip.distance);
                        return trip
                    }));
                });
        } catch (e) {
            
        }
    };

    useEffect(() => {
        let urlUUID = props.location.pathname.split('/').pop();

        if (urlUUID === '' && (uuid === undefined || uuid === '')) {
            props.history.push('/login');
            return;
        }

        document.title = "Bumpy - Trips";

        setUuid(urlUUID);

        const abortController = new AbortController();
        fetchData(urlUUID, abortController.signal);

        return () => {
            // clean up
            abortController.abort();
        };
    }, [uuid, setUuid, props.history, props.location.pathname]);

    let buttonFormatter = (cell, row) =>
        <Link to={`/trips/${row.tripUUID}`}>
            <Button className="btn bg-success text-white border-white">
                <i className="fa fa-info"/>
            </Button>
        </Link>;

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
        },  {
             dataField: 'details',
             text: '',
             sort: false,
             isDummyField: true,
             formatter: buttonFormatter,
             headerStyle: () => {
                   return { width: "10%" }}

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
