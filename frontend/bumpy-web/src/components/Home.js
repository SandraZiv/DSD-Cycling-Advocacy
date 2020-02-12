import React, {Component} from 'react';
import {Map as LeafletMap, TileLayer, Polyline} from 'react-leaflet';
import {bumpToMarker} from "../bumpyIssues";
import {fetchApi} from "../App";

export class Home extends Component {

    constructor(props) {
        super(props);
        this.state = {
            // for testing
            // latitude: 44.48773940093815,
            // longitude: 9.489166159182783,
            latitude: 45.815,
            longitude: 15.982,
            roadQualityHeatMap: undefined,
            bumpyPoints: undefined
        }
    };

    componentDidMount() {
        document.title = "Bumpy - Home";

        navigator.geolocation.getCurrentPosition(
            position => this.setState({
                latitude: position.coords.latitude,
                longitude: position.coords.longitude
            })
        );

        this.fetchRoadQualityData()
    }

    onViewportChanged = () => {
        this.fetchRoadQualityData()
      };

    fetchRoadQualityData() {
        let topRightLat = this._map.leafletElement.getBounds()._northEast.lat;
        let topRightLon = this._map.leafletElement.getBounds()._northEast.lng;
        let bottomLeftLat = this._map.leafletElement.getBounds()._southWest.lat;
        let bottomLeftLon = this._map.leafletElement.getBounds()._southWest.lng;

        fetchApi(`/mapData/getRoadQualitySegments?bottomLeftLat=${bottomLeftLat}&bottomLeftLon=${bottomLeftLon}&topRightLat=${topRightLat}&topRightLon=${topRightLon}`)
            .then(response => response.json())
            .then(data => {
                this.setState({roadQualityHeatMap: data});
            });

        fetchApi(`/mapData/getBumpyIssuePoints?bottomLeftLat=${bottomLeftLat}&bottomLeftLon=${bottomLeftLon}&topRightLat=${topRightLat}&topRightLon=${topRightLon}`)
            .then(response => response.json())
            .then(data => {
                this.setState({bumpyPoints: data});
            })
    }

    render() {
        let roadQualityPolys = "";
        if (this.state.roadQualityHeatMap !== undefined) {
            let mapData = this.state.roadQualityHeatMap;

            let i = 0; // used for keys
            roadQualityPolys = mapData.map(track => {
                return track.segments.map(s => {
                    if (s.qualityScore > 0.9) {
                        return <Polyline key={i++} positions={[[s.startLat, s.startLon], [s.endLat, s.endLon]]} color={'#b02727'}/>
                    } else if (s.qualityScore > 0.8) {
                        return <Polyline key={i++} positions={[[s.startLat, s.startLon], [s.endLat, s.endLon]]} color={'#fc0303'}/>
                     } else if (s.qualityScore > 0.7) {
                        return <Polyline key={i++} positions={[[s.startLat, s.startLon], [s.endLat, s.endLon]]} color={'#ff5900'}/>
                    } else if (s.qualityScore > 0.6) {
                        return <Polyline key={i++} positions={[[s.startLat, s.startLon], [s.endLat, s.endLon]]} color={'#ff8c00'}/>
                    } else if (s.qualityScore > 0.5) {
                        return <Polyline key={i++} positions={[[s.startLat, s.startLon], [s.endLat, s.endLon]]} color={'#ffbf00'}/>
                    } else if (s.qualityScore > 0.4) {
                        return <Polyline key={i++} positions={[[s.startLat, s.startLon], [s.endLat, s.endLon]]} color={'#ffff00'}/>
                    } else if (s.qualityScore > 0.3) {
                        return <Polyline key={i++} positions={[[s.startLat, s.startLon], [s.endLat, s.endLon]]} color={'#cdeb0c'}/>
                    } else if (s.qualityScore > 0.2) {
                        return <Polyline key={i++} positions={[[s.startLat, s.startLon], [s.endLat, s.endLon]]} color={'#0ceb4e'}/>
                    } else if (s.qualityScore > 0.1) {
                        return <Polyline key={i++} positions={[[s.startLat, s.startLon], [s.endLat, s.endLon]]} color={'#00b837'}/>
                    } else {
                        return <Polyline key={i++} positions={[[s.startLat, s.startLon], [s.endLat, s.endLon]]} color={'#078d2f'}/>
                    }
                })
            });

        }

        let bumpyIssueMarkers = "";
        if (this.state.bumpyPoints !== undefined) {
            let bumpyData = this.state.bumpyPoints;

            let i = 0; // used for keys
            bumpyIssueMarkers = bumpyData.map(bump => bumpToMarker(bump, i++));
        }

        return (
            <div>
                <h5>Road Quality Map</h5>
                <LeafletMap ref={map => (this._map = map)}
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
                    onViewportChanged={this.onViewportChanged}
                    easeLinearity={0.35}>
                    <TileLayer url='http://{s}.tile.osm.org/{z}/{x}/{y}.png'/>
                    {roadQualityPolys}
                    {bumpyIssueMarkers}
                </LeafletMap>
            </div>
        );
    }
}
