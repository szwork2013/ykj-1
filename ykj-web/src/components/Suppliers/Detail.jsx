import React, { PropTypes } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Cascader, Spin, Row, Col } from 'antd';
import { routerRedux } from 'dva/router';

import Container from '../Container';
import Box from '../Box';

const FormItem = Form.Item;
const CheckboxGroup = Checkbox.Group;
const Option = Select.Option;
const Detail = ({ suppliers, form, type, onSubmit, moreProps, dispatch, ...rest }) => {
  const { loading } = suppliers;
  const { getFieldProps, getFieldError, isFieldValidating } = form;
  
  const supplierNameProps = getFieldProps('supplierName', {
    rules: [
      { required: true, message: '供货商名称必须填写'}
    ]
  });
  
  const contactNameProps = getFieldProps('contactName', {
    rules: [
      { required: true, message: '联系人必须填写'}
    ]
  });
  
  const contactPhoneProps = getFieldProps('contactPhone', {
    rules: [
      { required: true, message: '联系电话必须填写' },
      { pattern: /^1[0-9]{10}$/, message: '请填写有效的联系电话' },
    ]
  });
  
  const contactEmailProps = getFieldProps('contactEmail', {
    rules: [
      { type: 'email', message: '填写正确格式的电子邮箱'}
    ]
  });
  
  const contactAddressProps = getFieldProps('contactAddress', {
    rules: [
    ]
  });
  
  const remarkProps = getFieldProps('remark', {
    rules: [
    ]
  });
  
  const {  } = moreProps(getFieldProps);
  
  const rowItemLayout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 12 },
  }
  const fullRowLayout = {
    labelCol: { span: 3 },
    wrapperCol: { span: 18 },
  }
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

          <Row>
            <Col span="12">
              <FormItem
                {...rowItemLayout}
                label="供货商名称"
                hasFeedback
                help={isFieldValidating('supplierName') ? '校验中...' : (getFieldError('supplierName') || []).join(', ')}
              >
                <Input {...supplierNameProps} disabled={ suppliers.submiting } />
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem
                {...rowItemLayout}
                label="联系电话"
                hasFeedback
                help={isFieldValidating('contactPhone') ? '校验中...' : (getFieldError('contactPhone') || []).join(', ')}
              >
                <Input {...contactPhoneProps} disabled={ suppliers.submiting } />
              </FormItem>
            </Col>
          </Row>

          <Row>
            <Col span="12">
              <FormItem
                {...rowItemLayout}
                label="联系人"
                hasFeedback
                help={isFieldValidating('contactName') ? '校验中...' : (getFieldError('contactName') || []).join(', ')}
              >
                <Input {...contactNameProps} disabled={ suppliers.submiting } />
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem
                {...rowItemLayout}
                label="电子邮箱"
                hasFeedback
                help={isFieldValidating('contactEmail') ? '校验中...' : (getFieldError('contactEmail') || []).join(', ')}
              >
                <Input {...contactEmailProps} disabled={ suppliers.submiting } />
              </FormItem>
            </Col>
          </Row>

          <Row>
            <Col span="24">
              <FormItem
                {...fullRowLayout}
                label="联系地址"
                hasFeedback
                help={isFieldValidating('contactAddress') ? '校验中...' : (getFieldError('contactAddress') || []).join(', ')}
              >
                <Input {...contactAddressProps} disabled={ suppliers.submiting } />
              </FormItem>
            </Col>
          </Row>

          <Row>
            <Col span="24">
              <FormItem
                {...fullRowLayout}
                label="备注"
                hasFeedback
                help={isFieldValidating('remark') ? '校验中...' : (getFieldError('remark') || []).join(', ')}
              >
                <Input {...remarkProps}  type="textarea" rows={6} disabled={ suppliers.submiting } />
              </FormItem>
            </Col>  
          </Row>
          <FormItem wrapperCol={{ span: 20, offset: 4 }} style={{ marginTop: 24 }}>
            <Button type="primary" htmlType="submit" loading={ suppliers.submiting } onClick={ (e) => onSubmit(e, form) }>确定</Button>
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
    const supplier = props.suppliers.current;
    return {
      supplierName: {
        value: supplier.supplierName
      },
      contactName: {
        value: supplier.contactName
      },
      contactPhone: {
        value: supplier.contactPhone
      },
      contactEmail: {
        value: supplier.contactEmail
      },
      contactAddress: {
        value: supplier.contactAddress
      },
      remark: {
        value: supplier.remark
      },
      ...props.mapPropsToFields(supplier),
    }
  }
})(Detail);
