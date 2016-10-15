import React, { PropTypes } from 'react';
import { Modal, Form, Cascader, InputNumber, Row, Col, Input, DatePicker, Select } from 'antd';

const FormItem = Form.Item;
const Option = Select.Option;

const PayModal = ({ form, dispatch, submiting, onOk, ...rest }) => {
    const { getFieldProps, getFieldError, isFieldValidating } = form;

    const categoryProps = getFieldProps('category', {
        rules: [
            { required: true, message: '请选择类目' }
        ]
    })

    const payWayProps = getFieldProps('payWay', {
        rules: [
            { required: true, message: '请选择付款方式' }
        ]
    })

    const paymentProps = getFieldProps('payment', {
        rules: [
            { required: true, type: 'number', message: '请输入金额'}
        ]
    })

    const remarkProps = getFieldProps('remark', {
        rules: [

        ]
    })

    const formItemLayout = {
        labelCol: { span: 9 },
        wrapperCol: { span: 15 },
    };

    return (
        <Modal
            title= "付款"
            { ...rest }
            onOk= { () => {
                form.validateFields( (error) => {
                    if(error) {
                        return false;
                    }
                    const formData = form.getFieldsValue();
                    onOk(formData);
                    form.resetFields();
                });
            }}
            onCancel= { () => {
                form.resetFields();
                dispatch({
                    type: 'orders/togglePayModal',
                    payload: {
                        currentOrderId: undefined,
                        payModalShow: false,
                    },
                })    
            }}
        >
            <Form horizontal>
                <div style={ { marginTop: '20px' } }>
                    <Row>
                        <Col sm={10} >
                            <FormItem
                                { ...formItemLayout }
                                label= "类目"
                                hasFeedback
                                help={isFieldValidating('category') ? '校验中...' : (getFieldError('category') || []).join(', ')}
                            >
                                <Select {...categoryProps} disabled={ submiting } size="default">

                                </Select>
                            </FormItem>   
                        </Col>
                        <Col sm={10} offset={2}>
                            <FormItem
                                { ...formItemLayout }
                                label= "付款方式"
                                hasFeedback
                                help={isFieldValidating('payWay') ? '校验中...' : (getFieldError('payWay') || []).join(', ')}
                            >
                                <Select {...payWayProps} disabled={ submiting } size="default">

                                </Select>
                            </FormItem>   
                        </Col>
                    </Row>
                    <Row>
                        <Col sm={10} >
                            <FormItem
                                { ...formItemLayout }
                                label= "金额"
                                hasFeedback
                                help={isFieldValidating('payment') ? '校验中...' : (getFieldError('payment') || []).join(', ')}
                            >
                                <InputNumber {...paymentProps} min={1} defaultValue={1} disabled={ submiting } size="default" />
                            </FormItem>   
                        </Col>
                    </Row>
                    <Row>
                        <Col sm={22} >
                            <FormItem
                                labelCol={ { span: 4 } }
                                wrapperCol={ { span: 20 } }
                                label= "摘要"
                                hasFeedback
                                help={isFieldValidating('remark') ? '校验中...' : (getFieldError('remark') || []).join(', ')}
                            >
                                <Input {...remarkProps} disabled={ submiting } size="default" />
                            </FormItem>   
                        </Col>
                    </Row> 
                </div> 
            </Form>
        </Modal>
    )

}

PayModal.propTypes = {
    form: PropTypes.object.isRequired,
}

export default Form.create({})(PayModal)