import React, { PropTypes } from 'react';
import { Link } from 'dva/router';
import { Button, Icon, Tabs, Form, Input, Popconfirm, Row, Col, Modal, Select } from 'antd';

import Container from '../Container';
import Box from '../Box';
import BoxTable from '../BoxTable';
import BoxTabs from '../BoxTabs';

import styles from './index.less';

const ButtonGroup = Button.Group;
const TabPane = Tabs.TabPane;
const FormItem = Form.Item;
const Option = Select.Option;
const { confirm } = Modal;
const List = ({ suppliers, form, children, dispatch, ...rest }) => {
  const { getFieldProps } = form;

  const onTableChange = (pagination, filters, sorter) => {
    dispatch({
      type: 'suppliers/setQuery',
      payload: {
        page: pagination.current,
        sort: sorter.field ? `${sorter.field},${sorter.order}` : undefined,
      },
    });
  }
  
  const formItemLayout = {
    labelCol: { span: 8 },
    wrapperCol: { span: 16 },
  };

  const selectBefore = function() {
    return (
        <Select defaultValue="supplierName" style={{ width: 120 }}
          {...getFieldProps('type')}
        >
            <Option value="supplierName">品牌/供应商名称</Option>
            <Option value="contactName">联系人</Option>
        </Select>
    )
  }
  return (
    <Container
      toolbar={ () => {
        return (
          <div>
            <Button type="primary" size="large">
              <Link to="/indents/suppliers/add">创建品牌/供货商列表</Link>
            </Button>
            <Button type="ghost" size="large" onClick={ () => {

              const formData = form.getFieldsValue();
              dispatch({
                type: 'suppliers/setQuery',
                payload: {},
              });

            } }><Icon type="reload"></Icon>刷新</Button>
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
                  type: 'suppliers/setQuery',
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
                <Col sm={ 12 }>
                  <FormItem
                    { ...formItemLayout }
                  >
                    <Input addonBefore={selectBefore()} {...getFieldProps('value')}/>
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
            dataIndex: 'index',
            key: 'index',
            render: (text, record, index) => index + 1
          },
          {
            title: '品牌/供货商名称',
            dataIndex: 'supplierName',
            key: 'supplierName',
          },
          {
            title: '联系人',
            dataIndex: 'contactName',
            key: 'contactName',
          },
          {
            title: '联系电话',
            dataIndex: 'contactPhone',
            key: 'contactPhone',
          },
          {
            title: '联系地址',
            dataIndex: 'contactAddress',
            key: 'contactAddress',
          },
          {
            title: '操作',
            key: 'operation',
            render: (text, record) => (
              <span>
                <Link to={ `/indents/suppliers/edit/${record.id}` }><Icon type="edit" />编辑</Link>
                <span className="ant-divider"></span>
                <Popconfirm title="确定要删除这个品牌/供货商列表吗？" onConfirm={ () => {
                  
                  dispatch({
                    type: 'suppliers/deleteOne',
                    payload: record.id,
                  })

                } } onCancel={() => {} }>
                  <a href="#"><Icon type="delete" />删除</a>
                </Popconfirm>
              </span>
            ),
          }]
        }
        rowKey={ record => record.id }
        dataSource={ suppliers.suppliers }
        pagination={ suppliers.pagination }
        loading={ suppliers.loading }
        onChange={ onTableChange }
      />
    </Container>
  )
}

List.propTypes = {

}

export default Form.create({
  mapPropsToFields: (props) => {
    const query = props.suppliers.query;
    return {
      value: {
        value: query.value
      },
      type: {
        value: query.type || 'supplierName'
      },
    }
  }
})(List);
