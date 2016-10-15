import React, { PropTypes } from 'react';

import { Row, Col, Menu, Icon } from 'antd';
import { Link } from 'dva/router';

import styles from './index.less';

const Header = props => {
  return (
    <header className={ styles['ant-layout-header'] }>
      <Row>
        <Col span={4}>
          <a className={ styles.logo } href="javascript:;"><span>银空间</span></a>
        </Col>
        <Col span={10}>
          <Menu
            className={ styles['header-menu'] + ' ' + styles['left-header-menu'] }
            mode="horizontal"
            selectedKeys={ ['home'] }
          >
            <Menu.Item key="home">
              <Link to="/stores"><Icon type="home" />你的门店</Link>
            </Menu.Item>
            <Menu.Item key="tablet">
              <Link to="/workbench"><Icon type="tablet" />个人工作台</Link>
            </Menu.Item>
          </Menu>
        </Col>
        <Col span={10}>
          <Menu
            className={ styles['header-menu'] + ' ' + styles['right-header-menu'] }
            mode="horizontal"
          >
            <Menu.SubMenu title={ <Icon className="userIcon" type="user" /> }>
              <Menu.Item key="setting:1">选项1</Menu.Item>
              <Menu.Item key="setting:2">选项2</Menu.Item>
              <Menu.Divider />
              <Menu.Item key="setting:3">注销</Menu.Item>
            </Menu.SubMenu>
            <Menu.Item key="mail">
              帮助
            </Menu.Item>
            <Menu.Item key="notification">
              <Icon type="notification" />
            </Menu.Item>
          </Menu>
        </Col>
      </Row>
    </header>
  )
}

export default Header;
