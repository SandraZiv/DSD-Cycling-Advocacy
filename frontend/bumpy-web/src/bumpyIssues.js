import L from "leaflet";
import {Marker, Popup} from "react-leaflet";
import React from "react";

export const bumpToMarker = (bump, i) => {
    let score;
    switch (bump.bumpyScore) {
        case 1: score = "Minor bump"; break;
        case 2: score = "Small bump"; break;
        case 3: score = "Moderate Bump"; break;
        case 4: score = "Large bump"; break;
        case 5: score = "Critical bump"; break;
        default: score = bump.bumpyScore;
    }
    

    return <Marker key={i} icon={bumpIconMarker} position={[bump.lat, bump.lon]}>
        <Popup>
            <span>{score}</span>
        </Popup>
    </Marker>
};

const bumpIconMarker = new L.icon({
    iconUrl: require('./images/marker.png'),
    iconSize: [20, 35]
});