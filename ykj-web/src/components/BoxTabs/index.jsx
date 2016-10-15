import React, { PropTypes } from 'react';
import { Tabs } from 'antd';

import Box from '../Box';

import styles from './index.less';

const TabPane = Tabs.TabPane;
const BoxTabs = ({ className, children, ...others }) => {
  const tabs = React.cloneElement(React.Children.only(children), {
    className: `${className} ${styles.tabs}`,
  });

  return (
    <Box
      noPadding
    >
      { tabs }
    </Box>
  );
}

BoxTabs.propTypes = {
  className: PropTypes.string,
  children: PropTypes.node.isRequired,
}

export default BoxTabs;
