import React, {Component} from 'react';
import { Map as LeafletMap, TileLayer, Marker, Popup, Polyline} from 'react-leaflet';
import Leaflet from 'leaflet/dist/leaflet.css'
import './Home.css';

export class Home extends Component {
    componentDidMount() {
        document.title = "Bumpy - Home"
      }
  render() {
    return (
      <LeafletMap
        center={[45.807323, 15.967772]}
        zoom={14}
        maxZoom={20}
        attributionControl={true}
        zoomControl={true}
        doubleClickZoom={true}
        scrollWheelZoom={true}
        dragging={true}
        animate={true}
        easeLinearity={0.35}
      >
        <TileLayer
          url='http://{s}.tile.osm.org/{z}/{x}/{y}.png'
        />
      </LeafletMap>
    );
  }
}
