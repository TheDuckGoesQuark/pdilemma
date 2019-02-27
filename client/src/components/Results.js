import React, {Component} from "react";
import {getAllGames} from "../PersecutorService";
import PropTypes from "prop-types";
import Grid from "@material-ui/core/Grid";
import CanvasJSReact from '../canvasjs.react';
import Button from "@material-ui/core/Button";

const CanvasJSChart = CanvasJSReact.CanvasJSChart;

class ResultView extends Component {

    constructor(props) {
        super(props);
        this.state = {results: []}
    }

    componentDidMount() {
        this.getResults();
    }

    getResults() {
        return getAllGames()
            .then(games => ResultView.countChoices(games))
            .then(count => this.setState({results: count}))
            .catch(reason => {
                this.props.updateResponseText(reason.message);
            })
    }

    static countChoices(games) {
        let cFreq = {label: "C", y: 0};
        let bFreq = {label: "B", y: 0};
        let waitingFreq = {label: "None", y:0};

        for (let i = 0; i < games.length; ++i) {
            let game = games[i];
            let prisoners = game.prisoners;
            for (let j = 0; j < prisoners.length; ++j) {
                switch (prisoners[j].choice) {
                    case "C":
                        cFreq.y++;
                        break;
                    case "B":
                        bFreq.y++;
                        break;
                    default:
                        waitingFreq.y++;
                        break;
                }
            }
        }

        return [cFreq, bFreq, waitingFreq];
    }

    render() {
        return <Grid container>
            <Results frequencies={this.state.results}/>
            <Grid item xs>
                <Button
                    color="secondary"
                    size="medium"
                    onClick={() => this.props.goBack()}>Go to Menu</Button>
            </Grid>
        </Grid>
    }
}

ResultView.propTypes = {
    updateResponseText: PropTypes.func,
    goBack: PropTypes.func
};

class Results extends Component {
    componentDidMount() {
    }

    render() {
        const datapoints = this.props.frequencies ? this.props.frequencies : [];
        const options = {
            title: {
                text: "Frequency of choices"
            },
            animationEnabled: true,
            theme: "dark2",
            data: [{
                type: "column",
                dataPoints: datapoints
            }]
        };

        return (
            <CanvasJSChart options={options}/>
        );
    }
}

Results.propTypes = {
    frequencies: PropTypes.array
};

export default ResultView;
