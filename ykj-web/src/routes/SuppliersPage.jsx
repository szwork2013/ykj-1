import React, { Component, PropTypes } from 'react';
import { connect } from 'dva';
import { Link } from 'dva/router';

import Suppliers from '../components/Suppliers';
import Add from '../components/Suppliers/Add';
import Edit from '../components/Suppliers/Edit';

import selectors from '../models/suppliers/selectors';

const SuppliersPage = props => {
  return <Suppliers {...props} />
}

const List = connect(selectors)(SuppliersPage)
const SupplierAdd = connect(selectors)(Add);
const SupplierEdit = connect(selectors)(Edit);
export {
  List as default,
  SupplierAdd,
  SupplierEdit,
}
