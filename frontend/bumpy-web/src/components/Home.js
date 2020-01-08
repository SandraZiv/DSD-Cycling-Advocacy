import React, {Component} from 'react';
import {Map as LeafletMap, TileLayer, Polyline} from 'react-leaflet';

export class Home extends Component {

    constructor(props) {
        super(props);
        this.state = {}
        this.state = {
            latitude: 44.48773940093815,
            longitude: 9.489166159182783,
            map: undefined,
            "segments": [
                  {
                    "endLat": 44.48773940093815,
                    "endLon": 9.489166159182783,
                    "qualityScore": 1,
                    "startLat": 44.48779731988906,
                    "startLon": 9.489132799208162
                  },
                  {
                    "endLat": 44.48775054886937,
                    "endLon": 9.489150736480951,
                    "qualityScore": 5,
                    "startLat": 44.48773940093815,
                    "startLon": 9.489166159182783
                  },
                  {
                    "endLat": 44.48776395991444,
                    "endLon": 9.48911511339247,
                    "qualityScore": 10,
                    "startLat": 44.48775054886937,
                    "startLon": 9.489150736480951
                  },
                  {
                    "endLat": 44.48777267709375,
                    "endLon": 9.489066498354077,
                    "qualityScore": 6,
                    "startLat": 44.48776395991444,
                    "startLon": 9.48911511339247
                  },
                  {
                    "endLat": 44.487762199714766,
                    "endLon": 9.48896398767829,
                    "qualityScore": 3,
                    "startLat": 44.48777267709375,
                    "startLon": 9.489066498354077
                  }]
        }
    };

    componentDidMount() {
        document.title = "Bumpy - Home";

        navigator.geolocation.getCurrentPosition(
            position => this.setState({
                latitude: position.coords.latitude,
                longitude: position.coords.longitude
            })
        )

        fetch(`/v1/mapData/getRoadQualitySegments?bottomLeftLat=40.58541598932073&bottomLeftLon=8.524859477435248&topRightLat=45.59234634754337&topRightLon=10.540044381476141`)
                    .then(response => response.json())
                    .then(data => {
                        this.setState({map: data});
            })
    }


    render() {
        if (this.state.map !== undefined) {
        let mapData = this.state.map;
        //let points = mapData.segments.map(point => [point.endLat, point.endLon]);
        }
      return (
      <div>
        <h5>Road Quality Map</h5>
          <LeafletMap
                style={{
                    height: '600px',
                    width: '100%',
                    margin: '0 auto'
                }}
                center={[this.state.latitude, this.state.longitude]}
                zoom={14}
                maxZoom={19}
                attributionControl={true}
                zoomControl={true}
                doubleClickZoom={true}
                scrollWheelZoom={true}
                dragging={true}
                animate={true}
                easeLinearity={0.35}>
                <TileLayer url='http://{s}.tile.osm.org/{z}/{x}/{y}.png'/>
                 {this.state.segments.map(({startLat, startLon, endLat, endLon, qualityScore}) => {
                    if (qualityScore > 6 ){
                        return <Polyline positions={[[startLat, startLon], [endLat, endLon]]} color={'green'} />
                    } else if (qualityScore < 4 ){
                         return <Polyline positions={[[startLat, startLon], [endLat, endLon]]} color={'red'} />
                    } else {
                         return <Polyline positions={[[startLat, startLon], [endLat, endLon]]} color={'yellow'} />
                    }
                  })}
            </LeafletMap>
      </div>
        );
    }
}
