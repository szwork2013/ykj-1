import React, { Component, PropTypes } from 'react';
import { connect } from 'dva';
import { Link } from 'dva/router';

import Measures from '../components/Measures';
import Add from '../components/Measures/Add';
import Edit from '../components/Measures/Edit';

import selectors from '../models/measures/selectors';

const MeasuresPage = props => {
  return <Measures {...props} />
}

const List = connect(selectors)(MeasuresPage)
const MeasureAdd = connect(selectors)(Add);
const MeasureEdit = connect(selectors)(Edit);
export {
  List as default,
  MeasureAdd,
  MeasureEdit,
}
