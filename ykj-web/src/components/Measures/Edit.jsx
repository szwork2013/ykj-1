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
      mapPropsToFields={ (measure) => ({}) }
      onSubmit={ (e, form) => {
        e.preventDefault();
        
        form.validateFieldsAndScroll((errors, values) => {
          if (!!errors) {
            return;
          }
          let formData = form.getFieldsValue();
          formData.id = props.params.id[1];
          formData.orderId = props.params.id[0];
          dispatch({
            type: 'measures/edit',
            payload: formData,
          })
		  
		    });

      } }
    />
  )
}

export default Edit;
