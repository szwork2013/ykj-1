import React, { PropTypes } from 'react';
import { Link, routerRedux } from 'dva/router';
import { Button, Icon, Tabs, Form, Input, Popconfirm, Row, Col, Modal, Checkbox, InputNumber } from 'antd';

import Container from '../Container';
import Box from '../Box';
import BoxTable from '../BoxTable';
import BoxTabs from '../BoxTabs';

import OrderCustomerInfo from '../CustomerInfo/OrderCustomerInfo'


const ButtonGroup = Button.Group;
const TabPane = Tabs.TabPane;
const FormItem = Form.Item;
const { confirm } = Modal;
const DeliveryGoods = ({ customers, deliverys, deliveryGoods, form, onSubmit, children, dispatch, ...rest }) => {
  const { getFieldProps } = form;

  const onTableChange = (pagination, filters, sorter) => {
    dispatch({
      type: 'deliverys/setQuery',
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
    <div>
      <BoxTable
        noPadding
        
        columns={
          [
          {
            title: '序号',
            dataIndex: 'index',
            key: 'index',
            render: (text, record, index) => index + 1 
          },
          {
            title: '商品名称',
            dataIndex: 'name',
            key: 'needTime',
          },
          {
            title: '商品型号',
            dataIndex: 'status',
            key: 'status',
          },
          {
            title: '位置',
            dataIndex: 'position',
            key: 'position',
          },
          {
            title: '单位',
            dataIndex: 'unit',
            key: 'unit',
          },
          {
            title: '订单数量',
            dataIndex: 'orderNum',
            key: 'orderNum',
          },
          {
            title: '未送货数量',
            dataIndex: 'needDeliverNum',
            key: 'needDeliverNum',
          },
          {
            title: '本次送货数量',
            dataIndex: 'deliverNum',
            key: 'deliverNum',
            render: (text, record) => {
              return <InputNumber defaultValue={text} onChange={(value) => {
                dispatch({
                  type: 'deliveryGoods/setDeliverNum',
                  payload: value
                })
              }}/>
            }
          },
          {
            title: '当前库存数量',
            dataIndex: 'storageGoodsNum',
            key: 'storageGoodsNum',
          },
          {
            title: '备注',
            dataIndex: 'remark',
            key: 'remark',
          }]
        }
        rowKey={ record => record.id }
        dataSource={ deliveryGoods.deliveryGoods }
        pagination={ deliveryGoods.pagination }
        loading={ deliveryGoods.loading }
        onChange={ onTableChange }
      />
      <br/>
      <div style={{textAlign: 'right'}}>
        <Button type="primary" loading={ deliveryGoods.submiting } onClick={ (e) => onSubmit(e) }>保存</Button>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <Button type="ghost" onClick={ () => dispatch(routerRedux.goBack()) }>返回</Button>
      </div>
    </div>
  )
}

DeliveryGoods.propTypes = {

}

export default Form.create({
  mapPropsToFields: (props) => {
    const query = props.deliverys.query;
    return {
    }
  }
})(DeliveryGoods);
