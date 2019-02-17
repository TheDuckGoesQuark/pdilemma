import React, {Component} from 'react';
import {testConnection} from "./PersecutorService";
import logo from './logo.svg';
import './App.css';

class App extends Component {
    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1>Press to Test</h1>
                    <button onClick={() => testConnection()}>Test</button>
                </header>
            </div>
        );
    }
}

export default App;
