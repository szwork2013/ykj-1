import React, { PropTypes } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Row, Col, Modal } from 'antd';

import Container from '../Container';
import Box from '../Box';
import Detail from './Detail';

const FormItem = Form.Item;
const ButtonGroup = Button.Group;
const Option = Select.Option;
const EditModal = (props) => {
	const { tracks, dispatch, ...rest } = props
  	const onSubmit = function(form) {
		form.validateFieldsAndScroll((errors, values) => {
			if (!!errors) {
				return;
			}
			const formData = form.getFieldsValue();
			dispatch({
				type: 'tracks/edit',
				payload: formData,
			})
		})
	}

	const handleCancel = function() {
		dispatch({
			type: 'tracks/toggleEditModal',
			payload: {
				showEditModal: false,
			}
		})
	}

	const handleOk = function() {
		
	}

  return (
    <Detail 
		tracks={tracks} 
		onSubmit={onSubmit}
		dispatch={dispatch} 
		handleCancel={handleCancel}
		type='edit'
	/>
  )
}

EditModal.propTypes = { 
    tracks: PropTypes.object,
}

EditModal.defaultProps = {
    tracks: {},
}

export default EditModal;
