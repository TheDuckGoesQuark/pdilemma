import React, {Component} from "react";
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";
import {addPrisonerToGame, startGame} from "../PersecutorService";
import ChoiceView from "./ChooseButton";
import TestButton from "./TestButton";
import JoinGame from "./JoinGame";


class MainMenu extends Component {
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
        let newGameId;
        startGame().then((gameId) => {
            newGameId= gameId;
            this.props.updateGameId(gameId);
            return addPrisonerToGame(gameId)
        }).then((prisoner) => {
            this.props.updatePrisoner(prisoner);
            this.props.updateResponseText(`You are prisoner number ${prisoner.prisonerId}. Share your game Id: ${newGameId}`);
            this.props.updateView(ChoiceView)
        }).catch((reason) => this.props.updateResponseText(reason.message))
    }
}

export default MainMenu

