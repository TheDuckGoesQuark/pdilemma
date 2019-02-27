import {Component} from "react";
import React from "react";
import Grid from '@material-ui/core/Grid'
import Button from '@material-ui/core/Button'
import TextField from '@material-ui/core/TextField'
import {addPrisonerToGame, getJoinableGames, getPrisonerFromGame, httpCodes} from "../PersecutorService";
import ChoiceView from "./ChooseButton";

class JoinGame extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showGameIdInput: false,
            error: false,
            gameId: null,
            prisonerId: null
        }
    }

    handleErrorResponse(reason) {
        this.props.updateResponseText(reason.message);
        this.setState({error: true});
    }

    joinGame(useUserInput) {
        if (useUserInput && !this.state.showGameIdInput) {
            this.setState({showGameIdInput: true});
        } else if (this.state.gameId == null || this.state.gameId === "") {
            this.setState({error: true});
        } else {
            if (this.state.prisonerId === undefined || this.state.prisonerId == null) {
                return addPrisonerToGame(this.state.gameId)
                    .then(prisoner => this.prepareChoiceView(prisoner))
                    .catch(reason => this.handleErrorResponse(reason))
            } else {
                return getPrisonerFromGame(this.state.gameId, this.state.prisonerId)
                    .then(prisoner => this.prepareChoiceView(prisoner))
                    .catch(reason => this.handleErrorResponse(reason))
            }
        }
    }

    prepareChoiceView(prisoner) {
        this.props.updatePrisoner(prisoner);
        this.props.updateGameId(this.state.gameId);
        this.props.updateResponseText(`You are prisoner number ${prisoner.prisonerId}. Share your game Id: ${this.state.gameId}`);
        this.props.updateView(ChoiceView)
    }

    joinRandomGame() {
        getJoinableGames()
            .then((games) => {
                if (games.length === 0) {
                    this.props.updateResponseText("No games are currently available to join.");
                } else {
                    this.setState({gameId: games[0].gameId});
                    this.joinGame(false)
                }
            })
            .catch(reason => {
                // Try again if someone else claimed space in game or game was deleted
                if (reason.status === httpCodes.conflict || reason.status === httpCodes.notFound) {
                    this.joinRandomGame()
                } else {
                    this.handleErrorResponse(reason)
                }
            })
    }

    handleSubmit(e) {
        console.log("hello");
        e.preventDefault();
        this.joinGame(true)
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
                        onClick={() => this.joinGame(true)}>
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

export default JoinGame
