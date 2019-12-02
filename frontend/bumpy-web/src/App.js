import React from 'react';
import {BrowserRouter, Route, Switch, Redirect} from 'react-router-dom';
import {Home} from './components/Home';  // Home without {} does not work no default export
import {PastTrips} from './components/PastTrips';  // PastTrips without {} does not work no default export
import {ReportIssue} from './components/ReportIssue';  // ReportIssue without {} does not work no default export
import {Navigation} from "./components/Navigation";
import './App.css';

function App() {
    return (
        <BrowserRouter>
            <div className="App">
                <Navigation/>
                <div className="App-body">
                    <Switch>
                        <Route exact path='/' component={Home}/>
                        <Route exact path='/trips' component={PastTrips}/>
                        <Route exact path='/fms' component={ReportIssue}/>
                        <Redirect to="/"/>
                    </Switch>
                </div>
            </div>
        </BrowserRouter>
    );
}

export default App;
