import React, { PropTypes } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Row, Col } from 'antd';

import Container from '../Container';
import Box from '../Box';
import Detail from './Detail';

const FormItem = Form.Item;
const ButtonGroup = Button.Group;
const Option = Select.Option;
const Edit = ({ house, dispatch, ...rest }) => {
  
  const onSubmit = function(e, form) {
		e.preventDefault();
	  let formData = form.getFieldsValue();
	  formData.id = house.id;
	  dispatch({
	    	type: 'houses/edit',
	    	payload: formData,
	  })
	}

  return (
    <Box>	
			<Detail 
				house={house} 
				onSubmit={onSubmit}
				dispatch={dispatch}
				type='edit'
			/>
    </Box>
  )
}

Edit.propTypes = { 
    house: PropTypes.object,
}

Edit.defaultProps = {
    house: {},
}

export default Edit;
