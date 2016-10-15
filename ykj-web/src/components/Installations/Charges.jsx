import React, { PropTypes } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Cascader, Spin, Row, Col, Upload, Icon, DatePicker, InputNumber, Table } from 'antd';
import { routerRedux } from 'dva/router';

import Container from '../Container';
import Box from '../Box';

const FormItem = Form.Item;
const CheckboxGroup = Checkbox.Group;
const Option = Select.Option;

const Charges = ({ installations, orders, form, type, onSubmit, moreProps, dispatch, ...rest }) => {
  const { loading } = installations;
  const { getFieldProps, getFieldError, isFieldValidating, setFieldsValue } = form;

  const installation = installations.current
  const dataSource = [{id:1}]
  
  const rowItemLayout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 12 },
  }
  const fullRowLayout = {
    labelCol: { span: 2 },
    wrapperCol: { span: 20 },
  }
  return (
    <Container
      {...rest}
    >
    <Spin spinning={ loading }>
      <Box
      >
        <Form horizontal >
          <h3>订单客户资料</h3>
          <br/>
          <Box>	
            <Row>
                <Col span="8">
                    <FormItem
                        {...rowItemLayout}
                        label="订单号"
                    >
                        <p className="ant-form-text" >{ orders.current.orderNo }</p>
                    </FormItem>
                </Col>
                <Col span="8">
                    <FormItem
                        {...rowItemLayout}
                        label="客户姓名"
                    >
                        <p className="ant-form-text" >{ orders.current.customerName }</p>
                    </FormItem>
                </Col>
                <Col span="8">
                    <FormItem
                        {...rowItemLayout}
                        label="客户电话"
                    >
                        <p className="ant-form-text" >{ orders.current.phone }</p>
                    </FormItem>
                </Col>
            </Row>
            <Row>
                <Col span="24">
                    <FormItem
                        {...fullRowLayout}
                        label="送货地址"
                    >
                        <p className="ant-form-text" >{ orders.current.address }</p>
                    </FormItem>
                </Col>
            </Row>
          </Box>
          <br/>
          <Box>
            <div style={ { width: '90%', marginLeft: '5%', border: '2px solid #eee', textAlign: 'center', background: '#eee'} }>
                <h2>服务费计算项目</h2>
            </div>
            <br/>
            <Table
                noPadding

                columns={
                [{
                    title: '序号',
                    dataIndex: 'id',
                    key: 'id',
                },
                {
                    title: '服务人员',
                    dataIndex: 'orderNumber',
                    key: 'orderNumber',
                    render: (text, record, index) => {
                        return <p className="ant-form-text" >张三</p>
                    }
                },
                {
                    title: '项目',
                    dataIndex: 'status',
                    key: 'status',     
                },
                {
                    title: '数量/营业额',
                    dataIndex: 'payStatus',
                    key: 'payStatus',
                    
                },
                {
                    title: '单价',
                    dataIndex: 'addTime',
                    key: 'addTime',
                },
                {
                    title: '小计',
                    dataIndex: 'contact',
                    key: 'contact',
                },
                {
                    title: '备注',
                    dataIndex: 'address',
                    key: 'address',
                },
                {
                    title: '折后单价',
                    dataIndex: 'orderOrigin',
                    key: 'orderOrigin',
                }]
                }
                style={ { width: '90%', marginLeft: '5%'} }
                rowKey={ record => record.id }
                dataSource={ dataSource }
                loading={ installations.loading }
            />
          </Box>
          <FormItem wrapperCol={{ span: 15, offset: 19 }} style={{ marginTop: 24 }}>
            <Button type="primary" htmlType="submit" loading={ installations.submiting } onClick={ (e) => onSubmit(e, form) }>确定</Button>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <Button type="ghost" onClick={ () => dispatch(routerRedux.goBack()) }>返回</Button>
          </FormItem>
        </Form>
      </Box>
    </Spin>
    </Container>
  )
}

Charges.propTypes = {
  
}

export default Form.create({
  
})(Charges);
