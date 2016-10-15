import React, { PropTypes } from 'react';
import { Link } from 'dva/router';
import { Button, Icon, Tabs, Form, Input, Popconfirm, Row, Col, Select, DatePicker, message } from 'antd';
const Option = Select.Option;

import Container from '../Container';
import Box from '../Box';
import BoxTable from '../BoxTable';
import BoxTabs from '../BoxTabs';
import OperationBox from '../OperationBox';

import styles from './index.less';

const ButtonGroup = Button.Group;
const TabPane = Tabs.TabPane;
const FormItem = Form.Item;
const List = ({ customers, form, children, dispatch, ...rest }) => {

    const { getFieldProps } = form;

    const onTableChange = (pagination, filters, sorter) => {
        dispatch({
            type: 'customers/setQuery',
            payload: {
                page: pagination.current,
                sort: sorter.field ? `${sorter.field},${sorter.order}` : undefined,
            },
        });
  }

    const options = {
        operation: [
            {
                label: "新增客户",
                url: '/customers/customers/add',
            },
            {
                label: "客户导入",
                url: '',
            },
            {
                label: "客户导出",
                url: '',
            },
            {
                label: "客户导入模板",
                url: '',
            },
            {
                label: "群发信息",
                url: '',
            },
        ]
    }
    const onChange = function() {
    }

    const formItemLayout = {
        labelCol: { span: 8 },
        wrapperCol: { span: 16 },
    };

    const onDelete = function(id) {
        dispatch({
            type: 'customers/deleteOne',
            payload: id,
        })
    }

    const selectBefore = function() {
        return (
            <Select defaultValue="name" style={{ width: 85 }} {...getFieldProps('type')}>
                <Option value="name">姓名</Option>
                <Option value="phone">手机号码</Option>
                <Option value="respons">客户负责人</Option>
                <Option value="status">状态</Option>
            </Select>
        )
    }
        
    return (
        <Container
            toolbar={ () => {
                return (
                    <div>
                        <Button type="ghost" size="large"><Icon type="reload"></Icon>刷新</Button>
                    </div>
                )
            } }
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
                                    type: 'customers/setQuery',
                                    payload: formData,
                                });

                            } } >搜索</Button>
                            <Button type="ghost" onClick={() => {
                                form.resetFields(['value'])
                            }}>重置</Button>
                        </ButtonGroup>

                    } >
                        <TabPane tab="快捷搜索" key="1">

                            <Row gutter={ 2 }>
                                <Col offset={1} sm={ 12 }>
                                    <FormItem
                                        { ...formItemLayout }
                                        >
                                        <Input addonBefore={selectBefore()} {...getFieldProps('value')}/>
                                    </FormItem>
                                </Col>
                            </Row>

                        </TabPane>
                    </Tabs>
                    <OperationBox options={options}/>
                </Form>
            </BoxTabs>
            <BoxTable
                noPadding

                columns={
                    [
                        {
                            title: '序号',
                            dataIndex: 'index',
                            key: 'id',
                            render: (text, record, index) => index+1,
                        },
                        {
                            title: '姓名',
                            dataIndex: 'name',
                            key: 'name',
                            sorter: true,
                        },
                        {
                            title: '状态',
                            dataIndex: 'statusName',
                            key: 'statusName',
                            sorter: true,
                        },
                        {
                            title: '手机号码',
                            dataIndex: 'phone',
                            key: 'phone',
                            sorter: true,
                        },
                        {
                            title: '楼盘名称',
                            dataIndex: 'buildingName',
                            key: 'buildingName',
                            sorter: true,
                        },
                        {
                            title: '装修进度',
                            dataIndex: 'decorateProcessName',
                            key: 'decorateProcessName',
                            sorter: true,
                        },
                        {
                            title: '客服负责人',
                            dataIndex: 'customerResponsibleName',
                            key: 'customerResponsibleName',
                            sorter: true,
                        },
                        {
                            title: '购物需求',
                            dataIndex: 'needTime',
                            key: 'needTime',
                            sorter: true,
                        },
                        {
                            title: '意向产品',
                            dataIndex: 'needProduct',
                            key: 'needProduct',
                            sorter: true,
                        },
                        {
                            title: '操作',
                            key: 'operation',
                            width: 150,
                            render: (text, record) => (
                                <table>
                                    <tbody>
                                        <tr>
                                            <td><Link to={`/customers/customers/${record.id}/houses`}><Icon type="edit" />房产信息</Link></td>
                                            <td>
                                                <Popconfirm title="确定要删除这个客户吗？" onConfirm={ () => onDelete(record.id) } onCancel={() => { } }>
                                                <a href="#"><Icon type="delete" />删除</a>
                                                </Popconfirm>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><Link to={`/customers/customers/${record.id}/tracks`}><Icon type="edit" />更进记录</Link></td>
                                            <td><Link to={`/customers/customers/edit/${record.id}`}><Icon type="edit" />编辑</Link></td>
                                        </tr>
                                        <tr>
                                            <td>
                                            {
                                                record.isEffectivity 
                                                ?
                                                <Popconfirm title="确定要将这个客户置为无效吗？" 
                                                    onConfirm={ () => {
                                                        dispatch({
                                                            type: 'customers/disabled',
                                                            payload: record.id
                                                        })
                                                    } } 
                                                    onCancel={() => { } }>
                                                    <a href="javascript:;">
                                                        <Icon type="edit" />
                                                        置为无效
                                                    </a>
                                                </Popconfirm> 
                                                :
                                                <Popconfirm title="确定要将这个客户置为有效吗？" 
                                                    onConfirm={() => {
                                                        dispatch({
                                                            type: 'customers/enabled',
                                                            payload: record.id
                                                        })
                                                    }} 
                                                    onCancel={() => { } }>
                                                    <a href="javascript:;">
                                                        <Icon type="edit" />
                                                        置为有效
                                                    </a>
                                                </Popconfirm> 
                                            }
                                            </td>
                                            <td></td>
                                        </tr>
                                    </tbody>
                                </table>
                            ),
                        }
                    ]
                }
                rowKey={ record => record.id }
                dataSource={ customers.customers }
                pagination={ customers.pagination }
                loading={ customers.loading }
                onChange={ onTableChange }
            />
        </Container>
    )
}

List.propTypes = {

}

export default Form.create({
  mapPropsToFields: (props) => {
    const query = props.customers.query;
    return {
      value: {
        value: query.value
      },
      type: {
        value: query.type || 'name'
      },
    }
  }
})(List);
