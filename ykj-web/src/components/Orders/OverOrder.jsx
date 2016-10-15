import React, { PropTypes } from 'react';
import { Link } from 'dva/router';
import { Button, Icon, Tabs, Form, Input, Popconfirm, Row, Col, Modal, Select, DatePicker } from 'antd';

import Container from '../Container';
import Box from '../Box';
import BoxTable from '../BoxTable';
import BoxTabs from '../BoxTabs';
import OperationBox from '../OperationBox';
import FinishModal from './FinishModal';
import AuditModal from './AuditModal';
import PayModal from './PayModal';
import UploadModal from './UploadModal';
import FollowModal from './FollowModal';

import styles from './index.less';

const ButtonGroup = Button.Group;
const TabPane = Tabs.TabPane;
const FormItem = Form.Item;
const Option = Select.Option;
const RangePicker = DatePicker.RangePicker;
const { confirm } = Modal;

const OverOrder = ({ items, orders, form, children, dispatch, ...rest }) => {
  const { getFieldProps } = form;
  
  const onTableChange = (pagination, filters, sorter) => {
    dispatch({
      type: 'orders/setQuery',
      payload: {
        page: pagination.current,
        sort: sorter.field ? `${sorter.field},${sorter.order}` : undefined,
      },
    });
  }

  const dataSource = [
    {
      id: 1,
      orderNo: `MDSL2345678`,
      type: 2,
      payStatus: '已付定金',
      orderDate: `2016-09-09`,
      address: '浙江省杭州市西湖区小鹤山',
      orderType: '自然客户',
      orderResponsibleName: '张伟刚',
    },
  ]

  const options = {
      operation: [
        {
            label: "订单导出",
            url: '',
        },
        {
            label: "刷新",
            url: '',
        },
      ],
    }
  
  const formItemLayout = {
    labelCol: { span: 10 },
    wrapperCol: { span: 14 },
  };

  return (
    <Container
      { ...rest }
    >
      <BoxTabs>
        <Form
          horizontal
        >
          <Tabs defaultActiveKey="1" tabBarExtraContent={ 

            <ButtonGroup>
              <Button type="primary" onClick={ () => {

                const formData = form.getFieldsValue();
                formData.type = 2;
                dispatch({
                  type: 'orders/setQuery',
                  payload: formData,
                });

              } } >查询</Button>
              <Button type="ghost" >重置</Button>
            </ButtonGroup>

          } >
            <TabPane tab="快捷搜索" key="1">
            
              <Row gutter={ 1 } >
                <Col sm={ 6 }>
                  <FormItem
                    { ...formItemLayout }
                  >
                    <Select
                      showSearch
                      placeholder = "跟单人、下单人、服务人员、全部"
                      notFoundContent = "无对应项"
                      style = { { width:250 } }
                      onChange = { () => {

                      }}
                    >
                      {
                        items.map( (item, index) => {
                          const options = [];
                          options.push(
                            <Option key={ index }>{`item${index}`}</Option>
                          )
                          return options;
                        })
                      }
                    </Select>
                  </FormItem>
                  
                </Col>
                <Col sm={ 4 }>
                  <FormItem
                    { ...formItemLayout }
                  >
                    <Select
                      showSearch
                      placeholder = "下单时间、完成时间"
                      notFoundContent = "无对应项"
                      style = { { width:150 } }
                      onChange = { () => {

                      }}
                    >
                      {
                        items.map( (item, index) => {
                          const options = [];
                          options.push(
                            <Option key={ index }>{`item${index}`}</Option>
                          )
                          return options;
                        })
                      }
                    </Select>
                  </FormItem>
                </Col>
                <Col sm={ 4 }>
                  <FormItem
                    { ...formItemLayout }
                    label="时间"
                  >
                    <RangePicker
                      style={{ width: 200 }} 
                      onChange={ () => {

                      }}
                    >
                    </RangePicker>
                  </FormItem>
                </Col>
                <Col sm={ 7 } offset={ 3 }>
                  <FormItem
                    { ...formItemLayout }
                  >
                    <Input  placeholder = "请输入关键字" size="default" />
                  </FormItem>
                </Col>
              </Row>
              <OperationBox options={options}/>
            </TabPane>
          </Tabs>
        </Form>
      </BoxTabs>
      <BoxTable
        noPadding
        
        columns={
          [
          {
            title: '序号',
            dataIndex: 'id',
            key: 'id',
            render: (text, record, index) => {
              return `${index + 1}`
            }
          },
          {
            title: '订单号',
            dataIndex: 'orderNo',
            key: 'orderNo',
          },
          {
            title: '状态',
            dataIndex: 'type',
            key: 'type',
            render: (text, record, index) => {
              return text == 0 ? `预订单` : (
                text == 1 ? `进行中` : (
                  text == 2 ? `已完成` : (
                    text == 3 ? `退单` : `暂无`
                  )
                )
              )
            }
          },
          {
            title: '付款状态',
            dataIndex: 'payStatus',
            key: 'payStatus',
            
          },
          {
            title: '完成时间',
            dataIndex: 'finishDate',
            key: 'finishDate',
          },
          {
            title: '客户姓名',
            dataIndex: 'customerName',
            key: 'customerName',
          },
          {
            title: '送货地址',
            dataIndex: 'address',
            key: 'address',
          },
          {
            title: '订单来源',
            dataIndex: 'orderType',
            key: 'orderType',
          },
          {
            title: '跟单人',
            dataIndex: 'orderResponsibleName',
            key: 'orderResponsibleName',
          },
          {
            title: '操作',
            key: 'operation',
            render: (text, record) => (
              <span>
                <a href="javascript:void(0)" onClick={ () => {
                    dispatch({
                        type: 'orders/queryFollowStatus',
                        payload: {
                        currentOrderId: record.id,
                        FollowModalShow: true,
                        }
                    });
                    }} ><Icon type="upload" />跟踪</a>
                <span className="ant-divider"></span>
                <Popconfirm title="确定要删除这个订单吗？" onConfirm={ () => {
                        dispatch({
                        type: 'orders/deleteOne',
                        payload: record.id,
                        })

                    } } onCancel={() => {} }>
                    <a href="#"><Icon type="delete" />删除</a>
                </Popconfirm>
                <span className="ant-divider"></span>
                <a href="javascript:void(0)" onClick={ () => {
                    dispatch({
                        type: 'orders/toggleUploadModal',
                        payload: {
                        currentOrderId: record.id,
                        UploadModalShow: true,
                        }
                    });
                }} ><Icon type="upload" />订单上传</a>
                <span className="ant-divider"></span>
                <Link to={ `/order/orders/print/${record.id}` }><Icon type="edit" />打印</Link>
              </span>
            ),
          }]
        }
        rowKey={ record => record.id }
        dataSource={ dataSource }
        pagination={ orders.pagination }
        loading={ orders.loading }
        onChange={ onTableChange }
      />
      <FollowModal
        visible={ orders.FollowModalShow }
        dispatch={ dispatch }
        
      >
      </FollowModal>
      <UploadModal
        visible={ orders.UploadModalShow }
        dispatch={ dispatch }
        orders={ orders }
        submiting={ orders.submiting }
        onOk={ (data) => {
          dispatch({
            type: 'orders/uploadOrder',
            payload: data,
          });
        }}
      >
      </UploadModal>
    </Container>
  )
}

OverOrder.propTypes = {

}

export default Form.create({
  mapPropsToFields: (props) => {
    const query = props.orders.query;
    return {
      createDate: {
        value: query.createDate
      },
      orderResponsibleId: {
        value: query.orderResponsibleId
      },
      orderNo: {
        value: query.orderNo
      },
    }
  }
})(OverOrder);
