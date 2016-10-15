import React, { PropTypes } from 'react';

import styles from './index.less';

const Footer = props => {
  return (
    <footer className={ styles.footer }>
      <p>@Copyright 杭州高网信息技术有限公司 --- { new Date().toLocaleDateString() }</p>
    </footer>
  );
}

Footer.propTypes = {
}

export default Footer;
