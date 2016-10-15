import React, { PropTypes } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Row, Col, Radio } from 'antd';
import { routerRedux } from 'dva/router';

import Container from '../Container';
import Box from '../Box';
import TagBox from '../TagBox'

import CustomerInfo from '../CustomerInfo'

const FormItem = Form.Item;
const CheckboxGroup = Checkbox.Group;
const RadioGroup = Radio.Group;
const RadioButton = Radio.Button;
const Option = Select.Option;
const Detail = ({ customers, codewordTypes, tags, dispatch, tree, form, onSubmit, ...rest }) => {
  const { getFieldProps, getFieldError, isFieldValidating } = form;
  const sex = codewordTypes['SEX'] || [];
	const customerNeedTime = codewordTypes['CUSTOMER_NEED_TIME'] || [];

  let options = { ...customers.current };
  options.dispatch = dispatch;
  options.tree = tree;
  options.changeable = true;
  const onChange = function() {

  }

  const handleOk = function(name) {
    dispatch({
      type: 'tags/add',
      payload: name
    })
  }

  const handleClose = function(id) {
    dispatch({
      type: 'tags/deleteOne',
      payload: id,
    })
  }

  const phoneProps = getFieldProps('phone', {
    rules: [
      { required: true, message: '请填写手机号码' },
      { pattern: /^1[0-9]{10}$/, message: '请填写有效手机号' },
    ]
  })

  const phoneSecProps = getFieldProps('phoneSec', {
    rules: [
      { pattern: /^1[0-9]{10}$/, message: '请填写有效手机号' },
    ]
  })

  const sexProps = getFieldProps('sex', {
    rules: [
        { required: true, message: '请输入性别'  }
    ]
  })

  const needProductProps = getFieldProps('needProduct', {
  })

  const needTimeProps = getFieldProps('needTime', {
    rules: [
        { required: true, message: '请输入购物需求'  }
    ]
  }) 

  const qqProps = getFieldProps('qq', {
  })

  const wechatProps = getFieldProps('wechat', {
  })

  const emailProps = getFieldProps('email', {
    rules: [
      { type:'email', message: '请填写有效的邮箱' }
    ]
  })

  const addressProps = getFieldProps('address', {
    rules: [
      { required: true, message: '请输入联系地址'}
    ]
  });

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
        <Box>
          <CustomerInfo options={options}>
            <Row>
              <Col span={23} offset={1}>
                  <span>
                      <TagBox 
                        tags={tags}
                        dispatch={dispatch}
                        handleOk={handleOk}
                        handleClose={handleClose}
                      />
                  </span>
              </Col>
            </Row>
          </CustomerInfo>
        </Box>
        
        <Box>
          <Form horizontal onSubmit={ e => onSubmit(e, form) }>
            <h3>客户基本资料</h3>
            <br/>
            <Row>
              <Col span="12">
                <FormItem
                  {...rowItemLayout}
                  label="手机号码"
                  hasFeedback
                  help={isFieldValidating('phone') ? '校验中...' : (getFieldError('phone') || []).join(', ')}
                >
                  <Input {...phoneProps} />
                </FormItem>
              </Col>
              <Col span="12">
                <FormItem
                  {...rowItemLayout}
                  label="备用手机"
                  hasFeedback
                  help={isFieldValidating('phoneSec') ? '校验中...' : (getFieldError('phoneSec') || []).join(', ')}
                >
                  <Input {...phoneSecProps}/>
                </FormItem>
              </Col>
            </Row>
            
            <Row>
              <Col span="12">
                <FormItem
                  {...rowItemLayout}
                  label="意向产品"
                  hasFeedback
                  help={isFieldValidating('needProduct') ? '校验中...' : (getFieldError('needProduct') || []).join(', ')}
                >
                  <Input {...needProductProps}/>
                </FormItem>
              </Col>
              <Col span="12">
                <FormItem
                  {...rowItemLayout}
                  label="购物需求"
                  hasFeedback
                >
                  <Select showSearch
                    placeholder="请选择购物需求"
                    optionFilterProp="children"
                    notFoundContent="无法找到"
                    onChange={onChange}
                    
                    {...needTimeProps}
                  >
                    {
                      customerNeedTime.map(item => {
                        return <Option key={item.code} value={item.code}>{item.value}</Option>
                      })
                    }
                  </Select>
                </FormItem>
              </Col>
            </Row>

            <Row>
              <Col span="12">
                <FormItem
                  {...rowItemLayout}
                  label="微信"
                  hasFeedback
                  help={isFieldValidating('wechat') ? '校验中...' : (getFieldError('wechat') || []).join(', ')}
                >
                  <Input {...wechatProps}/>
                </FormItem>
              </Col>
              <Col span="12">
                <FormItem
                  {...rowItemLayout}
                  label="qq号码"
                  hasFeedback
                >
                  <Input {...qqProps} />
                </FormItem>
              </Col>
            </Row>

            <Row>
              <Col span="12">
                <FormItem
                  {...rowItemLayout}
                  label="邮箱地址"
                  hasFeedback
                  help={isFieldValidating('email') ? '校验中...' : (getFieldError('email') || []).join(', ')}
                >
                  <Input {...emailProps} />
                </FormItem>
              </Col>
              <Col span="12">
                <FormItem
		                	{...rowItemLayout}
		                	label="客户性别"
			                hasFeedback
			                help={isFieldValidating('sex') ? '校验中...' : (getFieldError('sex') || []).join(', ')}
		              	>
	                	 	<RadioGroup {...sexProps}>
	                	 		{
                          sex.map(item => {
                            return <RadioButton key={item.code} value={item.code}>{item.value}</RadioButton>
                          })
                        }
                    </RadioGroup>
                </FormItem>
              </Col>
            </Row>

            <Row>
              <Col span="23">
                <FormItem
                  {...fullRowLayout}
                  label="联系地址"
                  hasFeedback
                  help={isFieldValidating('address') ? '校验中...' : (getFieldError('address') || []).join(', ')}
                >
                  <Input {...addressProps} />
                </FormItem>
              </Col>
            </Row>

            <FormItem wrapperCol={{ span: 22, offset: 3 }} style={{ marginTop: 24 }}>
              <Button type="primary" htmlType="submit">确定</Button>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <Button type="ghost" onClick={ () => dispatch(routerRedux.goBack()) }>返回</Button>
            </FormItem>
          </Form>
        </Box>
    </Container>
  )
}

Detail.propTypes = {
  onSubmit: PropTypes.func.isRequired,
}

Detail.defaultProps = {
  onSubmit: () => {},
}

function mapPropsToFields(props) {
  const customer = props.customers.current;
  return {
    phone: {
      value: customer.phone
    },
    phoneSec: {
      value: customer.phoneSec
    },
    needProduct: {
      value: customer.needProduct
    },
    needTime: {
      value: customer.needTime ? `${customer.needTime}` : undefined
    },
    wechat: {
      value: customer.wechat
    },
    qq: {
      value: customer.qq
    },
    email: {  
      value: customer.email
    },
    address: {
      value: customer.address
    },
    sex: {
      value: customer.sex ? `${customer.sex}` : undefined
    }
  }
}

export default Form.create({mapPropsToFields})(Detail);
