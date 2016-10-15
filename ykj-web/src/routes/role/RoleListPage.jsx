import React, { PropTypes } from 'react';
import { connect } from 'dva';
import { querySelector } from '../../models/role/selectors'
import RoleList from '../../components/Role'

const RoleListPage = (props) => {
  return (
    <RoleList {... props}/>
  )
}

export default connect(querySelector)(RoleListPage);
