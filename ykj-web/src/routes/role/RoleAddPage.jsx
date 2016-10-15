import React from 'react'

import { connect } from 'dva';
import { viewRoleDetailSelector } from '../../models/role/selectors'
import RoleDetail from '../../components/Role/Detail'

const RoleAddPage = (props) => {
  return (
    <RoleDetail {... props} type="save"/>
  )
}

export default connect(viewRoleDetailSelector)(RoleAddPage)
