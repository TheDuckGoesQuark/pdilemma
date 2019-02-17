import React, {Component} from 'react';
import {testConnection} from "./PersecutorService";
import logo from './logo.svg';
import './App.css';

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {responseText: ""}
    }

    handleTestConnection() {
        testConnection()
            .then(text => this.setState({responseText: text}));
    }

    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1>Press to Test</h1>
                    <button onClick={() => this.handleTestConnection()}>Test</button>
                    <p>{this.state.responseText}</p>
                </header>
            </div>
        );
    }
}

export default App;
