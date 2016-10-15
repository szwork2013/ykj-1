import React, { Component, PropTypes } from 'react';
import { connect } from 'dva';
import { Link } from 'dva/router';

import User from '../components/Users';
import Add from '../components/Users/Add';
import Edit from '../components/Users/Edit';

import selectors from '../models/users/selectors';

const UserPage = props => {
    return <User {...props } />
}

const List = connect(selectors)(UserPage);
const UserAdd = connect(selectors)(Add);
const UserEdit = connect(selectors)(Edit);

export {
    List as default,
    UserAdd,
    UserEdit,
}
