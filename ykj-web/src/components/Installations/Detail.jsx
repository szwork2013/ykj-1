import React, { PropTypes } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Cascader, Spin, Row, Col, Upload, Icon, DatePicker, InputNumber } from 'antd';
import { routerRedux } from 'dva/router';

import Container from '../Container';
import Box from '../Box';
import OrderCustomerInfo from '../CustomerInfo/OrderCustomerInfo';
import InstallGoods from './InstallGoods';

const FormItem = Form.Item;
const CheckboxGroup = Checkbox.Group;
const Option = Select.Option;

const Detail = (props) => {

  const { installations, installGoods, customers, form, type, onSubmit, moreProps, dispatch, ...rest } = props;

  const { loading } = installations;
  const { getFieldProps, getFieldError, isFieldValidating, setFieldsValue } = form;
  const installation = installations.current
  const customer = customers.current

  const fileList = installation.attachmentId ? 
  [
    {
      uid: -1,
      name: 'xxx.png',
      status: 'done',
      url: installation.attachmentId,
      thumbUrl: 'https://os.alipayobjects.com/rmsportal/NDbkJhpzmLxtPhB.png',
    }
  ]
  :
  []
  
  const nameProps = getFieldProps('name', {
    rules: [
      { required: true, message: '测量名称必须填写'}
    ]
  });
  
  const clerkIdProps = getFieldProps('clerkId', {
    rules: [
      { required: true, message: '服务人员必须选择'}
    ]
  });

  const clerkPhoneProps = getFieldProps('clerkPhone', {
    rules: [
      { pattern: /^1[0-9]{10}$/, message: '请填写有效手机号' },
    ]
  });
  
  const attachmentIdProps = getFieldProps('attachmentId', {
    rules: [
    ],
    valuePropName: 'fileList',
    normalize: (e) => {
      if (Array.isArray(e)) {
        return e;
      }
      return e && e.fileList;
    },
    onRemove: () => {},
    onPreview: () => {},
  });
  
  const needTimeProps = getFieldProps('needTime', {
    rules: [
      { required: true, message: '要求时间必须填写'}
    ]
  });
  
  const remarkProps = getFieldProps('remark', {
    rules: [
    ]
  });
  
  const costProps = getFieldProps('cost', {
    rules: [
      { required: true, type: 'number', message: '费用必须填写'}
    ]
  });
  
  const servicePositionProps = getFieldProps('servicePosition', {
    rules: [
    ]
  });

  const isFinishProps = getFieldProps('isFinish', {
    rules: [
    ]
  });

  const isSignProps = getFieldProps('isSign', {
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
          <h3>订单客户资料</h3>
          <br/>
          <OrderCustomerInfo
            dispatch={dispatch}
            options={customer}
          />
          <h3>安装安排详情</h3>
          <br/>
          <Box>
            <Row>
              <Col span="12">
                <FormItem
                  {...rowItemLayout}
                  label="业主要求时间"
                  hasFeedback
                  help={isFieldValidating('name') ? '校验中...' : (getFieldError('name') || []).join(', ')}
                >
                  <Input {...nameProps} disabled={ installations.submiting } />
                </FormItem>
              </Col>
              <Col span="12">
                <FormItem
                  {...rowItemLayout}
                  label="安装师傅"
                  hasFeedback
                  help={isFieldValidating('needTime') ? '校验中...' : (getFieldError('needTime') || []).join(', ')}
                >
                  <DatePicker {...needTimeProps} disabled={ installations.submiting } style={{width: '100%'}} 
                    onChange={(date, dateString) => {
                      setFieldsValue({
                        needTime: dateString
                      })
                    }}
                  />
                </FormItem>
              </Col>
            </Row>
            <Row>
              <Col span="12">
                <FormItem
                  {...rowItemLayout}
                  label="师傅联系电话"
                  hasFeedback
                  help={isFieldValidating('clerkId') ? '校验中...' : (getFieldError('clerkId') || []).join(', ')}
                >
                  <Input {...clerkIdProps} disabled={ installations.submiting } />
                </FormItem>
              </Col>
              <Col span="12">
                <FormItem
                {...rowItemLayout}
                label="订单产品数"
                hasFeedback
                help={isFieldValidating('clerkPhone') ? '校验中...' : (getFieldError('clerkPhone') || []).join(', ')}
              >
                <Input {...clerkPhoneProps} disabled={ installations.submiting } />
              </FormItem>
              </Col>
            </Row>
            <Row>
              <Col span="12">
                <FormItem
                  {...rowItemLayout}
                  label="服务费"
                  hasFeedback
                  help={isFieldValidating('cost') ? '校验中...' : (getFieldError('cost') || []).join(', ')}
                >
                  
                </FormItem>
              </Col>
              <Col span="12">
                <FormItem
                  {...rowItemLayout}
                  label="服务位置"
                  hasFeedback
                  help={isFieldValidating('servicePosition') ? '校验中...' : (getFieldError('servicePosition') || []).join(', ')}
                >
                  <Input {...servicePositionProps} disabled={ installations.submiting } />
                </FormItem>
              </Col>
            </Row>
            <Row>
              <Col span="24">
                <FormItem
                  {...fullRowLayout}
                  label="安装说明"
                  hasFeedback
                  help={isFieldValidating('remark') ? '校验中...' : (getFieldError('remark') || []).join(', ')}
                >
                  <Input type='textarea' rows={6} {...remarkProps} />
                </FormItem>
              </Col>
            </Row>
            <Row>
              <Col span="24">
                <FormItem
                  {...fullRowLayout}
                  label="附件"
                  hasFeedback
                  help={isFieldValidating('attachmentId') ? '校验中...' : (getFieldError('attachmentId') || []).join(', ')}
                >
                  <Upload 
                    {...attachmentIdProps}
                    showUploadList={ false }
                    fileList={fileList}
                    beforeUpload={file => {
                    }}
                  >
                    <Button type="ghost">
                      <Icon type="upload" /> 点击上传
                    </Button>
                  </Upload>
                </FormItem>
              </Col>
            </Row>
            <Row>
              <Col span="24">
                <FormItem
                  {...fullRowLayout}
                  label="安装标记"
                  hasFeedback
                  help={isFieldValidating('attachmentId') ? '校验中...' : (getFieldError('attachmentId') || []).join(', ')}
                >
                  <Checkbox defaultChecked={installation.isSign || false} {...isSignProps}
                    onChange={(e) => {
                      setFieldsValue({
                        isSign: e.target.checked
                      })
                    }}
                  >已签到</Checkbox>
                  <Checkbox defaultChecked={installation.isFinish || false} {...isFinishProps}
                    onChange={(e) => {
                      setFieldsValue({
                        isFinish: e.target.checked
                      })
                    }}
                  >已完成</Checkbox>
                </FormItem>
              </Col>
            </Row>
          </Box>
          <FormItem wrapperCol={{ span: 20, offset: 4 }} style={{ marginTop: 24 }}>
            <Button type="primary" htmlType="submit" loading={ installations.submiting } onClick={ (e) => onSubmit(e, form) }>确定</Button>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <Button type="ghost" onClick={ () => dispatch(routerRedux.goBack()) }>返回</Button>
          </FormItem>
        </Form>
        <InstallGoods
            {...props}
            onSubmit={e => {
              e.preventDefault();
              let formData = form.getFieldsValue();
              if (type === 'edit') {
                let goods = installGoods;
                goods.deliveryId = delivery.id
                dispatch({
                  type: 'installGoods/edit',
                  payload: goods
                })
              } else {
                
                form.validateFieldsAndScroll((errors, values) => {
                  if (!!errors) {
                    return;
                  }
                  formData.installGoods = installGoods;
                  dispatch({
                    type: 'deliverys/add',
                    payload: formData
                  })  
                });
              }
            }}
          >
          </InstallGoods>
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
    const installation = props.installations.current;
    return {
      name: {
        value: installation.name
      },
      clerkId: {
        value: installation.clerkId
      },
      attachmentId: {
        value: installation.attachmentId
      },
      needTime: {
        value: installation.needTime
      },
      remark: {
        value: installation.remark
      },
      cost: {
        value: installation.cost
      },
      servicePosition: {
        value: installation.servicePosition
      },
      ...props.mapPropsToFields(installation),
    }
  }
})(Detail);
