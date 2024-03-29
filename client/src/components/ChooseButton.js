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
        this.setState({choice:"C"});
        makeChoice(this.props.gameId, this.props.prisoner.prisonerId, "C")
            .then(() => this.setState({polling: true, choice: "Cooperate"}));
    }

    handleBetray() {
        this.setState({choice:"B"});
        makeChoice(this.props.gameId, this.props.prisoner.prisonerId, "B")
            .then(() => this.setState({polling: true, choice: "Betray"}));
    }

    handleReply(retVal) {
        let fullChoiceString = this.state.choice === "C" ? "Cooperate" : "Betray";
        this.props.goBack();
        this.props.updateResponseText(`You received a reduction to your sentence of ${retVal} years for choosing to ${fullChoiceString}`)
    }

    handleFailToPoll(reason) {
        this.props.goBack();
        this.props.updateResponseText(reason.message)
    }

    getView() {
        if (this.state.polling || this.state.choice !== undefined && this.state.choice !== null) {
            return <PollingIcon
                handleReply={retVal => this.handleReply(retVal)}
                handleFailToPoll={reason => this.handleFailToPoll(reason)}
                prisoner={this.props.prisoner}
                gameId={this.props.gameId}
            />
        } else {
            return <ChoiceButtons
                handleCooperate={() => this.handleCooperate()}
                handleBetray={() => this.handleBetray()}
            />
        }
    }

    render() {
        return this.getView();
    }
}

class PollingIcon extends Component {

    constructor(props) {
        super(props);
        this.delay = 2000
    }

    componentDidMount() {
        this.beginTimer()
    }

    beginTimer() {
        this.timer = setTimeout(() => this.pollForYearsReduction(), this.delay)
    }

    componentWillUnmount() {
        clearTimeout(this.timer);
    }

    updateDelay() {
        this.delay = this.delay * (1 + Math.random());
        if (this.delay > 7000) {
            this.delay = 2000
        }
    }

    pollForYearsReduction() {
        return getYearsReduction(this.props.gameId, this.props.prisoner.prisonerId)
            .then(numYears => this.props.handleReply(numYears))
            .catch(reason => {
                if (reason.status !== httpCodes.preconditionFailed) {
                    this.props.handleFailToPoll(reason)
                } else {
                    this.updateDelay();
                    this.beginTimer();
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
                        onClick={() => this.props.handleCooperate()}>
                    Cooperate [C]
                </Button>
            </Grid>
            <Grid item xs={2}>
            </Grid>
            <Grid item xs>
                <Button variant="contained"
                        color="primary"
                        fullWidth
                        onClick={() => this.props.handleBetray()}>
                    Betray [B]
                </Button>
            </Grid>
        </Grid>
    }
}

export default ChoiceView
