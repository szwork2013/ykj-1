import React, { PropTypes, Component } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Cascader, Spin, Row, Col, DatePicker, Table, Icon, Popconfirm  } from 'antd';
import { routerRedux } from 'dva/router';
import { Link } from 'dva/router';
import classNames from 'classnames';

import Container from '../Container';
import Box from '../Box';
import TreeBox from '../TreeBox'; 
import AuditModal from './AuditModal';
import TableVariable from './TableVariable';

const FormItem = Form.Item;
const CheckboxGroup = Checkbox.Group;
const Option = Select.Option;
const InputGroup = Input.Group;

class EditDetail extends Component {

  constructor(props) {
    super();
    this.state = {
      currentCustOrg: undefined,
      currentCustPhone: undefined,
      currentCustFollower: undefined,
    }
  }

  render() {
    const { orders, form, type, onSubmit, moreProps, dispatch, ...rest } = this.props;

    const { loading, queryCustomers } = orders;
    const { getFieldProps, getFieldError, isFieldValidating } = form;

    const price = {a:17645.00, b:15733.00, c:1912.00}
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
        yuliu: '3',
        time: '2016-10-10',
        followe: '木',
        outNumber: '2',
      },
      {
        Number: 2,
        goodName: '小木板',
        goodType: 'v0.1.0-rc',
        status: 'lal',
        payStatus: '件',
        addTime: 80,
        contact: 50,
        address: 0.02,
        orderOrigin: 50,
        follower: '90',
        orderOrigi: '20',
        yuliu: '4',
        time: '2016-11-10',
        followe: '木板',
        outNumber: '2',
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
        yuliu: '5',
        time: '2016-12-10',
        followe: '木板质量',
        outNumber: '2',
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
      <Spin spinning={ false }>
        
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
                <Row>
                  <Col sm={ 24 } offset={ 19 }>
                    <FormItem { ...formItemLayout } style={{ marginTop: 24 }}>
                      <Button type="primary" htmlType="submit" loading={ orders.submiting } onClick={ (e) => onSubmit(e, form) }>确定</Button>
                      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <Button type="ghost" onClick={ () => dispatch(routerRedux.goBack()) }>返回</Button>
                    </FormItem>
                  </Col>
                </Row>
            </Box>
              <TableVariable
                  columns={
                    [{
                      title: '序号',
                      dataIndex: 'Number',
                      key: 'Number',
                      render: (text, record, index) => (index + 1)
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
                      title: '已出库数',
                      dataIndex: 'outNumber',
                      key: 'outNumber',
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
                  dataSource={ dataSource }
                  dispatch={dispatch}
                  goodsEditing={orders.goodsEditing}
                  onOk={ (orderGoods) => {
                    this.setState({
                      orderGoods: orderGoods,
                    })
                  }}
                />
                <Row>
                    <Col sm={ 24 } offset={ 19 }>
                    <FormItem { ...formItemLayout } style={{ marginTop: 24 }}>
                        <Button type="primary" htmlType="submit" loading={ orders.submiting } onClick={ (e) => onSubmit(e, form) }>确定</Button>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <Button type="ghost" onClick={ () => dispatch(routerRedux.goBack()) }>返回</Button>
                    </FormItem>
                    </Col>
                </Row>
            <Box>
              <h3>收款/退款记录</h3>
              <br/>
              <Table
                  columns={
                    [{
                      title: '序号',
                      dataIndex: 'id',
                      key: 'id',
                      render: (text, record, index) => {
                        return `${index + 1}`;
                      }
                    },
                    {
                      title: '类目',
                      dataIndex: 'category',
                      key: 'category',
                    },
                    {
                      title: '金额',
                      dataIndex: 'payment',
                      key: 'payment',
                    },
                    {
                      title: '付款方式',
                      dataIndex: 'payWay',
                      key: 'payWay',
                    },
                    {
                      title: '摘要',
                      dataIndex: 'remark',
                      key: 'remark',
                    },
                    {
                      title: '记录人',
                      dataIndex: 'recorder',
                      key: 'recorder',
                    },
                    {
                      title: '日期',
                      dataIndex: 'recorderDate',
                      key: 'recorderDate',
                    }]
                  }
                  style={ { width: '85%', marginLeft: '90px'} }
                />
                <Row>
                  <Col sm={ 24 } offset={ 14 }>
                    <FormItem { ...formItemLayout } style={{ marginTop: 24 }}>
                      <p className="ant-form-text" >{ `成交价格（${price.a}）- 已收款（${price.b}）= 未收款（${price.c}）（单位：元）` }</p>
                    </FormItem>
                  </Col>
                </Row>
            </Box>
            <Box>
              <h3>测量设计信息</h3>
              <br/>
              <Table
                  columns={
                    [{
                      title: '序号',
                      dataIndex: 'id',
                      key: 'id',
                      render: (text, record, index) => {
                        return `${index + 1}`;
                      }
                    },
                    {
                      title: '文件名',
                      dataIndex: 'attachmentFilename',
                      key: 'attachmentFilename',
                      render: (text, record) => {
                        return <a href="javascript:void(0)" onClick={ () => {
                          dispatch({

                          });
                        }} >{ `${text}` }</a>
                      }
                    },
                    {
                      title: '文件大小',
                      dataIndex: 'attachmentSize',
                      key: 'attachmentSize',
                    },
                    {
                      title: '上传者',
                      dataIndex: 'uploadPersonName',
                      key: 'uploadPersonName',
                    },
                    {
                      title: '上传日期',
                      dataIndex: 'uploadDate',
                      key: 'uploadDate',
                    },
                    {
                      title: '备注',
                      dataIndex: 'remark',
                      key: 'remark',
                    },
                    {
                      title: '操作',
                      key: 'operation',
                      render: (text, record, index) =>{
                        <span>
                          <Popconfirm title="确定要删除这个选项吗？" onConfirm={ () => {
                            
                            dispatch({
                              type: 'orders/deleteOne',
                              payload: record.id,
                            })

                          } } onCancel={() => {} }>
                            <a href="#"><Icon type="delete" />删除</a>
                          </Popconfirm>
                        </span>
                      }
                    }]
                  }
                  style={ { width: '85%', marginLeft: '90px'} }
                />
            </Box>
            <Box>
              <h3>退补货信息</h3>
              <br/>
              <Table
                  columns={
                    [{
                      title: '序号',
                      dataIndex: 'id',
                      key: 'id',
                      render: (text, record, index) => {
                        return `${index + 1}`;
                      }
                    },
                    {
                      title: '商品名称',
                      dataIndex: 'goodsName',
                      key: 'goodsName',
                    },
                    {
                      title: '品牌',
                      dataIndex: 'supplierName',
                      key: 'supplierName',
                    },
                    {
                      title: '商品型号',
                      dataIndex: 'model',
                      key: 'model',
                    },
                    {
                      title: '单位',
                      dataIndex: 'unit',
                      key: 'unit',
                    },
                    {
                      title: '退货数量',
                      dataIndex: 'rarNums',
                      key: 'rarNums',
                    },
                    {
                      title: '补货数量',
                      dataIndex: 'rarsNums',
                      key: 'rarsNums',
                    },
                    {
                      title: '成交单价',
                      dataIndex: 'strikeUnitPrice',
                      key: 'strikeUnitPrice',
                    },
                    {
                      title: '金额',
                      dataIndex: 'money',
                      key: 'money',
                    },
                    {
                      title: '操作',
                      key: 'operation',
                      render: (text, record, index) =>{
                        <span>
                          <Popconfirm title="确定要删除这个选项吗？" onConfirm={ () => {
                            
                            dispatch({
                              type: 'orders/deleteOne',
                              payload: record.id,
                            })

                          } } onCancel={() => {} }>
                            <a href="#"><Icon type="delete" />删除</a>
                          </Popconfirm>
                        </span>
                      }
                    }]
                  }
                  style={ { width: '85%', marginLeft: '90px'} }
                />
            </Box>
          </Form>
      </Spin>
      <AuditModal
        visible={ orders.AuditModalShow }
        dispatch={ dispatch }
        orders={ orders }
        onOk={ () => {
          dispatch({
            type: 'orders/audit',
            payload: {},
          });
        }}
      >
      </AuditModal>
      </Container>
    )
  }
}

EditDetail.propTypes = {
  onSubmit: PropTypes.func.isRequired,
  moreProps: PropTypes.func.isRequired,
  mapPropsToFields: PropTypes.func.isRequired,
}

EditDetail.defaultProps = {
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
})(EditDetail);
