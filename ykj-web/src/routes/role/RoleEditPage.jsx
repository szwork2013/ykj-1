import React from 'react'

import { connect } from 'dva';
import { viewRoleDetailSelector } from '../../models/role/selectors'
import RoleDetail from '../../components/Role/Detail'

const RoleEditPage = (props) => {
  return (
    <RoleDetail {... props} type="update"/>
  )
}

export default connect(viewRoleDetailSelector)(RoleEditPage)
