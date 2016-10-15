import React, { PropTypes } from 'react';

import Detail from './Detail';

const Edit = (props) => {
  const { dispatch } = props;
  return (
    <Detail 
      {...props}
      
      type="edit"

      onSubmit={ (e, form) => {
        e.preventDefault();
        
        form.validateFields((errors, values) => {
          if (errors) {
            return;
          }
          const formData = form.getFieldsValue();
          formData.id = props.params.id;
          console.log(formData);
          dispatch({
              type: 'users/update',
              payload: formData,
          })  
		});
      } }

    />
  )
}

export default Edit;
