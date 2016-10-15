import React, { PropTypes } from 'react';
import { Link } from 'dva/router';
import { Button, Icon, Tabs, Form, Input, Popconfirm, Row, Col, Select, DatePicker } from 'antd';

import Container from '../../Container';
import Box from '../../Box';
import BoxTable from '../../BoxTable';
import BoxTabs from '../../BoxTabs';

import styles from './index.less';

const ButtonGroup = Button.Group;
const TabPane = Tabs.TabPane;
const FormItem = Form.Item;
const SelectOption = Select.Option;
const RangePicker = DatePicker.RangePicker;
const UserList = ({ form, users, children, dispatch, ...rest }) => {
  const { getFieldProps } = form;

  const formItemLayout = {
    labelCol: { span: 8 },
    wrapperCol: { span: 16 },
  };

  return (
    <Container
      toolbar={ () => {
        return (
          <div>
            <Button type="primary" size="large">
              <Link to="/config/users/add">新增员工</Link>
            </Button>
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
                  type: 'users/query',
                  payload: formData,
                });

              } } >查询</Button>
              <Button type="ghost" >重置</Button>
            </ButtonGroup>

          } >
            <TabPane tab="快捷搜索" key="1">
            
              <Row gutter={ 2 } >
                <Col sm={ 4 }>
                  <FormItem
                    { ...formItemLayout }
                    label="员工姓名"
                  >
                    <Input  type="text"  {...getFieldProps('userName')} size="default" />
                  </FormItem>
                  
                </Col>
                <Col sm={ 4 }>
                  <FormItem
                    { ...formItemLayout }
                    label="手机号码"
                  >
                    <Input  type="text" {...getFieldProps('phone')} size="default" />
                  </FormItem>
                </Col>
                <Col sm={ 4 }>
                  <FormItem
                    { ...formItemLayout }
                    label="邮箱"
                  >
                    <Input  type="text" {...getFieldProps('email')} size="default" />
                  </FormItem>
                </Col>
              </Row>
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
            dataIndex: 'number',
            key: 'number',
          },
          {
            title: '员工姓名',
            dataIndex: 'userName',
            key: 'userName',
          },
          {
            title: '员工身份',
            dataIndex: 'roleType',
            key: 'roleType',
          },
          {
            title: '性别',
            dataIndex: 'sex',
            key: 'sex',
            
          },
          {
            title: '出生日期',
            dataIndex: 'birthday',
            key: 'birthday',
          },
          {
            title: '手机号码',
            dataIndex: 'phone',
            key: 'phone',
          },
          {
            title: 'QQ',
            dataIndex: 'qqNumber',
            key: 'qqNumber',
          },
          {
            title: '邮箱',
            dataIndex: 'email',
            key: 'email',
          },
          {
            title: '操作',
            dataIndex: 'id',
            key: 'id',
            sorter: false,
            render: (text, user) => {
              return (  
                    <span>
                      <Link to={ `/config/users/edit/${user.id}` }><Icon type="edit" />编辑</Link>
                      <span className="ant-divider"></span>
                      <Popconfirm title={`确定要删除【${user.userName}】这个员工吗？`} onConfirm={ () => {
                        dispatch({
                          type: "users/delete",
                          payload: {
                            id: user.id,
                          }
                        });
                      } } onCancel={() => {} }>
                        <a href="#"><Icon type="delete" />删除</a>
                      </Popconfirm>
                    </span>   
              )     
            },
          }]
        }
        dataSource={ users.users }
      />
    </Container>
  )
}

UserList.propTypes = {

}

export default Form.create({
    mapPropsToFields: (props) => {
        const query = props.users.query;
        return {
            
        }
    }
})(UserList);
