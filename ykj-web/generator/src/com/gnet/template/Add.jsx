import React, { PropTypes } from 'react';

import Detail from './Detail';

const Add = (props) => {
  const { dispatch } = props;
  return (
    <Detail
      {...props}

      type="add"
      moreProps={ (getFieldProps) => {
        return {<#list records as record><#if addFields?seq_contains(record.COLUMN_NAME)>
          ${record.COLUMN_CAMEL_NAME}Props: getFieldProps('${record.COLUMN_CAMEL_NAME}', {
            rules: [
              { required: true, message: '${record.COLUMN_COMMENT}必须填写'}
            ]
          }),
          </#if>
          </#list>
        }
      } }
      mapPropsToFields={ (${_moduleName}) => ({}) }
      onSubmit={ (e, form) => {
        e.preventDefault();
        
        form.validateFieldsAndScroll((errors, values) => {
          if (!!errors) {
            return;
          }
		  
          const formData = form.getFieldsValue();
          dispatch({
            type: '${_moduleName}s/add',
            payload: formData,
          })
		  
        });
		
      } }
    />
  )
}

export default Add;
