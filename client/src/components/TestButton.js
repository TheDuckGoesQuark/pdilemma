import React, {Component} from 'react';
import Button from '@material-ui/core/Button'
import Grid from '@material-ui/core/Grid'

import {testConnection} from "../PersecutorService";

class TestButton extends Component {
    handleTestConnection() {
        testConnection()
            .then(text => {
                this.props.updateResponseText(text);
            }).catch(reason => {
            this.props.updateResponseText(reason);
        });
    }

    handleGoBack() {
        this.props.goBack()
    }

    render() {
        return <Grid container spacing={16} direction="column" justify="space-between" spacing-xs-16>
            <Grid item xs m={8}>
                <Button variant="contained"
                        color="primary"
                        size="large"
                        fullWidth
                        onClick={() => this.handleTestConnection()}>Test</Button>
            </Grid>
            <Grid item xs>
                <Button
                    color="secondary"
                    size="medium"
                    onClick={() => this.handleGoBack()}>Go to Menu</Button>
            </Grid>
        </Grid>
    }
}

export default TestButton
