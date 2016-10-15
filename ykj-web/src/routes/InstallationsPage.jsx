import React, { Component, PropTypes } from 'react';
import { connect } from 'dva';
import { Link } from 'dva/router';

import Installations from '../components/Installations';
import Add from '../components/Installations/Add';
import Edit from '../components/Installations/Edit';
import Charges from '../components/Installations/Charges';
import InstallNote from '../components/Installations/InstallNote';

import selectors from '../models/installations/selectors';

const InstallationsPage = props => {
  return <Installations {...props} />
}

const List = connect(selectors)(InstallationsPage)
const InstallationAdd = connect(selectors)(Add);
const InstallationEdit = connect(selectors)(Edit);
const InstallationCharges = connect(selectors)(Charges);
const InstallationNote = connect(selectors)(InstallNote);
export {
  List as default,
  InstallationAdd,
  InstallationEdit,
  InstallationCharges,
  InstallationNote,
}
