import React, { Component, PropTypes } from 'react';
import { connect } from 'dva';
import { Link } from 'dva/router';

import Codewords from '../components/Codewords';
import Add from '../components/Codewords/Add';
import Edit from '../components/Codewords/Edit';

import selectors from '../models/codewords/selectors';

const CodewordsPage = props => {
  return <Codewords {...props} />
}

const List = connect(selectors)(CodewordsPage)
const CodewordAdd = connect(selectors)(Add);
const CodewordEdit = connect(selectors)(Edit);
export {
  List as default,
  CodewordAdd,
  CodewordEdit,
}
