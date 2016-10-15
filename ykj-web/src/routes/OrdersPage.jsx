import React, { Component, PropTypes } from 'react';
import { connect } from 'dva';
import { Link } from 'dva/router';

import Orders from '../components/Orders';
import Add from '../components/Orders/Add';
import Edit from '../components/Orders/Edit';
import Print from '../components/Orders/Print';
import EnterOut from '../components/Orders/EnterOut';
import OverOrder from '../components/Orders/OverOrder';

import selectors from '../models/orders/selectors';

const OrdersPage = props => {
  return <Orders {...props} />
}

const List = connect(selectors)(OrdersPage)
const OrderAdd = connect(selectors)(Add);
const OrderEdit = connect(selectors)(Edit);
const OrderPrint = connect(selectors)(Print);
const OrderEnterOut = connect(selectors)(EnterOut);
const OverOrderPage = connect(selectors)(OverOrder);

export {
  List as default,
  OrderAdd,
  OrderEdit,
  OrderPrint,
  OrderEnterOut,
  OverOrderPage,
}
