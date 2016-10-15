import React, { PropTypes } from 'react';
import Detail from './Detail';

const Edit = (props) => {
	const { dispatch } = props;
  	return (
  		<Detail 
		   	{...props} 
		   	onSubmit = { (e, form) => {
				e.preventDefault();
				form.validateFieldsAndScroll((errors, values) => {
					if (!!errors) {
						return;
					}
					const customer = props.customers.current;
					let formData = form.getFieldsValue();
					formData.id = props.params.id;
					formData.name = customer.name;
					formData.customerResponsibleId = props.customers.customerResponsibleId
					dispatch({
						type: 'customers/update',
						payload: formData,
					})
				})
	      	}}

	    />
  	)
}
export default Edit;
