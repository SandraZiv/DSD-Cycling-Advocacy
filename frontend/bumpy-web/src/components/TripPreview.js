import React, {Component} from 'react';
import {Button, Card, CardGroup} from 'react-bootstrap';
import {Map as LeafletMap, TileLayer, Polyline, Marker, Popup} from 'react-leaflet';
import {formatDateDefault} from "../dateformat";
import {buildDuration, formatFloat} from "../utils";
import L from 'leaflet';

export class TripPreview extends Component {

    constructor(props) {
        super(props);
        this.state = {trip: undefined}
    }

    componentDidMount() {
        document.title = "Bumpy - Trip Preview";

        // const tripUUID = '700568e5-bfae-4908-91ec-54966c8cbb43';
        // const tripUUID = 'db68af06-d350-4207-ac7b-52f6e6a37e0c';
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

            let distance = (tripData.distance !== undefined)? formatFloat(tripData.distance) + ' km' : '';
            let duration = buildDuration(tripData.startTS, tripData.endTS);
            let avgSpeed = '', maxSpeed = '';
            if (tripData.speed !== undefined) {
               avgSpeed = formatFloat(tripData.speed.avgSpeed) + ' km/h';
               maxSpeed = formatFloat(tripData.speed.maxSpeed) + ' km/h';
            }

            let avgVibration = '15%';
            let bumpsDetected = '12';

            let avgElevation = '', maxElevation = '', minElevation = '';
            if (tripData.elevation !== undefined) {
              avgElevation = formatFloat(tripData.elevation.avgElevation) + ' m';
              maxElevation = formatFloat(tripData.elevation.maxElevation) + ' m';
              minElevation = formatFloat(tripData.elevation.minElevation) + ' m';
            }

            let points = tripData.gnssData.map(point => [point.lat, point.lon]);
            let center = points[0];
            let start = points[0];
            let end = points[points.length-1];

            card = <Card className="text-left">
                <Card.Header as="h5">{formatDateDefault(tripData.startTS)}
                    <Button className="btn float-right ">Export</Button>
                    <Button className="btn bg-danger text-white border-white float-right"><i className="fa fa-trash"/></Button>
                </Card.Header>
                <Card.Body>
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
                        center={center}
                        zoom={15}
                        maxZoom={20}
                        attributionControl={true}
                        zoomControl={true}
                        doubleClickZoom={true}
                        scrollWheelZoom={true}
                        dragging={true}
                        animate={true}
                        easeLinearity={0.35}>
                        <TileLayer url='http://{s}.tile.osm.org/{z}/{x}/{y}.png'/>
                        <Polyline positions={points} color={'red'}/>
                        <Marker position={start} icon={iconStart}>
                            <Popup>
                                <span>Trip Start<br/>{new Date(tripData.startTS).toLocaleTimeString()}</span>
                            </Popup>
                        </Marker>
                        <Marker position={end} icon={iconEnd}>
                            <Popup>
                                <span>Trip End<br/>{new Date(tripData.endTS).toLocaleTimeString()}</span>
                            </Popup>
                        </Marker>
                    </LeafletMap>
                </Card.Body>
            </Card>
        }

        return (
            <div>{card}</div>
        )
    }
}

export const iconStart = new L.icon({
    iconUrl: require('./../images/start-icon.svg'),
    iconSize: [30,30]
});

export const iconEnd = new L.icon({
    iconUrl: require('./../images/finish-icon.png'),
    iconSize: [30,30]
});
