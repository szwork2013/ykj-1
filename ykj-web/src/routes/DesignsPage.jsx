import React, { Component, PropTypes } from 'react';
import { connect } from 'dva';
import { Link } from 'dva/router';

import Designs from '../components/Designs';
import Add from '../components/Designs/Add';
import Edit from '../components/Designs/Edit';

import selectors from '../models/designs/selectors';

const DesignsPage = props => {
  return <Designs {...props} />
}

const List = connect(selectors)(DesignsPage)
const DesignAdd = connect(selectors)(Add);
const DesignEdit = connect(selectors)(Edit);
export {
  List as default,
  DesignAdd,
  DesignEdit,
}
