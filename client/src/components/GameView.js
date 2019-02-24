import {Component} from "react";
import React from "react";
import Grid from '@material-ui/core/Grid'
import Button from '@material-ui/core/Button'
import TextField from '@material-ui/core/TextField'
import InputLabel from '@material-ui/core/InputLabel'
import InputBase from '@material-ui/core/InputBase'

export class JoinGame extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showGameIdInput: false,
            error: false,
            gameId: null
        }
    }

    joinGame() {
        if (!this.state.showGameIdInput) {
            this.setState({showGameIdInput: true});
        } else if (this.state.gameId == null) {
            this.setState({error: true});
        }
        console.log("called")
    }

    joinRandomGame() {

    }

    handleSubmit(e) {
        e.preventDefault();
        this.joinGame()
    }

    getGameIdInput() {
        return (<Grid item xs padding={12}>
                <form onSubmit={(e) => this.handleSubmit(e)}>
                <TextField size="large"
                           style={{backgroundColor: "grey"}}
                           id="gameId-input"
                           name={"Game Id"}
                           autoFocus
                           variant="filled"
                           type="number"
                           error={this.state.error}
                           onChange={(e) => this.setState({gameId: e.target.value})}
                />
            </form>
        </Grid>)
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
            {this.state.showGameIdInput ? this.getGameIdInput() : null}
        </Grid>);
    }

}

export class StartGame extends Component {

}
