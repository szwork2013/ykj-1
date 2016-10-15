import React, { PropTypes, Component } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Cascader, Spin, Row, Col, DatePicker, Table, Icon, Popconfirm, InputNumber} from 'antd';
import { routerRedux } from 'dva/router';
import { Link } from 'dva/router';
import classNames from 'classnames';

import Container from '../Container';
import Box from '../Box';
import TreeBox from '../TreeBox';
import TableVariable from './TableVariable'

const FormItem = Form.Item;
const CheckboxGroup = Checkbox.Group;
const Option = Select.Option;
const InputGroup = Input.Group;

class Detail extends Component {

  constructor(props) {
    super();
    this.state = {
      currentCustOrg: undefined,
      currentCustPhone: undefined,
      currentCustFollower: undefined,
    }
  }

  getOrderGoodsData() {
    return this.table.getDataSource();
  }

  render() {
    const { orders, form, type, onSubmit, moreProps, dispatch, ...rest } = this.props;

    const { loading, queryCustomers } = orders;
    const { getFieldProps, getFieldError, isFieldValidating } = form;

    const dataSource = [
      {
        Number: 1,
        goodName: '小木板',
        goodType: 'v0.1.0-rc',
        status: '墙壁',
        payStatus: '件',
        addTime: 70,
        contact: 40,
        address: 0.01,
        orderOrigin: 40,
        follower: '90',
        orderOrigi: '10',
        time: '2016-10-10',
        followe: '木'
      },
      {
        Number: 2,
        goodName: '小木板',
        goodType: 'v0.1.0-rc',
        status: 'lal',
        payStatus: '件',
        addTime: 80,
        contact: 50,
        address: 0.01,
        orderOrigin: 50,
        follower: '90',
        orderOrigi: '20',
        time: '2016-11-10',
        followe: '木板'
      },
      {
        Number: 3,
        goodName: '小木板',
        goodType: 'v0.1.0-rc',
        status: 'mmm',
        payStatus: '件',
        addTime: 90,
        contact: 60,
        address: 0.01,
        orderOrigin: 60,
        follower: '90',
        orderOrigi: '30',
        time: '2016-12-10',
        followe: '木板质量'
      },
    ]

    const orderNoProps = getFieldProps('orderNo', {
        rules: [
            { required: true, min: 1, message: '订单号必须填写'}
        ]
    });

    const orderDateProps = getFieldProps('orderDate', {
        rules: [
            { required: true, message: '订单日期必须填写'}
        ]
    });

    const nameProps = getFieldProps('name', {
       rules: [
            { required: true, min: 1, message: '客户姓名不能小于1个字符'}
       ]
    });

    const phoneSecProps = getFieldProps('phoneSec', {
        rules: [
            { pattern: new RegExp(/^1[0-9]{10}$/), message: '请填写正确的第二联系人号码' },
        ],
    });

    const orderResponsibleProps = getFieldProps('orderResponsibleId', {
        rules: [
            { pattern: new RegExp(/^[0-9]+$/), message: '请填写正确的QQ' },
        ],
    });

    const orderTypeProps = getFieldProps('orderType', {
        rules: [
            { required: true, message: '请选择订单来源'}
        ],
    });

    const addressProps = getFieldProps('address', {
        rules: [
            { required: true, min: 1, message: '送货地址至少 1 个字符'}
        ],
    });
    
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
                <h3>基本资料</h3>
                <br/>
                <Row>
                  <Col sm={ 12 }>
                      <FormItem
                          { ...formItemLayout }
                          label = "订单号"
                          hasFeedback
                          help={isFieldValidating('orderNo') ? '校验中...' : (getFieldError('orderNo') || []).join(', ')}
                      >
                          <Input {...orderNoProps} size="default" style={ { width: '80%'} } disabled={ orders.submiting } />
                      </FormItem>
                  </Col>
                  <Col sm={ 12 }>
                      <FormItem
                          { ...formItemLayout }
                          label = "订单日期"
                          hasFeedback
                          help={isFieldValidating('orderDate') ? '校验中...' : (getFieldError('orderDate') || []).join(', ')}
                      >
                          <DatePicker {...orderDateProps} onChange={ (date, dateString) => {
                            form.setFieldsValue({
                              orderDate: dateString,
                            })
                          } } style={ { width: '80%'} } size="default" disabled={ orders.submiting }/>
                      </FormItem>
                  </Col>
                </Row>
                <Row>
                  <Col sm={ 12 }>
                      <FormItem
                          { ...formItemLayout }
                          label = "客户姓名"
                          help={isFieldValidating('name') ? '校验中...' : (getFieldError('name') || []).join(', ')}
                      >
                          <Select {...nameProps} combobox onchange={ (value) => {
                            dispatch({
                              type: 'orders/queryCustomer',
                              payload: value,
                            });
                          }} onSelect={ (value, option) => {
                            queryCustomers.map( (customer, index) => {
                              if(customer.id == value) {
                                this.setState({
                                  currentCustOrg: customer.organization,
                                  currentCustPhone: customer.phone,
                                  currentCustFollower: customer.responsibleName,
                                });
                              }
                            });
                          }} style={ { width: '80%'} } size="default" disabled={ orders.submiting }>
                          
                          </Select>
                      </FormItem>
                  </Col>
                  <Col sm={ 12 }>
                      <FormItem
                          { ...formItemLayout }
                          label = "客户单位名称"
                      >
                          <p className="ant-form-text" >{ this.state.currentCustOrg }</p>
                      </FormItem>
                  </Col>
                </Row>
                <Row>
                  <Col sm={ 12 }>
                      <FormItem
                          { ...formItemLayout }
                          label = "客户电话"   
                      >
                          <p className="ant-form-text" >{ this.state.currentCustPhone }</p>
                      </FormItem>
                  </Col>
                  <Col sm={ 12 }>
                      <FormItem
                          { ...formItemLayout }
                          label = "第二联系人电话"
                          hasFeedback
                          help={isFieldValidating('phoneSec') ? '校验中...' : (getFieldError('phoneSec') || []).join(', ')}
                      >
                          <Input {...phoneSecProps} size="default" style={ { width: '80%'} } disabled={ orders.submiting } />
                      </FormItem>
                  </Col>
                </Row>
                <Row>
                  <Col sm={ 12 }>
                      <FormItem
                          { ...formItemLayout }
                          label = "跟单人"
                          hasFeedback
                          help={isFieldValidating('orderResponsibleId') ? '校验中...' : (getFieldError('orderResponsibleId') || []).join(', ')}
                      >
                          <div className="ant-search-input-wrapper" >
                              <InputGroup className={classNames({ 'ant-search-input': true })} style={ { width: '80%'} } >
                                  <Input {...orderResponsibleProps} size="default" defaultValue={ this.state.currentCustFollower } disabled={ orders.submiting } />
                                  <div className="ant-input-group-wrap">
                                      <Button icon="edit" onClick={ () => {
                                        dispatch({
                                          type: 'orders/toggleModalShow',
                                          payload: true,
                                        });
                                      }} className={classNames({ 'ant-search-btn': true })} size="default" />
                                  </div>
                              </InputGroup>
                          </div>
                      </FormItem>
                  </Col>
                  <Col sm={ 12 }>
                      <FormItem
                          { ...formItemLayout }
                          label = "订单来源"
                          hasFeedback
                          help={isFieldValidating('orderType') ? '校验中...' : (getFieldError('orderType') || []).join(', ')}
                      >
                          <Select {...orderTypeProps} style={ { width: '80%'} } size="default" disabled={ orders.submiting }>
                            <Option key='1' value='1' >自然客户</Option>
                            <Option key='2' value='2' >活动</Option>
                          </Select>
                      </FormItem>
                  </Col>
                </Row>
                <Row>
                  <Col sm={ 12 }>
                      <FormItem
                          { ...formItemLayout }
                          label = "送货地址"
                          hasFeedback
                          help={isFieldValidating('address') ? '校验中...' : (getFieldError('address') || []).join(', ')}
                      >
                          <Input {...addressProps} size="default" style={ { width: '80%'} } disabled={ orders.submiting } />
                      </FormItem>
                  </Col>
                </Row>
                <h3>安装配送</h3>
                <br/>
                <Row>
                  <Col sm={ 12 } offset={ 2 }>
                    <FormItem
                        { ...formItemLayout } 
                    >
                        <Checkbox { ...getFieldProps('isNeedInstall') } defaultChecked onChange={ (e) => {
                          form.setFieldsValue({
                            isNeedInstall: e.target.checked,
                          })
                        }} >需要安装</Checkbox>
                        <Checkbox { ...getFieldProps('isNeedDelivery') } defaultChecked onChange={ (e) => {
                          form.setFieldsValue({
                            isNeedDelivery: e.target.checked,
                          })
                        }} >送货上门</Checkbox>
                    </FormItem>
                  </Col>
                </Row>
                <h3>报价审核</h3>
                <br/>
                <Row>
                  <Col sm={ 12 } offset={ 2 }>
                    <FormItem
                        { ...formItemLayout } 
                    >
                      <Checkbox { ...getFieldProps('isNeedAudit') } defaultChecked onChange={ (e) => {
                        form.setFieldsValue({
                            isNeedAudit: e.target.checked,
                          })
                      }} >需要审核</Checkbox>   
                    </FormItem>
                  </Col>
                </Row>
                <Row>
                  <Col sm={ 18 } offset={ 1 }>
                    <FormItem
                        labelCol={{ span: 2 }}
                        wrapperCol={{ span: 10 }}
                        label="客户备注"
                    >
                        <Input { ...getFieldProps('customerRemark') } size="default" style={ { width: '100%'} } disabled={ orders.submiting } />
                    </FormItem>
                  </Col>
                </Row>
                <Row>
                  <Col sm={ 18 } offset={ 1 }>
                    <FormItem
                        labelCol={{ span: 2 }}
                        wrapperCol={{ span: 10 }} 
                        label="内部备注"
                    >
                        <Input { ...getFieldProps('privateRemark') } size="default" style={ { width: '100%'} } disabled={ orders.submiting } />
                    </FormItem>
                  </Col>
                </Row> 
            </Box>
            <TableVariable
                ref={ (ref) => {
                  this.table = ref;
                }}
                columns={
                  [{
                    title: '序号',
                    dataIndex: 'Number',
                    key: 'Number',
                  },
                  {
                    title: '商品名称',
                    dataIndex: 'goodName',
                    key: 'goodName',
                  },
                  {
                    title: '商品型号',
                    dataIndex: 'goodType',
                    key: 'goodType',
                    width: '120px',
                  },
                  {
                    title: '位置',
                    dataIndex: 'status',
                    key: 'status',
                  },
                  {
                    title: '单位',
                    dataIndex: 'payStatus',
                    key: 'payStatus',
                    
                  },
                  {
                    title: '数量',
                    dataIndex: 'addTime',
                    key: 'addTime',
                  },
                  {
                    title: '原价',
                    dataIndex: 'contact',
                    key: 'contact',
                  },
                  {
                    title: '折扣率',
                    dataIndex: 'address',
                    key: 'address',
                    render: (text, record, index) => {
                      return <p className="ant-form-text">{ isNaN((record.orderOrigin/record.contact).toFixed(2)) ? `1.00` : `${(record.orderOrigin/record.contact).toFixed(2)}`}</p>
                    }
                  },
                  {
                    title: '折后单价',
                    dataIndex: 'orderOrigin',
                    key: 'orderOrigin',
                  },
                  {
                    title: '小计',
                    dataIndex: 'follower',
                    key: 'follower',
                    render: (text, record, index) => {
                      return <p className="ant-form-text">{ isNaN(record.addTime*record.orderOrigin) ? `0.00` : `${(record.addTime*record.orderOrigin)}`}</p>
                    }
                  },
                  {
                    title: '当前库存',
                    dataIndex: 'orderOrigi',
                    key: 'orderOrigi',
                  },
                  {
                    title: '预留库存',
                    dataIndex: 'yuliu',
                    key: 'yuliu',
                  },
                  {
                    title: '预留时间',
                    dataIndex: 'time',
                    key: 'time',
                  },
                  {
                    title: '备注',
                    dataIndex: 'followe',
                    key: 'followe',
                  },
                  {
                    title: '操作',
                    key: 'operation',
                  }]
                }
                type={ type }
                isAddable={ true }
              />
            <br/>
            <Row>
              <Col sm={ 24 } offset={ 19 }>
                <FormItem { ...formItemLayout } style={{ marginTop: 24 }}>
                  <Button type="primary" htmlType="submit" loading={ orders.submiting } onClick={ (e) => onSubmit(e, form, this.getOrderGoodsData()) }>确定</Button>
                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                  <Button type="ghost" onClick={ () => dispatch(routerRedux.goBack()) }>返回</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
      </Spin>
      </Container>
    )
  }
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
    const order = props.orders.current;
    return {
      id: {
        value: order.id
      },
      createDate: {
        value: order.createDate
      },
      modifyDate: {
        value: order.modifyDate
      },
      orderResponsibleId: {
        value: order.orderResponsibleId
      },
      customerId: {
        value: order.customerId
      },
      businessId: {
        value: order.businessId
      },
      type: {
        value: order.type
      },
      orderNo: {
        value: order.orderNo
      },
      orderDate: {
        value: order.orderDate
      },
      phoneSec: {
        value: order.phoneSec
      },
      orderSource: {
        value: order.orderSource
      },
      address: {
        value: order.address
      },
      customerRemark: {
        value: order.customerRemark
      },
      privateRemark: {
        value: order.privateRemark
      },
      priceBeforeDiscount: {
        value: order.priceBeforeDiscount
      },
      priceAfterDiscount: {
        value: order.priceAfterDiscount
      },
      strikePrice: {
        value: order.strikePrice
      },
      receiptPrice: {
        value: order.receiptPrice
      },
      isNeedDelivery: {
        value: order.isNeedDelivery
      },
      isNeedInstall: {
        value: order.isNeedInstall
      },
      isNeedMeasure: {
        value: order.isNeedMeasure
      },
      isNeedDesign: {
        value: order.isNeedDesign
      },
      isDel: {
        value: order.isDel
      },
      ...props.mapPropsToFields(order),
    }
  }
})(Detail);
