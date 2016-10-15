import React, { PropTypes } from 'react';
import { connect } from 'dva';
import { Link } from 'dva/router';
import { Icon, Button, Tabs, Form, Input, Select, Popconfirm, Row, Col } from 'antd';

import Box from '../Box';
import BoxTable from '../BoxTable';
import BoxTabs from '../BoxTabs';

import Container from '../Container';

import { querySelector } from '../../models/role/selectors'
import styles from './index.less'

const ButtonGroup = Button.Group;
const FormItem = Form.Item;
const Option = Select.Option;
const TabPane = Tabs.TabPane;
const RoleList = ({ list, pagination, loading, roleType, dispatch, form, ...rest }) => {
  const { getFieldProps } = form;
  const formItemLayout = {
    labelCol: { span: 8 },
    wrapperCol: { span: 16 },
  };

  return (
    <Container
      { ...rest }
      toolbar={ () => {
        return (
          <div>
            <Button type="primary" size="large"><Link to="/config/role/add" >新增</Link></Button>
            <Button type="ghost" size="large" onClick={() => {
                const formData = form.getFieldsValue();
                dispatch({
                  type: 'role/query',
                  payload: formData,
                });
              }}>
            <Icon type="reload"/>刷新</Button>
          </div>
        )
      } }
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
                  type: 'role/query',
                  payload: formData,
                });

              } } >搜索</Button>
            </ButtonGroup>

          } >
            <TabPane tab="快捷搜索" key="1">
            
              <Row gutter={ 2 }>
                <Col sm={ 4 }>
                  <FormItem
                    { ...formItemLayout }
                    label="角色名"
                  >
                    <Input {...getFieldProps('name')} size="default" />
                  </FormItem>
                  
                </Col>
                <Col sm={ 4 }>
                  <FormItem
                    { ...formItemLayout }
                    label="角色类别"
                  >
                    <Select {...getFieldProps('type')}>
                      <Option value="-1">-</Option>
                      <Option value="1">集团</Option>
                      <Option value="2">全区域</Option>
                      <Option value="3">区域</Option>
                      <Option value="4">园区</Option>
                    </Select>
                  </FormItem>
                </Col>
                <Col sm={ 4 }>
                  <FormItem
                    { ...formItemLayout }
                    label="角色归属"
                  >
                    <Input {...getFieldProps('belong')} size="default" />
                  </FormItem>
                </Col>
              </Row>
            
            </TabPane>
          </Tabs>
        </Form>
      </BoxTabs>
      <BoxTable
        loading={loading}
        className="role-table"
        columns={[
          {
            title: '角色名',
            dataIndex: 'name',
            key: 'name',
            sorter: false,
          },
          {
            title: '角色类别',
            dataIndex: 'typeName',
            key: 'typeName',
            sorter: false,
          },
          {
            title: '角色归属',
            dataIndex: 'belongName',
            key: 'belongName',
            sorter: false,
          },
          {
            title: '创建时间',
            dataIndex: 'createDate',
            key: 'createDate',
            sorter: false
          },
          {
            title: '关联用户数',
            dataIndex: 'userNum',
            key: 'userNum',
            sorter: false
          },
          {
            title: '操作',
            dataIndex: 'id',
            key: 'id',
            sorter: false,
            render: (roleId, role) => {
              return (
                <span>
                  <Link to={`/config/role/edit/${role.id}`}>
                    <Icon type="edit" />编辑
                  </Link>
                  <span className="ant-divider"></span>
                  <Popconfirm title={`确定要删除角色【${role.name}】吗？`} onConfirm={ () => { dispatch({type: 'role/delete', payload: {id: roleId, dispatch, formData: form.getFieldsValue()}}) } } onCancel={() => {} }>
                    <a href="#"><Icon type="delete" />删除</a>
                  </Popconfirm>
                </span>
              )
            }
          }
        ]}
        dataSource={ list }
      />
    </Container>
  )
}

export default Form.create()(RoleList);
