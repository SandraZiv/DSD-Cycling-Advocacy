import React, {Component} from 'react';
import {Button, Card, CardGroup} from 'react-bootstrap';
import {Map as LeafletMap, TileLayer, Polyline} from 'react-leaflet';
import L from "leaflet";
import {formatDateDefault} from "../dateformat";

export class TripPreview extends Component {

    constructor(props) {
        super(props);
        this.state = {trip: undefined}
    }

    componentDidMount() {
        document.title = "Bumpy - Trip Preview";

        // const tripUUID = '700568e5-bfae-4908-91ec-54966c8cbb43';
        let tripUUID = this.props.location.pathname.split('/').pop();

        fetch(`/v1/trip/getTripByTripUUID?tripUUID=${tripUUID}`)
            .then(response => response.json())
            .then(data => {
                this.setState({trip: data});
            })
    }

    render() {
        let card = '';
        if (this.state.trip !== undefined) {
            let tripData = this.state.trip;

            let distance = (tripData.distance !== undefined)? this.formatFloat(tripData.distance) + ' km' : '';
            let duration = this.buildDuration(tripData.startTS, tripData.endTS);
            let avgSpeed = '', maxSpeed = '';
            if (tripData.speed !== undefined) {
               avgSpeed = this.formatFloat(tripData.speed.avgSpeed) + ' km/h';
               maxSpeed = this.formatFloat(tripData.speed.maxSpeed) + ' km/h';
            }

            let avgVibration = '15%';
            let bumpsDetected = '12';

            let avgElevation = '', maxElevation = '', minElevation = '';
            if (tripData.elevation !== undefined) {
              avgElevation = this.formatFloat(tripData.elevation.avgElevation) + ' m';
              maxElevation = this.formatFloat(tripData.elevation.maxElevation) + ' m';
              minElevation = this.formatFloat(tripData.elevation.minElevation) + ' m';
            }

            let points = tripData.gnssData.map((lat,lon) => {
                return <li>{lat.lat}, {lat.lon}</li> //manage to display them, I can't pass them correctly to polyline
            }
            );

            card = <Card className="text-left">
                <Card.Header as="h5">{`Trip started: ${formatDateDefault(tripData.startTS)}`}
                    <Button className="btn float-right ">Export</Button>
                    <Button className="btn bg-danger float-right"><i class="fa fa-trash"></i></Button>
                </Card.Header>
                <Card.Body>
                    <p> {points} </p> //Displaying if points are ok
                    <CardGroup>
                        <Card className="text-center">
                            <Card.Body>
                                <Card.Title>{distance}</Card.Title>
                                <Card.Text>Distance</Card.Text>
                            </Card.Body>
                        </Card>
                        <Card className="text-center">
                            <Card.Body>
                                <Card.Title>{duration}</Card.Title>
                                <Card.Text>Duration</Card.Text>
                            </Card.Body>
                        </Card>
                        <Card className="text-center">
                            <Card.Body>
                                <Card.Title>{avgSpeed}</Card.Title>
                                <Card.Text>Average Speed</Card.Text>
                            </Card.Body>
                        </Card>
                        <Card className="text-center">
                            <Card.Body>
                                <Card.Title>{maxSpeed}</Card.Title>
                                <Card.Text>Maximum speed</Card.Text>
                            </Card.Body>
                        </Card>
                        <Card className="text-center">
                            <Card.Body>
                                <Card.Title>{avgVibration}</Card.Title>
                                <Card.Text>Average vibrations</Card.Text>
                            </Card.Body>
                        </Card>
                        <Card className="text-center">
                            <Card.Body>
                                <Card.Title>{bumpsDetected}</Card.Title>
                                <Card.Text>Bumps detected</Card.Text>
                            </Card.Body>
                        </Card>
                        <Card className="text-center">
                            <Card.Body>
                                <Card.Title>{avgElevation}</Card.Title>
                                <Card.Text>Average elevation</Card.Text>
                            </Card.Body>
                        </Card>
                        <Card className="text-center">
                            <Card.Body>
                                <Card.Title>{minElevation}</Card.Title>
                                <Card.Text>Minimum elevation</Card.Text>
                            </Card.Body>
                        </Card>
                        <Card className="text-center">
                            <Card.Body>
                                <Card.Title>{maxElevation}</Card.Title>
                                <Card.Text>Maximum elevation</Card.Text>
                            </Card.Body>
                        </Card>
                    </CardGroup>
                    <LeafletMap
                        style={{
                            height: '360px',
                            width: '100%',
                            margin: '10px auto'
                        }}
                        center={[45.8002408, 15.9809588]} //should be from points also
                        zoom={15}
                        maxZoom={20}
                        attributionControl={true}
                        zoomControl={true}
                        doubleClickZoom={true}
                        scrollWheelZoom={true}
                        dragging={true}
                        animate={true}
                        easeLinearity={0.35}>
                        <TileLayer url='http://{s}.tile.osm.org/{z}/{x}/{y}.png' />
                        <Polyline  positions={[
                                  [45.8002351, 15.9709695], [45.8002408, 15.9809588],  [45.80023, 15.999663] //hardcoded position are displayed
                                  //points
                                ]} color={'red'} />

                    </LeafletMap>
                </Card.Body>
            </Card>
        }

        return (
            <div>
                {card}
            </div>
        )
    }
}
