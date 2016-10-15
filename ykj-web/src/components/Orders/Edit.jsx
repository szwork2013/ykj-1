import React, { PropTypes } from 'react';

import EditDetail from './EditDetail';

const Edit = (props) => {
  const { dispatch } = props;
  return (
    <EditDetail
      {...props}

      type="edit"
      moreProps={ (getFieldProps) => {
        return {        }
      } }
      mapPropsToFields={ (order) => ({}) }
      onSubmit={ (e, form) => {
        e.preventDefault();
        
        form.validateFieldsAndScroll((errors, values) => {
		  if (!!errors) {
		  	return;
		  }
		  
		  const formData = form.getFieldsValue();
          dispatch({
            type: 'orders/edit',
            payload: formData,
          })
		  
		});

      } }
    />
  )
}

export default Edit;
