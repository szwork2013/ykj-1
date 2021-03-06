import React, { PropTypes } from 'react';

import Detail from './Detail';

const Add = (props) => {
  const { dispatch } = props;
  return (
    <Detail
      {...props}

      type="add"
      moreProps={ (getFieldProps) => {
        return {        }
      } }
      mapPropsToFields={ (design) => ({}) }
      onSubmit={ (e, form) => {
        e.preventDefault();
        
        form.validateFieldsAndScroll((errors, values) => {
          if (!!errors) {
            return;
          }
		  
          const formData = form.getFieldsValue();
          dispatch({
            type: 'designs/add',
            payload: formData,
          })
		  
        });
		
      } }
    />
  )
}

export default Add;
