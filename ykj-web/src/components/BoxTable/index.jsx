import React, { PropTypes } from 'react';

import PagedTable from '../PagedTable';

import Box from '../Box';

import styles from './index.less';

const BoxList = props => {
  const { title, boxClassName, children, ...others } = props;
  return (
    <Box
      noPadding
      title={ title }
      className={ boxClassName }
    >
      <PagedTable
        {...others}
      />
    </Box>
  );
}

export default BoxList;
