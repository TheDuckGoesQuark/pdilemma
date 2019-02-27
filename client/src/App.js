import React, {Component} from 'react';
import Grid from '@material-ui/core/Grid'
import './App.css';
import TestButton from "./components/TestButton";
import ChoiceView from "./components/ChooseButton";
import JoinGame from "./components/JoinGame";
import Header from "./components/Header";
import MainMenu from "./components/MainMenu";

const TITLE_TEXT = "Prisoners Dilemma";

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            headerText: TITLE_TEXT,
            currentView: MainMenu,
            prisoner: undefined,
            gameId: undefined
        };
    }

    updateView(newView) {
        this.setState({currentView: newView})
    }

    updateResponseText(newText) {
        this.setState({headerText: newText})
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
                return <TestButton
                    goBack={() => {
                        this.updateResponseText(TITLE_TEXT);
                        this.updateView(MainMenu)
                    }}
                    updateResponseText={(text) => this.updateResponseText(text)}/>;
            case ChoiceView:
                return <ChoiceView
                    goBack={() => this.updateView(MainMenu)}
                    updateResponseText={(text) => this.updateResponseText(text)}
                    gameId={this.state.gameId}
                    prisoner={this.state.prisoner}
                />;
            case JoinGame:
                return <JoinGame
                    goBack={() => this.updateView(MainMenu)}
                    updatePrisoner={(prisoner) => this.updatePrisoner(prisoner)}
                    updateGameId={(gId) => this.updateGameId(gId)}
                    updateView={(newView) => this.updateView(newView)}
                    updateResponseText={(text) => this.updateResponseText(text)}
                />;
            default:
            case MainMenu:
                return <MainMenu
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
                        <Header text={this.state.headerText}/>
                    </Grid>
                    <Grid item xs={4}>
                        {this.getView()}
                    </Grid>
                </Grid>
            </div>
        );
    }
}

export default App;
