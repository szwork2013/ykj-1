import React, { PropTypes } from 'react';

import { Table } from 'antd';

import styles from './index.less';

const PagedTable = props => {
  const { pagination, ...others } = props;
  return (
    <Table
      {...others}

      pagination={
        {
          ...pagination,
          showTotal: total => `共 ${total} 条`,
          showQuickJumper: true,
        }
      }
    />
  );
}

export default PagedTable;
