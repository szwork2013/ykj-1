import React, { Component, PropTypes } from 'react';
import { connect } from 'dva';
import { Link, routerRedux } from 'dva/router';

import selectors from '../models/login/selectors';

class LoginPage extends Component {
  componentWillMount() {
    const { oauth } = this.props;
    if (oauth.authentication) {
      this.context.router.replace('/');
      return false;
    }
  }

  componentWillReceiveProps(nextProps) {
    const { oauth } = nextProps;
    if (oauth.authentication) {
      this.context.router.replace('/');
      return false;
    }
  }

  render() {
    const { login, dispatch } = this.props;

    return (
      <div >
        <input name="username" onChange={ (e) => dispatch({ type: 'login/setUsername', payload: e.target.value }) } />
        <input name="password" type="password" onChange={ (e) => dispatch({ type: 'login/setPassword', payload: e.target.value }) } />
        <input type="button" onClick={ () => dispatch({
          type: 'login/submit',
          payload: {
            username: login.username,
            password: login.password,
          },
        }) } value="submit" />
      </div>
    );
  }
}

LoginPage.contextTypes = {
  router: PropTypes.object.isRequired,
}

export default connect(selectors)(LoginPage);
