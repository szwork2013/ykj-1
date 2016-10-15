import React, { PropTypes } from 'react';
import { Row, Col } from 'antd';

import styles from './index.less';

const Box = ({ title, className, noPadding, toolbar, renderTitle, children, ...others }) => {
  const head = title ? (
    <div className={ styles['box-head'] } >
      <Row>
        <Col span={ 12 }>
          {
            title ?
            <h3 className={ styles['box-head-title'] }>{ title }</h3>
            :
            (
              renderTitle ?
              renderTitle()
              :
              false
            )
            
          }
        </Col>
        <Col span={ 12 } className={ styles['toolbar-wrapper'] } >
          { toolbar() }
        </Col>
      </Row>
    </div>
  ) : null;

  return (
    <div {...others} className={ styles.box + ' ' + `${ noPadding ? styles['no-padding'] : '' }` + ' ' + className }>
      {head}
      <div className={ styles['box-body'] } >{ children }</div>
    </div>
  )
}

Box.propTypes = {
  title: PropTypes.string,
  className: PropTypes.string,
  noPadding: PropTypes.bool.isRequired,
  toolbar: PropTypes.func.isRequired,
  renderTitle: PropTypes.func,
  children: PropTypes.node,
}

Box.defaultProps = {
  noPadding: false,
  toolbar: () => {},
}

export default Box;
