import React, { PropTypes } from 'react';

import Detail from './Detail';

const Edit = (props) => {
  const { dispatch } = props;
  return (
    <Detail
      {...props}

      type="edit"
      moreProps={ (getFieldProps) => {
        return {        }
      } }
      mapPropsToFields={ (codeword) => ({}) }
      onSubmit={ (e, form) => {
        e.preventDefault();
        
        form.validateFieldsAndScroll((errors, values) => {
		  if (!!errors) {
		  	return;
		  }
		  
		  const formData = form.getFieldsValue();
          dispatch({
            type: 'codewords/edit',
            payload: formData,
          })
		  
		});

      } }
    />
  )
}

export default Edit;
