import React, { Component, PropTypes } from 'react';
import { connect } from 'dva';
import { Link, routerRedux } from 'dva/router';

import { Row, Col } from 'antd';

import Header from '../components/Header';
import GuideMenu from '../components/GuideMenu';
import Footer from '../components/Footer';

import styles from './IndexPage.less';

class IndexPage extends Component {
  componentWillMount() {
    const { oauth } = this.props;
    if (!oauth.authentication) {
      this.context.router.replace('/login');
      return false;
    }
  }

  render() {
    const { app, dispatch, children } = this.props;
    return (
      <div>
        <Header/>
        <Row>
          <Col span={ 4 }>
            <GuideMenu menuOpenKeys={ app.menu_open_keys } menuCurrent={ app.menu_current } dispatch={ dispatch } />
          </Col>
          <Col span={ 20 }>
            {
              children
            }
          </Col>
        </Row>
        <Footer/>
      </div>
    );
  }
}

IndexPage.propTypes = {
  children: PropTypes.node.isRequired,
};

IndexPage.contextTypes = {
  router: PropTypes.object.isRequired,
}

function mapStateToProps(state) {
  return {
    app: state.app,
    oauth: state.oauth
  };
}

export default connect(mapStateToProps)(IndexPage);
