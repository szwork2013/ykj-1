import React, { PropTypes } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Row, Col, Modal } from 'antd';

import Container from '../Container';
import Box from '../Box';
import Detail from './Detail';

import styles from './index.less';

const FormItem = Form.Item;
const ButtonGroup = Button.Group;
const Option = Select.Option;
const Add = ({ house, dispatch, visible, ...rest }) => {
  
	const onSubmit = function(form) {
	  const formData = form.getFieldsValue();
	  dispatch({
	    	type: 'houses/add',
	    	payload: formData,
	  })
	}
  
  return (
    <div>
    	<Detail 
    		house={house} 
    		onSubmit={onSubmit}
    		visible={visible} 
    		dispatch={dispatch} 
    		type='add'
  		/>
    </div>
  )
}

Add.propTypes = { 
    house: PropTypes.object,
}

Add.defaultProps = {
    house: {},
}

export default Add;
