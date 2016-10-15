import React, { PropTypes } from 'react';
import { Modal, Form, Cascader, InputNumber, Row, Col, Input, DatePicker } from 'antd';

const FormItem = Form.Item;

const FinishModal = ({ form, dispatch, submiting, onOk, ...rest }) => {
    const { getFieldProps, getFieldError, isFieldValidating } = form;

    const finishDateProps = getFieldProps('finishDate', {
        rules: [
            { required: true, message: '请输入完成时间' }
        ]
    })

    const formItemLayout = {
        labelCol: { span: 9 },
        wrapperCol: { span: 15 },
    };

    return (
        <Modal
            title= "输入订单完成时间"
            { ...rest }
            onOk= { () => {
                form.validateFields( (error, ) => {
                    if(error) {
                        return false;
                    }
                    const formData = form.getFieldsValue();
                    onOk(formData);
                    form.resetFields();
                });
            }}
            onCancel= { () => {
                dispatch({
                    type: 'orders/toggleFinishModal',
                    payload: {
                        currentOrderId: undefined,
                        finishModalShow: false,
                    },
                });
                form.resetFields();   
            }}
        >
            <Form horizontal>
                <FormItem
                    { ...formItemLayout }
                    label= "订单完成时间"
                    hasFeedback
                    help={isFieldValidating('finishDate') ? '校验中...' : (getFieldError('finishDate') || []).join(', ')}
                >
                    <DatePicker {...finishDateProps} size="default" onChange={ (date, dateString) => {
                        form.setFieldsValue({
                            finishDate: dateString,
                        })
                    }} disabled={ submiting }/>
                </FormItem>   
            </Form>
        </Modal>
    )

}

FinishModal.propTypes = {
    form: PropTypes.object.isRequired,
}

export default Form.create({})(FinishModal)