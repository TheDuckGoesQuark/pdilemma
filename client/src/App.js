import React, {Component} from 'react';
import {makeChoice, testConnection} from "./PersecutorService";
import logo from './logo.svg';
import './App.css';

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            responseText: "",
            currentView: TestButton
        };
    }

    updateView(newView) {
        this.setState({currentView: newView})
    }

    updateResponseText(newText) {
        this.setState({responseText: newText})
    }

    getView() {
        switch (this.state.currentView) {
            default:
            case TestButton:
                return <TestButton updateView={() => this.updateView(ChoiceButtons)}
                                   updateResponseText={(text) => this.updateResponseText(text)}/>;
            case ChoiceButtons:
                return <ChoiceButtons
                    updateView={(choice, years) => this.updateResponseText(`You chose ${choice} and received ${years} reduction.`)}/>;
        }
    }

    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    {this.getView()}
                    <p>{this.state.responseText}</p>
                </header>
            </div>
        );
    }
}

class TestButton extends Component {
    handleTestConnection() {
        testConnection()
            .then(text => {
                this.props.updateResponseText(text);
                this.props.updateView();
            }).catch(reason => {
                this.props.updateResponseText(reason);
        });
    }

    render() {
        return <div>
            <h1>Press to Test</h1>
            <button onClick={() => this.handleTestConnection()}>Test</button>
        </div>
    }
}

class ChoiceButtons extends Component {
    handleCooperate() {
        makeChoice("C")
            .then((response) => this.props.updateView("Cooperate", response.numYearsReduction))
    }

    handleBetray() {
        makeChoice("B")
            .then((response) => this.props.updateView("Betray", response.numYearsReduction))
    }

    render() {
        return <div>
            <button onClick={() => this.handleCooperate()}>Cooperate [C]</button>
            <button onClick={() => this.handleBetray()}>Betray [B]</button>
        </div>
    }
}

export default App;
