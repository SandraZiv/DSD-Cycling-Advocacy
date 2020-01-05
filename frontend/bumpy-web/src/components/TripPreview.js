import React, {Component} from 'react';
import {Button, Card, CardGroup} from 'react-bootstrap';
import {Map as LeafletMap, TileLayer} from 'react-leaflet';

export class TripPreview extends Component {

    componentDidMount() {
            document.title = "Bumpy - Trip Preview"
          }

    render() {
        return(
        <Card className="text-left">
            <Card.Header as="h5">Past trip on 27.10.2019
                <Button className="btn float-right ">Export</Button>
            </Card.Header>
            <Card.Body>
                <CardGroup>
                    <Card className="text-center">
                        <Card.Body>
                            <Card.Title>15km</Card.Title>
                            <Card.Text>
                                Distance
                            </Card.Text>
                        </Card.Body>
                    </Card>
                    <Card className="text-center">
                        <Card.Body>
                            <Card.Title>1h 50 min</Card.Title>
                            <Card.Text>
                                Duration
                            </Card.Text>
                        </Card.Body>
                    </Card>
                    <Card className="text-center">
                        <Card.Body>
                            <Card.Title>15km/h</Card.Title>
                            <Card.Text>
                                Average Speed
                            </Card.Text>
                        </Card.Body>
                    </Card>
                    <Card className="text-center">
                        <Card.Body>
                            <Card.Title>30km/h</Card.Title>
                            <Card.Text>
                                Maximum speed
                            </Card.Text>
                        </Card.Body>
                    </Card>
                    <Card className="text-center">
                        <Card.Body>
                            <Card.Title>15%</Card.Title>
                            <Card.Text>
                                Average vibrations
                            </Card.Text>
                        </Card.Body>
                    </Card>
                    <Card className="text-center">
                        <Card.Body>
                            <Card.Title>10</Card.Title>
                            <Card.Text>
                                Bumps detected
                            </Card.Text>
                        </Card.Body>
                    </Card>
                    <Card className="text-center">
                        <Card.Body>
                            <Card.Title>140</Card.Title>
                            <Card.Text>
                                Average elevation
                            </Card.Text>
                        </Card.Body>
                    </Card>
                    <Card className="text-center">
                        <Card.Body>
                            <Card.Title>130</Card.Title>
                            <Card.Text>
                                Minimum elevation
                            </Card.Text>
                        </Card.Body>
                    </Card>
                    <Card className="text-center">
                        <Card.Body>
                            <Card.Title>150</Card.Title>
                            <Card.Text>
                                Maximum elevation
                            </Card.Text>
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
        )
    }
}
