import React, {Component} from "react";
import Button from '@material-ui/core/Button'
import Grid from '@material-ui/core/Grid'
import {makeChoice} from "../PersecutorService";

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

export default ChoiceButtons
