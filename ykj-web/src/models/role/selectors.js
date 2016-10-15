
export function querySelector(state, ownProps) {
  return state.role;
}

export function viewRoleDetailSelector(state, ownProps) {
  return {
    roleDetail: state.role.detail,
    submiting: state.role.submiting,
    permissionMap: state.permission.permissionMap,
    groups: state.group.groups,
    areaMap: state.area.areaMap,
    zoneMap: state.zone.zoneMap,
    ...ownProps
  }
}