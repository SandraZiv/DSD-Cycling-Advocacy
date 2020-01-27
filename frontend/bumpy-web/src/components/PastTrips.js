import React, {useContext, useEffect, useState} from 'react';
import BootstrapTable from 'react-bootstrap-table-next'
import paginationFactory from 'react-bootstrap-table2-paginator';
import {formatDateDefault} from "../dateformat";
import {ShortUuidContext} from "../Store";
import {Link} from "react-router-dom";
import {Button} from 'react-bootstrap';
import {formatFloat} from "../utils";
import './PastTrips.css'

export const PastTrips = (props) => {
    const [shortUuid, setShortUuid] = useContext(ShortUuidContext);
    const [trips, setTrips] = useState(undefined);

    useEffect(() => {
        let urlUUID = props.location.pathname.split('/').pop();

        if (urlUUID === '' && (shortUuid === undefined || shortUuid === '')) {
            props.history.push('/login');
            return;
        }

        document.title = "Bumpy - Trips";

        fetch(`/api/v1/device/getLongDeviceUUID?shortDeviceUUID=${urlUUID}`)
            .then(response => {
                if (response.ok) {
                    setShortUuid(urlUUID);
                    return response.text()
                } else {
                    alert('User Identifier not valid');
                    props.history.push('/login');
                }
            })
            .then(text => {
                if (text === undefined) {
                    return
                }

                let clearText = text.split("\"");
                let longUuid = (clearText.length > 1) ? clearText[1] : clearText[0];

                fetch(`/api/v1/trip/getTripsByDeviceUUID?deviceUUID=${longUuid}`)
                    .then(response => response.json())
                    .then(data => {
                        setTrips(data.map(function (trip) {
                            trip.startTS = formatDateDefault(trip.startTS);
                            trip.endTS = formatDateDefault(trip.endTS);
                            trip.distance = formatFloat(trip.distance);
                            trip.avgVibration = "";
                            if (trip.vibration !== undefined) {
                                trip.avgVibration = formatFloat(trip.vibration.avgVibration);
                            }
                            return trip
                        }));
                    });
            })

    }, [shortUuid, setShortUuid, props.history, props.location.pathname]);

    let buttonFormatter = (cell, row) =>
        <Link to={`/trips/${row.tripUUID}`}>
            <Button className="btn bg-success text-white border-white">
                <i className="fa fa-info"/>
            </Button>
        </Link>;

    let deleteFormatter = (cell, row) =>
        (<Button className = "btn bg-danger text-white border-white" onClick={() => {
            if (window.confirm('Are you sure you wish to delete this trip?')){
                fetch(`/api/v1/trip/deleteTrip?tripUUID=${row.tripUUID}`, {
                    method: 'DELETE'
                }).then(response => {
                    window.location.reload();
                })}
            }}> <i className="fa fa-trash"/>
        </Button>
    );


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
            text: 'Distance(km)',
            sort: true
        }, {
            dataField: 'avgVibration',
            text: 'Average vibration(%)',
            sort: true
        },  {
             dataField: 'details',
             text: '',
             sort: false,
             isDummyField: true,
             formatter: buttonFormatter,
             headerStyle: () => {
                   return { width: "10%" }}

        }, {
             dataField: 'delete',
             text: '',
             sort: false,
             isDummyField: true,
             formatter: deleteFormatter,
             headerStyle: () => {
                    return { width: "10%" }}
        }];


        tripTable =
            <BootstrapTable
                bootstrap4
                keyField="tripUUID"
                data={trips}
                columns={columns}
                noDataIndication="No performed trips yet"
                pagination={paginationFactory()}
            />
    }

    return (
        <div>
            {tripTable}
        </div>
    )
};
