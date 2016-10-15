import React, { PropTypes } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Cascader, Spin, Row, Col, Upload, Icon, DatePicker, InputNumber } from 'antd';
import { routerRedux } from 'dva/router';

import Container from '../Container';
import Box from '../Box';
import OrderCustomerInfo from '../CustomerInfo/OrderCustomerInfo'
import DeliveryGoods from './DeliveryGoods'

const FormItem = Form.Item;
const CheckboxGroup = Checkbox.Group;
const Option = Select.Option;
const Detail = (props) => {
  const { deliverys, customers, deliveryGoods, form, type, onSubmit, moreProps, dispatch, ...rest } = props
  const { loading } = deliverys;
  const { getFieldProps, getFieldError, isFieldValidating, setFieldsValue } = form;
  const delivery = deliverys.current
  const customer = customers.current
  const fileList = delivery.attachmentId ? 
  [
    {
      uid: -1,
      name: 'xxx.png',
      status: 'done',
      url: delivery.attachmentId,
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
          <Row>
            <Col span={12}><h3>送货安排详情</h3></Col>
            <Col span={12} style={{textAlign: 'right'}}>
              {
                type === 'edit'
                ?
                <div>
                  {
                    deliverys.readonly
                    ?
                    <Button type="primary" loading={ deliverys.submiting }
                      onClick={()=>{
                        dispatch({
                          type: 'deliverys/toggleReadonly',
                          payload: false
                        })
                      }}
                    >
                      编辑
                    </Button>
                    :
                    <div>
                      <Button type="primary" loading={ deliverys.submiting }
                        onClick={(e) => onSubmit(e, form)}
                      >
                      保存
                      </Button>
                      &nbsp;&nbsp;&nbsp;
                      <Button type="primary"
                        onClick={() => {
                          form.resetFields();
                          dispatch({
                          type: 'deliverys/toggleReadonly',
                          payload: true
                        })
                        }}
                      >
                      取消
                      </Button>
                    </div> 
                  }
                </div>
                :
                ''
              } 
            </Col>
          </Row>
          <br/>
          <Box>
            <Row>
              <Col span="12">
                <FormItem
                  {...rowItemLayout}
                  label="测量名称"
                  hasFeedback
                  help={isFieldValidating('name') ? '校验中...' : (getFieldError('name') || []).join(', ')}
                >
                {
                  deliverys.readonly 
                  ?
                  <p>
                    {delivery.name}
                  </p> 
                  :
                  <Input {...nameProps} disabled={ deliverys.submiting } />
                }
                </FormItem>
              </Col>
              <Col span="12">
                <FormItem
                  {...rowItemLayout}
                  label="要求时间"
                  hasFeedback
                  help={isFieldValidating('needTime') ? '校验中...' : (getFieldError('needTime') || []).join(', ')}
                >
                {
                  deliverys.readonly 
                  ?
                  <p>
                    {delivery.needTime}
                  </p> 
                  :
                  <DatePicker {...needTimeProps} disabled={ deliverys.submiting } style={{width: '100%'}} 
                    onChange={(date, dateString) => {
                      setFieldsValue({
                        needTime: dateString
                      })
                    }}
                  />
                } 
                </FormItem>
              </Col>
            </Row>
            <Row>
              <Col span="12">
                <FormItem
                  {...rowItemLayout}
                  label="服务人员"
                  hasFeedback
                  help={isFieldValidating('clerkId') ? '校验中...' : (getFieldError('clerkId') || []).join(', ')}
                >
                  {
                    deliverys.readonly 
                    ?
                    <p>
                      {delivery.clerkId}
                    </p> 
                    :
                    <Input {...clerkIdProps} disabled={ deliverys.submiting } />
                  }
                </FormItem>
              </Col>
              <Col span="12">
                <FormItem
                {...rowItemLayout}
                label="服务人员电话"
                hasFeedback
                help={isFieldValidating('clerkPhone') ? '校验中...' : (getFieldError('clerkPhone') || []).join(', ')}
              >
                {
                  deliverys.readonly 
                  ?
                  <p>
                    {delivery.clerkPhone}
                  </p> 
                  :
                  <Input {...clerkPhoneProps} disabled={ deliverys.submiting } />
                }
              </FormItem>
              </Col>
            </Row>
            <Row>
              <Col span="12">
                <FormItem
                  {...rowItemLayout}
                  label="费用"
                  hasFeedback
                  help={isFieldValidating('cost') ? '校验中...' : (getFieldError('cost') || []).join(', ')}
                >
                  {
                    deliverys.readonly 
                    ?
                    <p>
                      {delivery.cost}
                    </p> 
                    :
                    <InputNumber {...costProps} disabled={ deliverys.submiting } style={{width: '100%'}}/>
                  }
                </FormItem>
              </Col>
              <Col span="12">
                <FormItem
                  {...rowItemLayout}
                  label="服务位置"
                  hasFeedback
                  help={isFieldValidating('servicePosition') ? '校验中...' : (getFieldError('servicePosition') || []).join(', ')}
                > 
                  {
                    deliverys.readonly 
                    ?
                    <p>
                      {delivery.servicePosition}
                    </p> 
                    :
                    <Input {...servicePositionProps} disabled={ deliverys.submiting } />
                  }
                </FormItem>
              </Col>
            </Row>
            <Row>
              <Col span="24">
                <FormItem
                  {...fullRowLayout}
                  label="摘要"
                  hasFeedback
                  help={isFieldValidating('remark') ? '校验中...' : (getFieldError('remark') || []).join(', ')}
                > 
                  {
                    deliverys.readonly 
                    ?
                    <p>
                      {delivery.remark}
                    </p> 
                    :
                    <Input type='textarea' rows={6} {...remarkProps} disabled={ deliverys.submiting }/>
                  }
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
                {
                  deliverys.readonly 
                  ?
                  <a>
                  {delivery.attachmentId}
                  </a> 
                  :
                  <Upload 
                    {...attachmentIdProps}
                    showUploadList={ false }
                    fileList={fileList}
                    beforeUpload={file => {
                    }} 
                  >
                    <Button type="ghost" disabled={ deliverys.submiting }>
                      <Icon type="upload" /> 点击上传
                    </Button>
                  </Upload>
                }
                </FormItem>
              </Col>
            </Row>
            <Row>
              <Col span="24">
                <FormItem
                  {...fullRowLayout}
                  label="测量标记"
                  hasFeedback
                  help={isFieldValidating('attachmentId') ? '校验中...' : (getFieldError('attachmentId') || []).join(', ')}
                >
                  {
                    deliverys.readonly 
                    ?
                    <p>
                    {delivery.isFinish ? '已完成' : '未完成'}
                    </p> 
                    :
                    <Checkbox defaultChecked={delivery.isFinish || false} {...isFinishProps} disabled={ deliverys.submiting }
                      onChange={(e) => {
                        setFieldsValue({
                          isFinish: e.target.checked
                        })
                      }}
                    >已完成</Checkbox>
                  }
                </FormItem>
              </Col>
            </Row>
          </Box>
          <h3>本次送货的商品列表</h3>
          <br/>
          <DeliveryGoods
            {...props}
            onSubmit={e => {
              e.preventDefault();
              let formData = form.getFieldsValue();
              if (type === 'edit') {
                let goods = deliveryGoods;
                goods.deliveryId = delivery.id
                dispatch({
                  type: 'deliveryGoods/edit',
                  payload: goods
                })
              } else {
                
                form.validateFieldsAndScroll((errors, values) => {
                  if (!!errors) {
                    return;
                  }
                  formData.deliveryGoods = deliveryGoods;
                  dispatch({
                    type: 'deliverys/add',
                    payload: formData
                  })  
                });
              }
            }}
          >
          </DeliveryGoods>
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
    const delivery = props.deliverys.current;
    return {
      name: {
        value: delivery.name
      },
      clerkId: {
        value: delivery.clerkId
      },
      attachmentId: {
        value: delivery.attachmentId
      },
      needTime: {
        value: delivery.needTime
      },
      remark: {
        value: delivery.remark
      },
      cost: {
        value: delivery.cost
      },
      servicePosition: {
        value: delivery.servicePosition
      },
      ...props.mapPropsToFields(delivery),
    }
  }
})(Detail);
