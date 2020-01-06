import React, {Component} from 'react';
import {Map as LeafletMap, TileLayer} from 'react-leaflet';

export class Home extends Component {

    constructor(props) {
        super(props);
        this.state = {
            latitude: 45.815,
            longitude: 15.982
        }
    }

    componentDidMount() {
        document.title = "Bumpy - Home";

        navigator.geolocation.getCurrentPosition(
            position => this.setState({
                latitude: position.coords.latitude,
                longitude: position.coords.longitude
            })
        )
    }


    render() {
      return (
          <LeafletMap
                style={{
                    height: '600px',
                    width: '100%',
                    margin: '0 auto'
                }}
                center={[this.state.latitude, this.state.longitude]}
                zoom={14}
                maxZoom={20}
                attributionControl={true}
                zoomControl={true}
                doubleClickZoom={true}
                scrollWheelZoom={true}
                dragging={true}
                animate={true}
                easeLinearity={0.35}>
                <TileLayer url='http://{s}.tile.osm.org/{z}/{x}/{y}.png'/>
            </LeafletMap>
        );
    }
}
