import React, { Component, PropTypes } from 'react';
import { connect } from 'dva';
import { Link } from 'dva/router';

import Deliverys from '../components/Deliverys';
import Add from '../components/Deliverys/Add';
import Edit from '../components/Deliverys/Edit';
import Note from '../components/Deliverys/DeliveryNote';

import selectors from '../models/deliverys/selectors';

const DeliverysPage = props => {
  return <Deliverys {...props} />
}

const List = connect(selectors)(DeliverysPage)
const DeliveryAdd = connect(selectors)(Add);
const DeliveryEdit = connect(selectors)(Edit); 
const DeliveryNote = connect(selectors)(Note);
export {
  List as default,
  DeliveryAdd,
  DeliveryEdit,
  DeliveryNote,
}
