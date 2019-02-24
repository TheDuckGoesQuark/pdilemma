import {Component} from "react";
import React from "react";
import Grid from '@material-ui/core/Grid'
import Button from '@material-ui/core/Button'
import TextField from '@material-ui/core/TextField'
import {addPrisonerToGame, getPrisonerFromGame} from "../PersecutorService";
import ChoiceButtons from "./ChooseButton";

export class JoinGame extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showGameIdInput: false,
            error: false,
            gameId: null,
            prisonerId: null
        }
    }

    joinGame() {
        if (!this.state.showGameIdInput) {
            this.setState({showGameIdInput: true});
        } else if (this.state.gameId == null) {
            this.setState({error: true});
        } else {
            if (this.state.prisonerId == null) {
                addPrisonerToGame(this.state.gameId)
                    .then(prisonerId => {
                        this.props.setPrisonerId(prisonerId);
                        this.props.updateResponseText(`You are prisoner number ${prisonerId}.`);
                        this.props.updateView(ChoiceButtons)
                    }).catch(reason => {
                    this.props.updateResponseText(reason.message)
                })
            } else {
                getPrisonerFromGame(this.state.gameId)
                    .then(prisonerId => {
                        this.props.setPrisonerId(prisonerId);
                        this.props.updateResponseText(`You are prisoner number ${prisonerId}.`);
                        this.props.updateView(ChoiceButtons)
                    }).catch(reason => {
                    this.props.updateResponseText(reason.message)
                })
            }
        }
    }

    joinRandomGame() {

    }

    handleSubmit(e) {
        console.log("hello");
        e.preventDefault();
        this.joinGame()
    }

    getInputs() {
        return (<form onSubmit={(e) => this.handleSubmit(e)}>
            <Grid container direction="row" justify="space-evenly" alignItems="center">
                <Grid item xs>
                    <TextField size="large"
                               required
                               style={{backgroundColor: "grey"}}
                               id="gameId-input"
                               name={"Game Id"}
                               autoFocus
                               variant="filled"
                               label="Game Id"
                               type="number"
                               error={this.state.error}
                               onChange={(e) => this.setState({gameId: e.target.value})}
                    />
                </Grid>
                <Grid item xs>
                    <TextField size="large"
                               style={{backgroundColor: "grey"}}
                               id="gameId-input"
                               name={"Prisoner Id"}
                               variant="filled"
                               label="Prisoner Id"
                               type="number"
                               error={this.state.error}
                               onChange={(e) => this.setState({prisonerId: e.target.value})}
                    />
                </Grid>
            </Grid>
        </form>);
    }

    render() {
        return (<Grid container justify="space-around" direction="column" spacing={16}>
            <Grid item xs={12}>
                <h1>Choose Option</h1>
            </Grid>
            <Grid item xs padding={12}>
                <Button variant="contained"
                        color="primary"
                        size="large"
                        fullWidth
                        onClick={() => this.joinRandomGame()}>
                    Join Random Game
                </Button>
            </Grid>
            <Grid item xs={12}>
                <h2>Or</h2>
            </Grid>
            <Grid item xs>
                <Button variant="contained"
                        color="primary"
                        size="large"
                        fullWidth
                        onClick={() => this.joinGame()}>
                    {this.state.showGameIdInput && this.state.gameId != null
                        ? `Start Game ${this.state.gameId}`
                        : "Enter Game Id"}
                </Button>
            </Grid>
            {this.state.showGameIdInput ? this.getInputs() : null}
            <Grid item xs>
                <Button
                    color="secondary"
                    size="medium"
                    onClick={() => this.props.goBack()}>Go to Menu</Button>
            </Grid>
        </Grid>);
    }

}

export class StartGame extends Component {

}
