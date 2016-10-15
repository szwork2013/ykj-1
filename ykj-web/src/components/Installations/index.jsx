import React, { PropTypes } from 'react';
import { Link } from 'dva/router';
import { Button, Icon, Tabs, Form, Input, Popconfirm, Row, Col, Modal, Checkbox } from 'antd';

import Container from '../Container';
import Box from '../Box';
import BoxTable from '../BoxTable';
import BoxTabs from '../BoxTabs';

import OrderCustomerInfo from '../CustomerInfo/OrderCustomerInfo'

import styles from './index.less';

const ButtonGroup = Button.Group;
const TabPane = Tabs.TabPane;
const FormItem = Form.Item;
const { confirm } = Modal;
const List = ({ customers, installations, form, children, dispatch, ...rest }) => {
  const { getFieldProps } = form;

  const options = customers.current

  const dataSource = [{
    id: 1,
    needTime: '2016-09-09',
  }]

  const onTableChange = (pagination, filters, sorter) => {
    dispatch({
      type: 'installations/setQuery',
      payload: {
        page: pagination.current,
        sort: sorter.field ? `${sorter.field},${sorter.order}` : undefined,
      },
    });
  }
  
  const formItemLayout = {
    labelCol: { span: 10 },
    wrapperCol: { span: 14 },
  };
  return (
    <Container
      { ...rest }
    >
      <h3>订单客户资料</h3>
      <br/>
      <OrderCustomerInfo
        dispatch={dispatch}
        options={options}
      />
      <Row>
        <Col span={12}><h3>安装安排历史</h3></Col>
        <Col span={12} style={{textAlign: 'right'}}>
          <Button type="primary" size="large">
            <Link to={`/order/orders/${rest.params.id}/installations/add`}>创建安装安排</Link>
          </Button>
        </Col>
      </Row>
      <br/> 
      <BoxTable
        noPadding
        
        columns={
          [
          {
            title: '序号',
            dataIndex: 'id',
            key: 'id',
            render: (text, record, index) => index + 1 
          },
          {
            title: '业主要求时间',
            dataIndex: 'needTime',
            key: 'needTime',
          },
          {
            title: '实际安装时间',
            dataIndex: 'serviceTime',
            key: 'serviceTime',
          },
          {
            title: '服务位置',
            dataIndex: 'servicePosition',
            key: 'servicePosition',
          },
          {
            title: '服务状态',
            dataIndex: 'serviceStatus',
            key: 'serviceStatus',
          },
          {
            title: '师傅姓名',
            dataIndex: 'clerkName',
            key: 'clerkName',
          },
          {
            title: '师傅电话',
            dataIndex: 'clerkPhone',
            key: 'clerkPhone',
          },
          {
            title: '服务评价',
            dataIndex: 'starLevel',
            key: 'starLevel',
          },
          {
            title: '费用',
            dataIndex: 'cost',
            key: 'cost',
          },
          {
            title: '结算标志',
            dataIndex: 'isClear',
            key: 'isClear',
            render: (text, record, index) => {
              return (
                text
                ?
                <Popconfirm title="确定要取消结算标识吗？" onConfirm={ () => {
                  dispatch({
                    type: 'installations/cancelStatement',
                    payload: record.id,
                  })

                }} onCancel={() => {} }>
                  <Checkbox checked={text}></Checkbox>
                </Popconfirm>
                :
                <Popconfirm title="确定要确认结算标识吗？" onConfirm={ () => {
                  dispatch({
                    type: 'installations/statement',
                    payload: record.id,
                  })

                }} onCancel={() => {} }>
                  <Checkbox checked={text}></Checkbox>
                </Popconfirm>
              )
            }
          },
          {
            title: '操作',
            key: 'operation',
            render: (text, record) => (
              <span>
                <Link to={`/order/orders/${rest.params.id}/installations/edit/${record.id}`}><Icon type="edit" />编辑</Link>
                <span className="ant-divider"></span>
                <Popconfirm title="确定要删除这个安装安排吗？" onConfirm={ () => {
                  
                  dispatch({
                    type: 'installations/deleteOne',
                    payload: record.id,
                  })

                } } onCancel={() => {} }>
                  <a href="#"><Icon type="delete" />删除</a>
                </Popconfirm>
                <span className="ant-divider"></span>
                <Link to={`/order/orders/${rest.params.id}/installations/note/${record.id}`}><Icon type="edit" />安装服务单</Link>
                <span className="ant-divider"></span>
                <Link to={`/order/orders/${rest.params.id}/installations/charges/${record.id}`}><Icon type="edit" />服务费计算</Link>
              </span>
            ),
          }]
        }
        rowKey={ record => record.id }
        dataSource={ dataSource }
        pagination={ installations.pagination }
        loading={ installations.loading }
        onChange={ onTableChange }
      />
    </Container>
  )
}

List.propTypes = {

}

export default Form.create({
  mapPropsToFields: (props) => {
    const query = props.installations.query;
    return {
    }
  }
})(List);
