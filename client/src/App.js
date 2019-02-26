import React, {Component} from 'react';
import logo from './logo.svg';
import Button from '@material-ui/core/Button'
import Grid from '@material-ui/core/Grid'
import './App.css';
import TestButton from "./components/TestButton";
import ChoiceButtons from "./components/ChooseButton";
import {JoinGame, StartGame} from "./components/GameView";

const TITLE_TEXT = "Prisoners Dilemma";

class App extends Component {


    constructor(props) {
        super(props);
        this.state = {
            responseText: TITLE_TEXT,
            currentView: Menu
        };
    }

    updateView(newView) {
        this.setState({currentView: newView})
    }

    updateResponseText(newText) {
        this.setState({responseText: newText})
    }

    updatePrisonerId(prisonerId) {
        this.setState({prisonerId: prisonerId})
    }

    getView() {
        switch (this.state.currentView) {
            case TestButton:
                return <TestButton goBack={() => {this.updateResponseText(TITLE_TEXT); this.updateView(Menu)}}
                                   updateResponseText={(text) => this.updateResponseText(text)}/>;
            case ChoiceButtons:
                return <ChoiceButtons
                    goBack={() => this.updateView(Menu)}
                    updateView={(choice, years) => this.updateResponseText(`You chose ${choice} and received ${years} reduction.`)}/>;
            case JoinGame:
                return <JoinGame
                    goBack={() => this.updateView(Menu)}
                    setPrisonerId={(pId) => this.updatePrisonerId(pId)}
                    updateView={(newView) => this.updateView(newView)}
                    updateResponseText={(text) => this.updateResponseText(text)}
                />;
            case StartGame:
                return <StartGame
                    goBack={() => this.updateView(Menu)}
                />;
            default:
            case Menu:
                return <Menu
                    updateView={(viewToChangeTo) => this.updateView(viewToChangeTo)}
                />
        }
    }

    render() {
        return (
            <div className="App">
                <Grid container justify="center">
                    <Grid item xs={12}>
                        <header className="App-header">
                            <img src={logo} className="App-logo" alt="logo"/>
                            <p>{this.state.responseText}</p>
                        </header>
                    </Grid>
                    <Grid item xs={4}>
                        {this.getView()}
                    </Grid>
                </Grid>
            </div>
        )
            ;
    }
}

class Menu extends Component {
    handleChoice(choice) {
        this.props.updateView(choice);
    }

    render() {
        return (
            <Grid container justify="space-between" direction="column" spacing={16}>
                <Grid item xs={12}>
                    <h1>Choose Option</h1>
                </Grid>
                <Grid item xs padding={12}>
                    <Button variant="contained"
                            color="primary"
                            size="large"
                            fullWidth
                            onClick={() => this.handleChoice(TestButton)}>
                        Test Connection
                    </Button>
                </Grid>
                <Grid item xs>
                    <Button variant="contained"
                            color="primary"
                            size="large"
                            fullWidth
                            onClick={() => this.handleChoice(JoinGame)}>
                        Join Game
                    </Button>
                </Grid>
                <Grid item xs>
                    <Button variant="contained"
                            color="primary"
                            size="large"
                            fullWidth
                            onClick={() => this.handleChoice(StartGame)}>
                        Start Game
                    </Button>
                </Grid>
            </Grid>
        )
    }
}

export default App;
