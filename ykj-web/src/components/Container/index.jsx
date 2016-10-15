import React, { PropTypes } from 'react';
import { Link } from 'dva/router';

import { Row, Col, Breadcrumb, Button } from 'antd';

import styles from './index.less';

const Container = ({ toolbar, children, ...rest }) => {
  return (
    <div className={ styles.container }>
      <Row
        className={ styles.breadcrumbWrapper }
      >
        <Col span={ 12 } >
          <Breadcrumb
            {...rest}
            separator=" > "
            linkRender={ (href, name, paths) => {
              return <Link to={ href }>{ name }</Link>
            } }
          />
        </Col>
        <Col span={ 12 } className={ styles['toolbar-wrapper'] } >
          { toolbar() }
        </Col>
      </Row>
      <Row>
        <Col span={ 24 } >
          { children }
        </Col>
      </Row>

    </div>
  )
}

Container.propTypes = {
  toolbar: PropTypes.func.isRequired,
  children: PropTypes.node.isRequired,
}

Container.defaultProps = {
  toolbar: () => {},
}

export default Container;
