import React, { PropTypes } from 'react';
import { Row, Col, Button } from 'antd';
import { Link } from 'dva/router';
const ButtonGroup = Button.Group;

import styles from './index.less';

const OperationBox = ({ options = {}, ...others }) => {
  
  const filter = options.filter || [];
  const operation = options.operation || [];

  const renderFilter = function() {
      return filter.map((item, index) => {
          return (
              <a href={item.url} key={index}>{item.label} {item.number ? `(${item.number})` : ''}</a>
          )
      })
  }

  const renderOperation = function() {
      return operation.map((item, index) => {
          return (
              <Button className={styles['operation-btn']} key={index}>
                <Link to={item.url}>{item.label}</Link>
              </Button>
          )
      })
  }

  const filterBox = (<div className={styles['left']}>{ renderFilter() }</div>)
  const operationBox = (<ButtonGroup className={styles['right']}>{ renderOperation() }</ButtonGroup>)

  return (
    <div className={styles['operationBox']} {...others}>
      {filterBox}
      {operationBox}
    </div>
  )
}

OperationBox.propTypes = {
}

export default OperationBox;
