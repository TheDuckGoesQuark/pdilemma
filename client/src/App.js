import React, {Component} from 'react';
import logo from './logo.svg';
import Button from '@material-ui/core/Button'
import Grid from '@material-ui/core/Grid'
import './App.css';
import TestButton from "./components/TestButton";
import ChoiceButtons from "./components/ChooseButton";
import JoinGame from "./components/JoinGame";
import {addPrisonerToGame, startGame} from "./PersecutorService";

const TITLE_TEXT = "Prisoners Dilemma";

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            responseText: TITLE_TEXT,
            currentView: Menu,
            prisoner: undefined,
            gameId: undefined
        };
    }

    updateView(newView) {
        this.setState({currentView: newView})
    }

    updateResponseText(newText) {
        this.setState({responseText: newText})
    }

    updatePrisoner(prisoner) {
        this.setState({prisoner: prisoner})
    }

    updateGameId(gameId) {
        this.setState({gameId: gameId})
    }

    getView() {
        switch (this.state.currentView) {
            case TestButton:
                return <TestButton goBack={() => {
                    this.updateResponseText(TITLE_TEXT);
                    this.updateView(Menu)
                }}
                                   updateResponseText={(text) => this.updateResponseText(text)}/>;
            case ChoiceButtons:
                return <ChoiceButtons
                    goBack={() => this.updateView(Menu)}
                    updateView={(choice, years) => {
                        this.updateResponseText(`You chose ${choice} and received ${years} reduction.`);
                        this.updateView(Menu)
                    }}
                    gameId={this.state.gameId}
                    prisoner={this.state.prisoner}
                />;
            case JoinGame:
                return <JoinGame
                    goBack={() => this.updateView(Menu)}
                    updatePrisoner={(prisoner) => this.updatePrisoner(prisoner)}
                    updateGameId={(gId) => this.updateGameId(gId)}
                    updateView={(newView) => this.updateView(newView)}
                    updateResponseText={(text) => this.updateResponseText(text)}
                />;
            default:
            case Menu:
                return <Menu
                    updateView={(viewToChangeTo) => this.updateView(viewToChangeTo)}
                    updatePrisoner={(prisoner) => this.updatePrisoner(prisoner)}
                    updateResponseText={(text) => this.updateResponseText(text)}
                    updateGameId={(gId) => this.updateGameId(gId)}
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
                            onClick={() => this.startNewGame()}>
                        Start Game
                    </Button>
                </Grid>
            </Grid>
        )
    }

    startNewGame() {
        startGame().then((gameId) => {
            this.props.updateGameId(gameId);
            return addPrisonerToGame(gameId)
        }).then((prisoner) => {
            this.props.updatePrisoner(prisoner);
            this.props.updateResponseText(`You are prisoner number ${prisoner.prisonerId}.`);
            this.props.updateView(ChoiceButtons)
        }).catch((reason) => this.props.updateResponseText(reason.message))
    }


}

export default App;
