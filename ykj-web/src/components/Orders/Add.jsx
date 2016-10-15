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
      mapPropsToFields={ (order) => ({}) }
      onSubmit={ (e, form, orderGoods) => {
        e.preventDefault();
        console.log(orderGoods);
        form.validateFieldsAndScroll((errors, values) => {
          if (!!errors) {
            return;
          }
		  
          const formData = form.getFieldsValue();
          dispatch({
            type: 'orders/add',
            payload: formData,
          })
		  
        });
		
      } }
    />
  )
}

export default Add;
