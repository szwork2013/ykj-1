import React, { PropTypes } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Cascader, Spin } from 'antd';
import { routerRedux } from 'dva/router';

import Container from '../Container';
import Box from '../Box';

const FormItem = Form.Item;
const CheckboxGroup = Checkbox.Group;
const Option = Select.Option;
const Detail = ({ codewords, form, type, onSubmit, moreProps, dispatch, ...rest }) => {
  const { loading } = codewords;
  const { getFieldProps, getFieldError, isFieldValidating } = form;
  
  const valueProps = getFieldProps('value', {
    rules: [
      { required: true, message: '请输入数据字典的值' }
    ]
  });
  
  
  const {  } = moreProps(getFieldProps);
  
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
          <FormItem
            {...formItemLayout}
            label="数据值"
            hasFeedback
            help={isFieldValidating('value') ? '校验中...' : (getFieldError('value') || []).join(', ')}
          >
            <Input {...valueProps} disabled={ codewords.submiting } />
          </FormItem>
          <FormItem wrapperCol={{ span: 20, offset: 4 }} style={{ marginTop: 24 }}>
            <Button type="primary" htmlType="submit" loading={ codewords.submiting } onClick={ (e) => onSubmit(e, form) }>确定</Button>
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
    const codeword = props.codewords.current;
    return {
      value: {
        value: codeword.value
      },
      ...props.mapPropsToFields(codeword),
    }
  }
})(Detail);
