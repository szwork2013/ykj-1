import React, { PropTypes } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Row, Col, Modal } from 'antd';

import Container from '../Container';
import Box from '../Box';
import Detail from './Detail';

import styles from './index.less';

const FormItem = Form.Item;
const ButtonGroup = Button.Group;
const Option = Select.Option;
const AddModal = ({ tracks, dispatch, ...rest }) => {
  
	const onSubmit = function(form) {
		form.validateFieldsAndScroll((errors, values) => {
			if (!!errors) {
				return;
			}
			const formData = form.getFieldsValue();
			dispatch({
				type: 'tracks/add',
				payload: formData,
			})
		})
	}

	const handleCancel = function() {
		dispatch({
			type: 'tracks/toggleAddModal',
			payload: false
		})
	}
  
  return (
		<Detail 
			tracks={tracks} 
			onSubmit={onSubmit}
			handleCancel={handleCancel}
			dispatch={dispatch} 
			type='add'
		/>
  )
}

AddModal.propTypes = { 
    tracks: PropTypes.object,
}

AddModal.defaultProps = {
    tracks: {},
}

export default AddModal;
