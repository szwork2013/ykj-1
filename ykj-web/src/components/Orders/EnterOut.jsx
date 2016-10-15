import React, { PropTypes, Component } from 'react';
import { Form, Input, Select, Button,  Spin, Row, Col, Table, InputNumber} from 'antd';
import { routerRedux } from 'dva/router';
import classNames from 'classnames';

import Container from '../Container';
import Box from '../Box';

const FormItem = Form.Item;

class EnterOut extends Component {

  render() {

    const { orders, form, dispatch, ...rest } = this.props;
  
    const { loading, submiting, currentGoodInfo } = orders;
    const { getFieldProps, getFieldError, isFieldValidating } = form;

    const backRarNumsProps = getFieldProps('backRarNums', {
        rules: [
            { type: 'number', message: '请输入退货数量'}
        ]
    })

    const fillRarNumsProps = getFieldProps('fillRarNums', {
        rules: [
            { type: 'number', message: '请输入补货数量'}
        ]
    })

    const formItemLayout = {
        labelCol: { span: 6 },
        wrapperCol: { span: 10 },
    };

    return (
      <Container
          {...rest}
      >
        <Spin spinning={ loading }>
          <Form horizontal >
            <Box>
              <h3>商品基本信息</h3>
              <br/>
              <Row>
                <Col sm={ 12 } >
                  <FormItem
                      { ...formItemLayout }
                      label = "商品名称"
                  >
                      <p className="ant-form-text" >&nbsp;&nbsp;&nbsp;&nbsp;{ `${currentGoodInfo.name}` }</p>
                  </FormItem>
                </Col>
                <Col sm={ 12 }>
                  <FormItem
                      { ...formItemLayout }
                      label = "型号"
                  >
                      <p className="ant-form-text" >&nbsp;&nbsp;&nbsp;&nbsp;{ `${currentGoodInfo.model}` }</p>
                  </FormItem>
                </Col>
              </Row>
              <Row>
                <Col sm={ 12 }>
                  <FormItem
                      { ...formItemLayout }
                      label = "品牌"
                  >
                      <p className="ant-form-text" >&nbsp;&nbsp;&nbsp;&nbsp;{ `${currentGoodInfo.supplierName}` }</p>
                  </FormItem>
                </Col>
                <Col sm={ 12 }>
                  <FormItem
                      { ...formItemLayout }
                      label = "计价单位"
                  >
                      <p className="ant-form-text" >&nbsp;&nbsp;&nbsp;&nbsp;{ `${currentGoodInfo.unit}` }</p>
                  </FormItem>
                </Col>
              </Row>
              <Row>
                <Col sm={ 12 }>
                  <FormItem
                      { ...formItemLayout }
                      label = "订单数量"
                  >
                      <p className="ant-form-text" >&nbsp;&nbsp;&nbsp;&nbsp;{ `${currentGoodInfo.orderGoodsNum}` }</p>
                  </FormItem>
                </Col>
                <Col sm={ 12 }>
                  <FormItem
                      { ...formItemLayout }
                      label = "已出库数量"
                  >
                      <p className="ant-form-text" >&nbsp;&nbsp;&nbsp;&nbsp;{ `${111}` }</p>
                  </FormItem>
                </Col>
              </Row>
              <Row>
                <Col sm={ 12 }>
                  <FormItem
                      { ...formItemLayout }
                      label = "成交单价"
                  >
                      <p className="ant-form-text" >&nbsp;&nbsp;&nbsp;&nbsp;{ `${currentGoodInfo.strikeUnitPrice}` }</p>
                  </FormItem>
                </Col>
              </Row>
            </Box>
            <Box>
              <h3>退补货数量和金额</h3>
              <br/>
              <Row>
                <Col sm={ 12 }>
                  <FormItem
                      { ...formItemLayout }
                      label = "退货数量"
                  >
                      <InputNumber {...backRarNumsProps} min={0} disabled={ submiting } size="default" style={ { width: '200px' } } />
                  </FormItem>
                </Col>
                <Col sm={ 12 }>
                  <FormItem
                      { ...formItemLayout }
                      label = "退货金额"
                  >
                      <p className="ant-form-text" >&nbsp;&nbsp;&nbsp;&nbsp;{ `${form.getFieldValue('backRarNums') ? `${ form.getFieldValue('backRarNums') * 5 }` : 0 }` }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;元</p>
                  </FormItem>
                </Col>
              </Row>
              <Row>
                <Col sm={ 12 }>
                  <FormItem
                      { ...formItemLayout }
                      label = "补货数量"
                  >
                      <InputNumber {...fillRarNumsProps} min={0} disabled={ submiting } size="default" style={ { width: '200px' } } />
                  </FormItem>
                </Col>
                <Col sm={ 12 }>
                  <FormItem
                      { ...formItemLayout }
                      label = "补货金额"
                  >
                      <p className="ant-form-text" >&nbsp;&nbsp;&nbsp;&nbsp;{ `${form.getFieldValue('fillRarNums') ? `${ form.getFieldValue('fillRarNums') * 5 }` : 0 }` }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;元</p>
                  </FormItem>
                </Col>
              </Row>
            </Box>
            <Row>
              <Col sm={ 24 } offset={ 20 }>
                <FormItem { ...formItemLayout } style={{ marginTop: 24 }}>
                  <Button type="primary" htmlType="submit" loading={ submiting } onClick={ (e) => {
                    e.preventDefault();
                    form.validateFieldsAndScroll((errors, values) => {
                        if (!!errors) {
                          return;
                        }
                        const formData = form.getFieldsValue();
                        formData.id = this.props.params.id;
                        formData.goodId = this.props.params.goodId;
                        dispatch({
                          type: 'orders/saveEnterOut',
                          payload: formData,
                        }) 
                    });
                  } }>保存</Button>
                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                  <Button type="ghost" onClick={ () => dispatch(routerRedux.goBack()) }>取消</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
        </Spin>
      </Container>
    )
  }
}

EnterOut.propTypes = {
  
}

export default Form.create({

})(EnterOut);
