import React, { PropTypes } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Cascader, Spin } from 'antd';
import { routerRedux } from 'dva/router';

import Container from '../Container';
import Box from '../Box';

const FormItem = Form.Item;
const CheckboxGroup = Checkbox.Group;
const Option = Select.Option;
const Detail = ({ ${_moduleName}s, form, type, onSubmit, moreProps, dispatch, ...rest }) => {
  const { loading } = ${_moduleName}s;
  const { getFieldProps, getFieldError, isFieldValidating } = form;

  <#list records as record>
  <#if !moreFields?seq_contains(record.COLUMN_NAME)>
  const ${record.COLUMN_CAMEL_NAME}Props = getFieldProps('${record.COLUMN_CAMEL_NAME}', {
    rules: [
      <#if record.IS_NULLABLE == 'NO'>
      { required: true, message: '${record.COLUMN_COMMENT}必须填写'}
      </#if>
    ]
  });
  
  </#if>
  </#list>
  const { <#list records as record><#if moreFields?seq_contains(record.COLUMN_NAME)>${record.COLUMN_CAMEL_NAME}Props = {}<#if record_has_next>,</#if> </#if></#list> } = moreProps(getFieldProps);
  
  const formItemLayout = {
    labelCol: { span: 4 },
    wrapperCol: { span: 5 },
  };
  return (
    <Container
      {...rest}
    >
    <Spin spinning={ loading }>
      <Box
      >
        <Form horizontal >
          <h3>基本资料</h3>
          <br/>
          <#list records as record>
          <#if !moreFields?seq_contains(record.COLUMN_NAME)>
          <FormItem
            {...formItemLayout}
            label="${record.COLUMN_COMMENT}"
            hasFeedback
            help={isFieldValidating('${record.COLUMN_CAMEL_NAME}') ? '校验中...' : (getFieldError('${record.COLUMN_CAMEL_NAME}') || []).join(', ')}
          >
            <Input {...${record.COLUMN_CAMEL_NAME}Props} disabled={ ${_moduleName}s.submiting } />
          </FormItem>
          </#if>
          </#list>
          <FormItem wrapperCol={{ span: 20, offset: 4 }} style={{ marginTop: 24 }}>
            <Button type="primary" htmlType="submit" loading={ ${_moduleName}s.submiting } onClick={ (e) => onSubmit(e, form) }>确定</Button>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <Button type="ghost" onClick={ () => dispatch(routerRedux.goBack()) }>返回</Button>
          </FormItem>
        </Form>
      </Box>
    </Spin>
    </Container>
  )
}

Detail.propTypes = {
  onSubmit: PropTypes.func.isRequired,
  moreProps: PropTypes.func.isRequired,
  mapPropsToFields: PropTypes.func.isRequired,
}

Detail.defaultProps = {
  onSubmit: () => {},
  moreProps: () => ({}),
  mapPropsToFields: () => ({}),
}

export default Form.create({
  mapPropsToFields: (props) => {
    const ${_moduleName} = props.${_moduleName}s.current;
    return {
      <#list records as record>
      ${record.COLUMN_CAMEL_NAME}: {
        value: ${_moduleName}.${record.COLUMN_CAMEL_NAME}
      },
      </#list>
      ...props.mapPropsToFields(${_moduleName}),
    }
  }
})(Detail);
