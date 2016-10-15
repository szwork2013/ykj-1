import React, { PropTypes } from 'react';
import { routerRedux } from 'dva/router';
import { Form, Input, Select, DatePicker, TreeSelect, Checkbox, Button, Tag, Col, Row } from 'antd';

import Container from '../Container';
import Box from '../Box';

const FormItem = Form.Item;
const Option = Select.Option;

const Detail = ({ users, form, dispatch, type, onSubmit, ...rest }) => {

    const dicts = {roleType: [{key:0,label:'张三'},{key:1,label:'李四'}],
                   sexType: [{key:0,label:'男'},{key:1,label:'女'}]}

    const formItemLayout = {
        labelCol: { span: 6 },
        wrapperCol: { span: 12 },
    }

    const { getFieldProps, getFieldValue, getFieldError, isFieldValidating } = form;

    const nameProps = getFieldProps('name', {
        rules: [
            { required: true, min: 1, message: '姓名至少 1 个字符'}
        ]
    });

    const sexProps = getFieldProps('sex', {
        rules: [
            { required: true, message: '请选择性别'}
        ]
    });

    const telephoneProps = getFieldProps('phone', {
        rules: [
            { required: true, message: '手机号码必须填写' },
            { pattern: new RegExp(/^1[0-9]{10}$/), message: '请填写正确的手机号码' },
        ],
    });

    const prePhoneProps = getFieldProps('phoneSec', {
        rules: [
            { pattern: new RegExp(/^1[0-9]{10}$/), message: '请填写正确的备用号码' },
        ],
    });

    const qqProps = getFieldProps('qq', {
        rules: [
            { pattern: new RegExp(/^[0-9]+$/), message: '请填写正确的QQ' },
        ],
    });

    const emailProps = getFieldProps('email', {
        rules: [
            { required: true, message: '邮箱必须填写'},
            { pattern: new RegExp(/(^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\.[a-zA-Z0-9_-]+$)|(^$)/), message: '请填写正确的邮箱' },
        ],
    });

    const userNameProps = getFieldProps('username', {
        rules: [
            { required: true, min: 1, message: '用户名至少 1 个字符'}
        ],
    });

    const passProps = type == 'add' ? getFieldProps('password', {
        rules: [
            { required: true, whitespace: true, message: '请填写密码' }
        ],
    }) : undefined;

    const rePassProps = type == 'add' ? getFieldProps('repassword', {
        rules: [{
            required: true,
            whitespace: true,
            message: '请再次输入密码',
        }, {
            validator: (rule, value, callback) => {
                if (value && value !== form.getFieldValue('password')) {
                    callback('两次输入密码不一致！');
                } else {
                    callback();
                }
            },
        }],
    }) : undefined;

    const roleProps = getFieldProps('roleType', {
        rules: [
            { required: true, message: '请选择角色类型' },
        ],
    });

    const officeProps = getFieldProps('officeId', {
        rules: [
            { required: true, message: `直属部门不得为空`}
        ],
    })

    return (
        <Container
            {...rest}
        >
            <Box>
                <Form horizontal>
                    <h3>员工基本信息</h3>
                    <br/>
                        <Row>
                            <Col sm={ 12 }>
                                <FormItem
                                    {...formItemLayout}
                                    label="员工姓名"
                                    hasFeedback
                                    help={isFieldValidating('name') ? '校验中...' : (getFieldError('name') || []).join(', ')}
                                >
                                    <Input  {...nameProps} disabled={ users.submiting } style={ {width: '70%'} }></Input>
                                </FormItem>
                            </Col>
                            <Col sm={ 12 }>
                                <FormItem
                                    {...formItemLayout}
                                    hasFeedback
                                    help={isFieldValidating('sex') ? '校验中...' : (getFieldError('sex') || []).join(', ')}
                                    label="性别"
                                >
                                    <Select {...sexProps} placeholder="请选择性别" onChange={ (value) => {
                                        form.setFieldsValue({
                                            sex: value,
                                        });
                                    }} disabled={ users.submiting } style={ {width: '70%'} } >
                                        {
                                            dicts.sexType.map( (sex) => {
                                                return <Option key={ sex.key } value={ `${sex.key}` } >{sex.label}</Option>
                                            } )
                                        }
                                    </Select>
                                </FormItem>
                            </Col>
                        </Row>                       
                    <br/>
                        <Row>
                            <Col sm={ 12 }>
                                <FormItem
                                    {...formItemLayout}
                                    label="出生日期"
                                    required={true}
                                >
                                    <DatePicker {...getFieldProps('birthday')} onChange={ (data, dataString) =>{
                                        form.setFieldsValue({
                                            birthday: dataString,
                                        })
                                    } } disabled={ users.submiting } style={ {width: '70%'} } ></DatePicker>
                                </FormItem>
                            </Col>
                            <Col sm={ 12 }>
                                <FormItem
                                    {...formItemLayout}
                                    label="选择部门"
                                    hasFeedback
                                    help={isFieldValidating('officeId') ? '校验中...' : (getFieldError('officeId') || []).join(', ')}
                                >
                                    <Input {...officeProps} disabled={ users.submiting } style={ {width: '70%'} }></Input>
                                </FormItem>
                            </Col>
                        </Row>                       
                    <br/>
                        <Row>
                            <Col sm={ 12 }>
                                <FormItem
                                    {...formItemLayout}
                                    label="选择角色"
                                    hasFeedback
                                    help={isFieldValidating('roleType') ? '校验中...' : (getFieldError('roleType') || []).join(', ')}
                                >
                                    <Select {...roleProps} placeholder="请选择角色类型" onChange={ (value) => {
                                        form.setFieldsValue({
                                            roleType: value,
                                        });
                                    }} disabled={ users.submiting } style={ {width: '70%'} } >
                                        {
                                            dicts.roleType.map( (role) => {
                                                return <Option key={ role.key } value={ `${role.key}` } >{role.label}</Option>
                                            } )
                                        }
                                    </Select>
                                </FormItem>
                            </Col>
                            <Col sm={ 12 }>
                                <FormItem
                                    {...formItemLayout}
                                    label="手机号码"
                                    hasFeedback
                                    help={isFieldValidating('phone') ? '校验中...' : (getFieldError('phone') || []).join(', ')}
                                >
                                    <Input  {...telephoneProps} disabled={ users.submiting } style={ {width: '70%'} }></Input>
                                </FormItem>
                            </Col>
                        </Row>
                    <br/>
                        <Row>          
                            <Col sm={ 12 }>
                                <FormItem
                                    {...formItemLayout}
                                    label="备用手机号码"
                                    hasFeedback
                                    help={isFieldValidating('phoneSec') ? '校验中...' : (getFieldError('phoneSec') || []).join(', ')}
                                >
                                    <Input  {...prePhoneProps} disabled={ users.submiting } style={ {width: '70%'} }></Input>
                                </FormItem>
                            </Col>
                            <Col sm={ 12 }>
                                <FormItem
                                    {...formItemLayout}
                                    label="QQ"
                                    hasFeedback
                                    help={isFieldValidating('qq') ? '校验中...' : (getFieldError('qq') || []).join(', ')}
                                >
                                    <Input  {...qqProps} disabled={ users.submiting } style={ {width: '70%'} }></Input>
                                </FormItem>
                            </Col>
                        </Row>                       
                    <br/>
                        <Row>
                            <Col sm={ 12 }>
                                <FormItem
                                    {...formItemLayout}
                                    label="微信号"
                                >
                                    <Input  {...getFieldProps('weChat')} disabled={ users.submiting } style={ {width: '70%'} }></Input>
                                </FormItem>
                            </Col>
                            <Col sm={ 12 }>
                                <FormItem
                                    {...formItemLayout}
                                    label="邮箱"
                                    hasFeedback
                                    help={isFieldValidating('email') ? '校验中...' : (getFieldError('email') || []).join(', ')}
                                >
                                    <Input  {...emailProps} disabled={ users.submiting } style={ {width: '70%'} }></Input>
                                </FormItem>
                            </Col>
                        </Row>                       
                    <br/>
                    <h3>用户基本信息</h3>
                    <br/>
                    {
                        type == 'edit' ?
                        (
                            <Row>
                                <Col sm={ 12 } >
                                    <FormItem
                                        {...formItemLayout}
                                        label="用户名"
                                    >
                                        <p className="ant-form-text" >{ getFieldValue('username') }</p>
                                    </FormItem>
                                </Col>
                            </Row>
                        )
                        :
                        (
                            <div>
                                <Row>
                                    <Col sm={ 12 }>
                                        <FormItem
                                            {...formItemLayout}
                                            label="用户名"
                                            hasFeedback
                                            help={isFieldValidating('username') ? '校验中...' : (getFieldError('username') || []).join(', ')}
                                        >
                                            <Input  {...userNameProps} disabled={ users.submiting } style={ {width: '70%'} }></Input>
                                        </FormItem>
                                    </Col>
                                    <Col sm={ 12 }>
                                        <FormItem
                                            {...formItemLayout}
                                            label="登录密码"
                                            hasFeedback
                                            help={isFieldValidating('password') ? '校验中...' : (getFieldError('password') || []).join(', ')}
                                        >
                                            <Input {...passProps} type="password" disabled={ users.submiting } style={ {width: '70%'} }></Input>
                                        </FormItem>
                                    </Col>
                                </Row>                      
                                <br/>
                                <Row>
                                    <Col sm={ 12 }>
                                        <FormItem
                                            {...formItemLayout}
                                            label="再次输入"
                                            hasFeedback
                                            help={isFieldValidating('repassword') ? '校验中...' : (getFieldError('repassword') || []).join(', ')}
                                        >
                                            <Input {...rePassProps} type="password" disabled={ users.submiting } style={ {width: '70%'} }></Input>
                                        </FormItem>
                                    </Col>
                                </Row>
                            </div>
                        )
                    }    
                    <br/><br/><br/>
                    <FormItem wrapperCol={{ span: 22, offset: 16 }} style={{ marginTop: 24 }}>
                        <Button type="primary" htmlType="submit"  loading={ users.submiting } onClick={ (e) => onSubmit(e, form) }>确定</Button>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <Button type="ghost" onClick={ () => { dispatch(routerRedux.goBack()) } }>返回</Button>
                    </FormItem>    
                </Form>
            </Box>
        </Container>
    )
}


export default Form.create({
  mapPropsToFields: (props) => {
    const id = props.params.id;
    if (!id) {
      return {};
    }
    
    const user = props.users.current;
    return {
      name: {
        value: user.name
      },
      sex: {
        value: `${user.sex}`
      },
      birthday: {
        value: user.birthday
      },
      officeId: {
        value: user.officeId
      },
      roleType: {
        value: `${user.roleType}`
      },
      phone: {
        value: user.phone
      },
      phoneSec: {
        value: user.phoneSec
      },
      qq: {
        value: user.qq
      },
      weChat: {
        value: user.weChat
      },
      email: {
        value: user.email
      },
      username: {
        value: user.username
      },
      password: {
        value: user.password
      },
      repassword: {
        value: user.repassword
      },
    }
  }
})(Detail);