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
const List = ({ items, orders, form, children, dispatch, ...rest }) => {
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

  const options = {
      operation: [
        {
            label: "新增订单",
            url: '/order/orders/add',
        },
        {
            label: "订单导入",
            url: '',
        },
        {
            label: "订单导出",
            url: '',
        },
        {
            label: "刷新",
            url: '',
        },
      ],
      filter: [
        {
          label: "全部",
          url: '',
          number: '',
        },
        {
          label: "预订单",
          url: '',
          number: '',
        },
        {
          label: "进行中",
          url: '',
          number: '',
        },
        {
          label: "退单",
          url: '',
          number: '',
        },
        {
          label: "完成",
          url: '',
          number: '',
        },
      ]
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
            title: '下单时间',
            dataIndex: 'orderDate',
            key: 'orderDate',
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
              <table>
                <tbody>
                  <tr>
                    <td>
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
                      {
                        record.type == 2 || record.type == 3 ?
                        (<Link to={ `/order/orders/print/${record.id}` }><Icon type="edit" />打印</Link>)
                        :
                        (<Link to={ `/order/orders/edit/${record.id}` }><Icon type="edit" />编辑</Link>)
                      }
                      
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
                      {
                        record.type == 2 ?
                        (
                          <Popconfirm title="确定要删除这个订单吗？" onConfirm={ () => {
                              dispatch({
                                type: 'orders/deleteOne',
                                payload: record.id,
                              })

                            } } onCancel={() => {} }>
                            <a href="#"><Icon type="delete" />删除</a>
                          </Popconfirm>
                        )
                        :
                        undefined
                      }
                    </td>
                  </tr>
                  {
                    record.type == 0 ?
                    (
                      <tr>
                        <td>
                          <span className="ant-divider"></span>
                              <Link to={ `/order/orders/${record.id}/measures` }><Icon type="edit" />测量</Link>
                              <span className="ant-divider"></span>
                              <Link to={ `/order/orders/${record.id}/designs` }><Icon type="edit" />设计</Link>
                          <Link to={ `/order/orders/print/${record.id}` }><Icon type="edit" />打印</Link>
                          <span className="ant-divider"></span>
                          <a href="javascript:void(0)" onClick={ () => {
                            dispatch({
                              type: 'orders/toggleAuditModal',
                              payload: {
                                currentOrderId: record.id,
                                AuditModalShow: true,
                              }
                            });
                          }} ><Icon type="edit" />审核</a>
                          <span className="ant-divider"></span>
                          <a href="javascript:void(0)" onClick={ () => {
                            dispatch({
                                type: 'orders/togglePayModal',
                                payload: {
                                  currentOrderId: record.id,
                                  payModalShow: true,
                                },
                            });   
                          }} ><Icon type="edit" />付款</a>
                        </td>
                      </tr>
                    )
                    :
                    (
                      record.type == 2 || record.type == 3 ?
                      (
                        undefined
                      )
                      :
                      (
                        record.payStatus == '未付清' ?
                        (
                          <tr>
                            <td>
                              <Link to={ `/order/orders/print/${record.id}` }><Icon type="edit" />打印</Link>
                              <span className="ant-divider"></span>
                              <Link to={ `/order/orders/edit/${record.id}` }><Icon type="edit" />退单</Link>
                              <span className="ant-divider"></span>
                              <a href="javascript:void(0)" onClick={ () => {
                                dispatch({
                                    type: 'orders/togglePayModal',
                                    payload: {
                                      currentOrderId: record.id,
                                      payModalShow: true,
                                    },
                                });   
                              }} ><Icon type="edit" />付款</a>
                            </td>  
                          </tr>
                        )
                        :
                        ( 
                          <tr>
                            <td>
                              <Link to={ `/order/orders/print/${record.id}` }><Icon type="edit" />打印</Link>
                              <span className="ant-divider"></span>
                              <Link to={ `/order/orders/edit/${record.id}` }><Icon type="edit" />退单</Link>
                              <span className="ant-divider"></span>
                              <Link to={ `/order/orders/${record.id}/measures` }><Icon type="edit" />测量</Link>
                              <span className="ant-divider"></span>
                              <Link to={ `/order/orders/${record.id}/designs` }><Icon type="edit" />设计</Link>
                            </td>
                          </tr>      
                        )
                      )
                     )
                  } 
                  {
                    record.type == 1 && record.payStatus == '全部付清' ?
                    (
                      <tr>
                        <td>
                          <a href="javascript:void(0)" onClick={ () => {
                            dispatch({
                                type: 'orders/togglePayModal',
                                payload: {
                                  currentOrderId: record.id,
                                  payModalShow: true,
                                },
                            });   
                          }} ><Icon type="edit" />付款</a>
                          <span className="ant-divider"></span>
                          <Link to={ `/order/orders/${record.id}/deliverys` }><Icon type="edit" />送货</Link>
                          <span className="ant-divider"></span>
                          <Link to={ `/order/orders/${record.id}/installations` }><Icon type="edit" />安装</Link>
                          <span className="ant-divider"></span>
                          <a href="javascript:void(0)" onClick={ () => {
                            dispatch({
                                type: 'orders/toggleFinishModal',
                                payload: {
                                  currentOrderId: record.id,
                                  finishModalShow: true,
                                },
                            });   
                          }} ><Icon type="edit" />完成</a>
                        </td>
                      </tr>
                    )
                    :
                    undefined
                  }
                </tbody>
              </table>
            ),
          }]
        }
        rowKey={ record => record.id }
        dataSource={ orders.orders }
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
      <PayModal
        visible={ orders.payModalShow }
        dispatch={ dispatch }
        
        submiting={ orders.submiting }
        onOk={ (data) => {
          dispatch({
            type: 'orders/pay',
            payload: data,
          });
        }}
      >
      </PayModal>
      <FinishModal
        visible={ orders.finishModalShow }
        dispatch={ dispatch }
        submiting={ orders.submiting }
        onOk={ (data) => {
          dispatch({
            type: 'orders/finish',
            payload: data,
          });
        }}
      >
      </FinishModal>
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

List.propTypes = {

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
})(List);
