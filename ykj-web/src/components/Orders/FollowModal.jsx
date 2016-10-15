import React, { PropTypes } from 'react';
import { Modal, Form, Cascader, InputNumber, Row, Col, Input, DatePicker, Select, Button } from 'antd';
import styles from './FollowModal.less';

const FormItem = Form.Item;
const Option = Select.Option;

const FollowModal = ({ form, dispatch, ...rest }) => {
    const { getFieldProps, getFieldError, isFieldValidating } = form;

    const status = [true, false, true, false, true, false, true, false, true]

    const formItemLayout = {
        labelCol: { span: 9 },
        wrapperCol: { span: 15 },
    };

    return (
        <Modal
            title= "订单跟踪"
            { ...rest }
            closable={ false }
            footer={ 
                <Button type="primary" onClick={ () =>{
                    dispatch({
                        type: 'orders/toggleFollowModal',
                        payload: {
                            currentOrderId: undefined,
                            FollowModalShow: false,
                        },
                    });
                }} >确定</Button>
            }   
            width={ 700 }   
        >
             
            <div className={styles['outerLayout']} >
                <hr className={styles['hrLayout']} />
                {
                    status[0] ?
                    (
                        <div className={ styles['statusRadius'] }>
                            <div style={ { position: 'absolute',top: '35%',left: '8.6%'} }><h4>报价</h4></div>
                        </div>
                    )
                    :
                    (
                        <div className={ styles['noStatusRadius'] }>
                            <div style={ { position: 'absolute',top: '35%',left: '8.6%'} }><h4>报价</h4></div>
                        </div>
                    )
                }
                {
                    status[1] ?
                    (
                        <div className={ styles['statusRadius'] }>
                            <div style={ { position: 'absolute',top: '35%',left: '18.6%'} }><h4>审核</h4></div>
                        </div>
                    )
                    :
                    (
                        <div className={ styles['noStatusRadius'] }>
                            <div style={ { position: 'absolute',top: '35%',left: '18.6%'} }><h4>审核</h4></div>
                        </div>
                    )
                }
                {
                    status[2] ?
                    (
                        <div className={ styles['statusRadius'] }>
                            <div style={ { position: 'absolute',top: '35%',left: '28.6%'} }><h4>定金</h4></div>
                        </div>
                    )
                    :
                    (
                        <div className={ styles['noStatusRadius'] }>
                            <div style={ { position: 'absolute',top: '35%',left: '28.6%'} }><h4>定金</h4></div>
                        </div>
                    )
                }
                {
                    status[3] ?
                    (
                        <div className={ styles['statusRadius'] }>
                            <div style={ { position: 'absolute',top: '35%',left: '38.6%'} }><h4>测量</h4></div>
                        </div>
                    )
                    :
                    (
                        <div className={ styles['noStatusRadius'] }>
                            <div style={ { position: 'absolute',top: '35%',left: '38.6%'} }><h4>测量</h4></div>
                        </div>
                    )
                }
                {
                    status[4] ?
                    (
                        <div className={ styles['statusRadius'] }>
                            <div style={ { position: 'absolute',top: '35%',left: '48.6%'} }><h4>设计</h4></div>
                        </div>
                    )
                    :
                    (
                        <div className={ styles['noStatusRadius'] }>
                            <div style={ { position: 'absolute',top: '35%',left: '48.6%'} }><h4>设计</h4></div>
                        </div>
                    )
                }
                {
                    status[5] ?
                    (
                        <div className={ styles['statusRadius'] }>
                            <div style={ { position: 'absolute',top: '35%',left: '58.6%'} }><h4>付款</h4></div>
                        </div>
                    )
                    :
                    (
                        <div className={ styles['noStatusRadius'] }>
                            <div style={ { position: 'absolute',top: '35%',left: '58.6%'} }><h4>付款</h4></div>
                        </div>
                    )
                }
                {
                    status[6] ?
                    (
                        <div className={ styles['statusRadius'] }>
                            <div style={ { position: 'absolute',top: '35%',left: '68.6%'} }><h4>送货</h4></div>
                        </div>
                    )
                    :
                    (
                        <div className={ styles['noStatusRadius'] }>
                            <div style={ { position: 'absolute',top: '35%',left: '68.6%'} }><h4>送货</h4></div>
                        </div>
                    )
                }
                {
                    status[7] ?
                    (
                        <div className={ styles['statusRadius'] }>
                            <div style={ { position: 'absolute',top: '35%',left: '78.6%'} }><h4>安装</h4></div>
                        </div>
                    )
                    :
                    (
                        <div className={ styles['noStatusRadius'] }>
                            <div style={ { position: 'absolute',top: '35%',left: '78.6%'} }><h4>安装</h4></div>
                        </div>
                    )
                }
                {
                    status[8] ?
                    (
                        <div className={ styles['statusRadius'] }>
                            <div style={ { position: 'absolute',top: '35%',left: '88.6%'} }><h4>完成</h4></div>
                        </div> 
                    )
                    :
                    (
                        <div className={ styles['noStatusRadius'] }>
                            <div style={ { position: 'absolute',top: '35%',left: '88.6%'} }><h4>完成</h4></div>
                        </div> 
                    )
                }
            </div> 
            
        </Modal>
    )

}

FollowModal.propTypes = {
    form: PropTypes.object.isRequired,
}

export default Form.create({})(FollowModal)