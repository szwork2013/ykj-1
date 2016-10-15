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
      mapPropsToFields={ (delivery) => ({}) }
      onSubmit={ (e, form) => {
        e.preventDefault();
        
        form.validateFieldsAndScroll((errors, values) => {
          if (!!errors) {
            return;
          }
        
          let formDate = form.getFieldsValue();
          formDate.id = props.params.id[1];
          dispatch({
            type: 'deliverys/edit',
            payload: formDate
          })
          
        });

      } }
    />
  )
}

export default Edit;
