import React, {Component} from 'react';
import {Button, Card, CardGroup} from 'react-bootstrap';
import {Map as LeafletMap, TileLayer} from 'react-leaflet';

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

    formatFloat(value) {
        return parseFloat(value).toFixed(2);
    }

    buildDuration(start, end) {
        let seconds = (new Date(end) - new Date(start))/1000;
        let hours =  this.zeroFill(Math.floor(seconds/3600), 2);
        seconds = seconds % 3600;
        let minutes = this.zeroFill(Math.floor(seconds/60), 2);
        seconds = this.zeroFill(Math.round(seconds % 60), 2);

        return `${hours}:${minutes}:${seconds}`
    }

    zeroFill(number, width) {
        width -= number.toString().length;
        if (width > 0) {
            return new Array(width + (/\./.test(number) ? 2 : 1)).join('0') + number;
        }
        return number + ""; // always return a string
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

            card = <Card className="text-left">
                <Card.Header as="h5">{new Date(tripData.startTS).toLocaleDateString()}
                    <Button className="btn float-right ">Export</Button>
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
                        center={[45.807323, 15.967772]}
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
