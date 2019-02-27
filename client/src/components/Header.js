import {Component} from "react";
import logo from '../logo.svg';
import React from "react";

class Header extends Component {
    render() {
        return (
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <p>{this.props.text}</p>
            </header>
        )
    }
}

export default Header
