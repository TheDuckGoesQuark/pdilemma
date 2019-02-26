import React, {Component} from "react";
import Button from '@material-ui/core/Button'
import Grid from '@material-ui/core/Grid'
import {getYearsReduction, httpCodes, makeChoice} from "../PersecutorService";
import {CircularProgress} from "@material-ui/core";

class ChoiceView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            polling: false,
            choice: props.prisoner.choice
        }
    }

    handleCooperate() {
        makeChoice(this.props.gameId, this.props.prisoner.prisonerId, "C")
            .then(() => this.setState({polling: true, choice: "Cooperate"}));
    }

    handleBetray() {
        makeChoice(this.props.gameId, this.props.prisoner.prisonerId, "B")
            .then(() => this.setState({polling: true, choice: "Betray"}));
    }

    handleChoice(choice) {
        switch (choice) {
            case "B":
                this.handleBetray();
                break;
            case "C":
                this.handleCooperate();
                break;
        }
    }

    getView() {
        if (this.state.polling) {
            return <PollingIcon
                handleReturn={retVal=>this.handleReturnValue(retVal)}
            />
        } else {
            return <ChoiceButtons
                handleChoice={choice=>this.handleChoice(choice)}
            />
        }
    }

    handleReturnValue(retVal) {

    }
}

class PollingIcon extends Component {

    componentDidMount() {
        this.timer = setInterval(() => this.pollForYearsReduction(), 2)
    }

    componentWillUnmount() {
        clearInterval(this.timer);
    }

    pollForYearsReduction() {
        return getYearsReduction(this.props.gameId, this.props.prisoner.prisonerId)
            .then(numYears => {
                this.props.updateView(this.state.choice, numYears);
                this.setState({polling: false})
            })
            .catch(reason => {
                if (reason.status !== httpCodes.preconditionFailed) {
                    this.props.this.setState({polling: false})
                }
            })
    }

    render() {
        return (
            <Grid item xs>
                <h1>Waiting for other prisoner...</h1>
                <CircularProgress/>
            </Grid>
        )
    }
}

class ChoiceButtons extends Component {
    render() {
        return <Grid container direction="row" justify="space-between" alignItems="center">
            <Grid item xs>
                <Button variant="contained"
                        color="primary"
                        fullWidth
                        onClick={() => this.handleCooperate()}>
                    Cooperate [C]
                </Button>
            </Grid>
            <Grid item xs={2}>
            </Grid>
            <Grid item xs>
                <Button variant="contained"
                        color="primary"
                        fullWidth
                        onClick={() => this.handleBetray()}>
                    Betray [B]
                </Button>
            </Grid>
        </Grid>
    }
}

export default ChoiceView
