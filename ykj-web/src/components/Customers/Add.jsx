import React, { PropTypes } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Radio, Button, Row, Col, Modal } from 'antd';
import { routerRedux } from 'dva/router';

import Container from '../Container';
import Box from '../Box';
import TagBox from '../TagBox';
import TreeBox from '../TreeBox';

const FormItem = Form.Item;
const CheckboxGroup = Checkbox.Group;
const Option = Select.Option;
const RadioButton = Radio.Button;
const RadioGroup = Radio.Group;

const Add = ({ customers, codewordTypes, tags, dispatch, form, ...rest }) => {
	const { getFieldProps, getFieldError, isFieldValidating, getFieldsValue, setFieldsValue } = form;
	const sex = codewordTypes['SEX'] || [];
	const customerNeedTime = codewordTypes['CUSTOMER_NEED_TIME'] || [];
	const customerSource = codewordTypes['CUSTOMER_SOURCE'] || [];

	const onChange = function() {

  	}
	
	const treeData = [
		{ name: '杭州', id: '1' },
		{ name: '台州', id: '2' },
		{ name: '湖州', id: '3' },
		{ name: '张伟刚', id: '4', pId: '1' },
		{ name: '许照亮', id: '5', pId: '2' },
		{ name: '其味无穷', id: '6', pId: '5' },
	]

  	const handleClose = function(id) {
        dispatch({
            type: 'tags/deleteTag',
            payload: id
        })
    }

    const handleOk = function(name) {
        dispatch({
            type: 'tags/addTag',
            payload: name,
        })
    }

	const onSubmit = function(e, form) {
		e.preventDefault();
		form.validateFieldsAndScroll((errors, values) => {
			if (!!errors) {
				return;
			}
			const formData = form.getFieldsValue();
			dispatch({
				type: 'customers/add',
				payload: formData,
			})
		})
	}

    const managerHandleCancel = function(){}

    const managerHandleOk = function(){}

  	const nameProps = getFieldProps('name', {
  		rules: [
	      	{ required: true, message: '请输入姓名'}
	    ]
  	})

  	const sexProps = getFieldProps('sex', {
  		rules: [
	      	{ required: true, message: '请输入性别' }
	    ]
  	})

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

  	const qqProps = getFieldProps('qq', {
  	})

  	const wechatProps = getFieldProps('wechat', {
  	})

  	const emailProps = getFieldProps('email', {
    	rules: [
			{ type: 'email', message: '请填写正确格式的邮箱' },
		]
  	})

  	const customerResponsibleIdProps = getFieldProps('customerResponsibleId', {
  		rule: [
  			{ required: true, message: '请选择客户负责人' }
  		]
  	})

	const needProductProps = getFieldProps('needProduct', {

	})

	const needTimeProps = getFieldProps('needTime', {
		rules: [
  			{ required: true, message: '请选择购物需求' }
  		]
	})

  	const addressProps = getFieldProps('address', {
	    rules: [
	      	{ required: true, min: 1, message: '请输入联系地址'}
	    ]
  	});

  	const tagsProps = getFieldProps('tags', {
  		rules: [
	      	{ type: 'array' }
	    ]
  	})

  	const originTypeProps = getFieldProps('originType', {
		rules: [
	      	{ required: true, message: '请输入客户来源'}
	    ]
  	})
	
	const formItemLayout = {
	    labelCol: { span: 3 },
	    wrapperCol: { span: 5 },
  	};
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
      
      	<Box
      	>
        	<Form horizontal onSubmit={ e => onSubmit(e, form) }>
	          	<h3>客户基本资料</h3>
	          	<br/>
	          	<Row>
		            <Col span="12">
		              	<FormItem
			                {...rowItemLayout}
			                label="客户姓名"
			                hasFeedback
			                help={isFieldValidating('name') ? '校验中...' : (getFieldError('name') || []).join(', ')}
		              	>
		                	<Input {...nameProps} />
		              	</FormItem>
		            </Col>
		            <Col span="12">
		              	<FormItem
		                	{...rowItemLayout}
		                	label="客户性别"
			                hasFeedback
			                help={isFieldValidating('sex') ? '校验中...' : (getFieldError('sex') || []).join(', ')}
		              	>
	                	 	<RadioGroup onChange={onChange} defaultValue="0" {...sexProps}>
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
							help={isFieldValidating('needTime') ? '校验中...' : (getFieldError('needTime') || []).join(', ')}
		              	>
			                <Select showSearch
			                  	placeholder="请选择购物需求"
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
		                	label="客户负责人"
		                	hasFeedback
			                help={isFieldValidating('customerResponsibleId') ? '校验中...' : (getFieldError('customerResponsibleId') || []).join(', ')}
		              	>	
							<TreeBox 
					          	treeData={treeData}
					          	multiple={ false }
					          	checkable={ false }
								changeable={ true }
					          	treeProps={ customerResponsibleIdProps }
								onOk={(value) => {
									const { setFieldsValue } = form
									setFieldsValue({
										customerResponsibleId: value
									})
								}}
				          	/> 
		              	</FormItem>
		            </Col>
	          	</Row>

	          	<Row>
		            <Col span="24">
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

	          	<Row>
		            <Col span="24">
		              	<FormItem
			                {...fullRowLayout}
			                label="客户标签"
			                hasFeedback
							help={isFieldValidating('tags') ? '校验中...' : (getFieldError('tagss') || []).join(', ')}
		              	>
							<Select tags
								{...tagsProps}
								style={{ width: '100%' }}
								placeholder="输入后按enter添加一个标签"
							>
							</Select>
		              	</FormItem>
		            </Col>
	          	</Row>

	          	<Row>
		            <Col span="12">
		              	<FormItem
			                {...rowItemLayout}
			                label="客户来源"
			                hasFeedback
							help={isFieldValidating('originType') ? '校验中...' : (getFieldError('originType') || []).join(', ')}
		              	>
							<Select showSearch
			                  	placeholder="请选择客户来源"
			                 	notFoundContent="无法找到"
			                  	onChange={onChange}
			                  	{...originTypeProps}
			                >
								{
									customerSource.map(item => {
										return <Option key={item.code} value={item.code}>{item.value}</Option>
									})
								}
		                	</Select>
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

function mapPropsToFields(props) {
  const customer = props.customers.current;

  return {
    customerResponsibleId: {
      value: customer.customerResponsibleId || '1'
    },
	
  }
}

export default Form.create({mapPropsToFields})(Add);
