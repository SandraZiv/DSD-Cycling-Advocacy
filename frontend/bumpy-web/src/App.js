import React from 'react';
import {Route, Switch, Redirect, Router} from 'react-router-dom';
import Store from "./Store";
import {Navigation} from "./components/Navigation";
import {Home} from './components/Home';  // Home without {} does not work no default export
import {UserUUID} from "./components/UserUUID";
import {PastTrips} from './components/PastTrips';  // PastTrips without {} does not work no default export
import {ReportIssue} from './components/ReportIssue';  // ReportIssue without {} does not work no default export
import {PastTrip} from './components/PastTrip';
import history from './setupHistory'
import './App.css';

const App = () => {
    return (
        <Store>
            <Router history={history}>
                <div className="App">
                    <Navigation/>
                    <div className="App-body">
                        <Switch>
                            <Route exact path='/' component={Home}/>
                            <Route exact path='/user' component={UserUUID}/>
                            <Route path='/trips/' component={PastTrips}/>
                            <Route exact path='/fms' component={ReportIssue}/>
                            <Route exact path='/pasttrip' component={PastTrip}/>
                            <Redirect to="/"/>
                        </Switch>
                    </div>
                </div>
            </Router>
        </Store>
    );
};

export default App;
