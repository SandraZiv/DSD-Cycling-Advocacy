import React from 'react';
import {Route, Switch, Redirect, Router} from 'react-router-dom';
import Store from "./Store";
import {Navigation} from "./components/Navigation";
import {Home} from './components/Home';  // Home without {} does not work no default export
import {UserUUID} from "./components/UserUUID";
import {PastTrips} from './components/PastTrips';  // PastTrips without {} does not work no default export
import {TripPreview} from './components/TripPreview';
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
                            <Route path='/user/' component={PastTrips}/>
                            <Route path='/trips/' component={TripPreview}/>
                            <Route exact path='/login' component={UserUUID}/>
                            <Redirect to="/"/>
                        </Switch>
                    </div>
                </div>
            </Router>
        </Store>
    );
};

export default App;
