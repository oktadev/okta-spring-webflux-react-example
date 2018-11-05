import React, { Component } from 'react';
import './App.css';
import { withAuth } from '@okta/okta-react';
import { Auth } from './App';
import logo from './logo.svg';
import ProfileList from './ProfileList';

interface HomeProps {
  auth: Auth;
}

interface HomeState {
  authenticated: boolean;
}

class Home extends Component<HomeProps, HomeState> {
  constructor(props: HomeProps) {
    super(props);
    this.state = {authenticated: false};
    this.checkAuthentication = this.checkAuthentication.bind(this);
    this.login = this.login.bind(this);
    this.logout = this.logout.bind(this);
  }

  async checkAuthentication() {
    const authenticated = await this.props.auth.isAuthenticated();
    if (authenticated !== this.state.authenticated) {
      this.setState({authenticated});
    }
  }

  async componentDidMount() {
    await this.checkAuthentication();
  }

  async componentDidUpdate() {
    await this.checkAuthentication();
  }

  async login() {
    this.props.auth.login('/')
  }

  async logout() {
    this.props.auth.logout('/');
  }

  render() {
    const {authenticated} = this.state;
    let body = null;
    if (authenticated) {
      body = (
        <div className="Buttons">
          <button onClick={this.logout}>Logout</button>
          <p>
            <ProfileList auth={this.props.auth}/>
          </p>
        </div>
      );
    } else {
      body = (
        <div className="Buttons">
          <button onClick={this.login}>Login</button>
        </div>
      );
    }

    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo"/>
          <h1 className="App-title">Welcome to React</h1>
          {body}
        </header>
      </div>
    );
  }
}

export default withAuth(Home);