import React, { PropTypes } from 'react';
import { Modal, Form, Cascader, InputNumber, Row, Col, Input, DatePicker } from 'antd';

const FormItem = Form.Item;

const AuditModal = ({ form, dispatch, orders, onOk, ...rest }) => {
    const { getFieldProps, getFieldError, isFieldValidating } = form;

    const formItemLayout = {
        labelCol: { span: 7 },
        wrapperCol: { span: 15 },
    };

    return (
        <Modal
            title= "订单审核"
            { ...rest }
            onOk= { () => {
                 onOk();
            }}
            onCancel= { () => {
                dispatch({
                    type: 'orders/toggleAuditModal',
                    payload: {
                        currentOrderId: undefined,
                        AuditModalShow: false,
                    },
                })    
            }}
        >
            <div style={ { marginTop: '12px', marginLeft: '100px'} }>
                <p className="ant-form-text" >{ `订单号：${orders.current.orderNo}` }</p>
                <br/><br/>
                <p className="ant-form-text" >{ `订单地址：${orders.current.address}` }</p>
                <br/><br/>
                <p className="ant-form-text" >{ `请确认是否审核通过` }</p>
            </div>
        </Modal>
    )

}

AuditModal.propTypes = {
    form: PropTypes.object.isRequired,
}

export default Form.create({})(AuditModal)