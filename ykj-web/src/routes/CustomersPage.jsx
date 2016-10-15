import React, { Component, PropTypes } from 'react';
import { connect } from 'dva';
import { Link } from 'dva/router';

import Customers from '../components/Customers';
import Add from '../components/Customers/Add';
import Edit from '../components/Customers/Edit';
import Houses from '../components/Houses';
import Tracks from '../components/Tracks';

import selectors from '../models/customers/selectors';

const CustomersPage = props => {
  return <Customers {...props} />
}

const List = connect(selectors)(CustomersPage)
const CustomerAdd = connect(selectors)(Add)
const CustomerEdit = connect(selectors)(Edit)
const CustomerHouses = connect(selectors)(Houses)
const CustomerTracks = connect(selectors)(Tracks)
export {
  List as default,
  CustomerAdd,
  CustomerEdit,
  CustomerHouses,
  CustomerTracks,
}
