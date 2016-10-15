import React, { Component, PropTypes } from 'react';
import { connect } from 'dva';
import { Link } from 'dva/router';

import ${moduleName}s from '../components/${moduleName}s';
import Add from '../components/${moduleName}s/Add';
import Edit from '../components/${moduleName}s/Edit';

import selectors from '../models/${_moduleName}s/selectors';

const ${moduleName}sPage = props => {
  return <${moduleName}s {...props} />
}

const List = connect(selectors)(${moduleName}sPage)
const ${moduleName}Add = connect(selectors)(Add);
const ${moduleName}Edit = connect(selectors)(Edit);
export {
  List as default,
  ${moduleName}Add,
  ${moduleName}Edit,
}
