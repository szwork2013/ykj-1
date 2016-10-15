import React, { PropTypes } from 'react';
import { Link } from 'dva/router';
import { Button, Icon, Tabs, Form, Input, Popconfirm, Row, Col, Modal } from 'antd';

import Container from '../Container';
import Box from '../Box';
import BoxTable from '../BoxTable';
import BoxTabs from '../BoxTabs';

import styles from './index.less';

const ButtonGroup = Button.Group;
const TabPane = Tabs.TabPane;
const FormItem = Form.Item;
const { confirm } = Modal;
const List = ({ ${_moduleName}s, form, children, dispatch, ...rest }) => {
  const { getFieldProps } = form;

  const onTableChange = (pagination, filters, sorter) => {
    dispatch({
      type: '${_moduleName}s/setQuery',
      payload: {
        page: pagination.current,
        sort: sorter.field ? `${"$"}{sorter.field},${"$"}{sorter.order}` : undefined,
      },
    });
  }
  
  <#if canDelAll>
  const rowSelection = {
    selectedRowKeys: ${_moduleName}s.selectedRowKeys,
    onChange: (selectedRowKeys) => {
      dispatch({
        type: '${_moduleName}s/selectRows',
        payload: selectedRowKeys,
      })
    }
  }
  
  </#if>
  const formItemLayout = {
    labelCol: { span: 10 },
    wrapperCol: { span: 14 },
  };
  return (
    <Container
      toolbar={ () => {
        return (
          <div>
            <Button type="primary" size="large">
              <Link to="/${packageName}/${_moduleName}s/add">创建${moduleCName}</Link>
            </Button>
            <Button type="ghost" size="large" onClick={ () => {

              const formData = form.getFieldsValue();
              dispatch({
                type: '${_moduleName}s/setQuery',
                payload: {},
              });

            } }><Icon type="reload"></Icon>刷新</Button>
          </div>
        )
      } }
      { ...rest }
    >
      <#if !noSearch>
      <BoxTabs>
        <Form
          horizontal
        >
          <Tabs defaultActiveKey="1" tabBarExtraContent={ 

            <ButtonGroup>
              <Button type="primary" onClick={ () => {

                const formData = form.getFieldsValue();
                dispatch({
                  type: '${_moduleName}s/setQuery',
                  payload: formData,
                });

              } } >搜索</Button>
              <Button type="ghost" >导出</Button>
            </ButtonGroup>

          } >
            <TabPane tab="快捷搜索" key="1">
            
              <Row gutter={ 2 }>
              	<#list records as record>
              	<#if searchableList?seq_contains(record.COLUMN_NAME)>
                <Col sm={ 6 }>
                  <FormItem
                    { ...formItemLayout }
                    label="${record.COLUMN_COMMENT}"
                  >
                    <Input {...getFieldProps('${record.COLUMN_CAMEL_NAME}')} size="default" />
                  </FormItem>
                </Col>
                </#if>
                </#list>
              </Row>
            
            </TabPane>
            <#if !noASearch>
            <TabPane tab="高级搜索" key="2">
            
              <Row gutter={ 2 }>
                <#list records as record>
              	<#if searchableAList?seq_contains(record.COLUMN_NAME)>
                <Col sm={ 6 }>
                  <FormItem
                    { ...formItemLayout }
                    label="${record.COLUMN_COMMENT}"
                  >
                    <Input {...getFieldProps('${record.COLUMN_CAMEL_NAME}')} size="default" />
                  </FormItem>
                </Col>
                </#if>
                </#list>
              </Row>
            
            </TabPane>
            </#if>
          </Tabs>
        </Form>
      </BoxTabs>
      </#if>
      <BoxTable
        noPadding
        
        <#if canDelAll>
        rowSelection={rowSelection}
        </#if>
        columns={
          [
          <#list records as record>
          {
            title: '${record.COLUMN_COMMENT}',
            dataIndex: '${record.COLUMN_CAMEL_NAME}',
            key: '${record.COLUMN_CAMEL_NAME}',
          },
          </#list>
          {
            title: '操作',
            key: 'operation',
            render: (text, record) => (
              <span>
                <Link to={ `/${packageName}/${_moduleName}s/edit/${"$"}{record.id}` }><Icon type="edit" />编辑</Link>
                <span className="ant-divider"></span>
                <Popconfirm title="确定要删除这个${moduleCName}吗？" onConfirm={ () => {
                  
                  dispatch({
                    type: '${_moduleName}s/deleteOne',
                    payload: record.id,
                  })

                } } onCancel={() => {} }>
                  <a href="#"><Icon type="delete" />删除</a>
                </Popconfirm>
              </span>
            ),
          }]
        }
        <#if canDelAll>
        
        footer={ () => {
          return (
            <div style={ { marginLeft: '12px' } } >
              <Button type="primary"
                disabled={ ${_moduleName}s.selectedRowKeys.length === 0 }
                loading={ ${_moduleName}s.batchDelLoading }
                onClick={ () => {
                  
                  confirm({
                    title: '您是否确认要删除选中的所有内容',
                    content: '一旦确定后，将不可恢复，请慎重考虑。并且请注意，是否正确选中想要删除的${moduleCName}？',
                    okText: '确定删除',
                    onOk() {
                      dispatch({
                        type: '${_moduleName}s/batchDelete',
                        payload: ${_moduleName}s.selectedRowKeys,
                      })
                    },
                    onCancel() {},
                  });
                  
                } }
              >一键删除</Button>
              <span style={{ marginLeft: 8 }}>{${_moduleName}s.selectedRowKeys.length > 0 ? `选择了 ${"$"}{${_moduleName}s.selectedRowKeys.length} 个对象` : ''}</span>
            </div>
          )
        } }
        
        </#if>
        rowKey={ record => record.id }
        dataSource={ ${_moduleName}s.${_moduleName}s }
        pagination={ ${_moduleName}s.pagination }
        loading={ ${_moduleName}s.loading }
        onChange={ onTableChange }
      />
    </Container>
  )
}

List.propTypes = {

}

export default Form.create({
  mapPropsToFields: (props) => {
    const query = props.${_moduleName}s.query;
    return {
      <#if !noASearch>
      <#list records as record>
  	  <#if searchableAList?seq_contains(record.COLUMN_NAME)>
      ${record.COLUMN_CAMEL_NAME}: {
        value: query.${record.COLUMN_CAMEL_NAME}
      },
      </#if>
      </#list>
      <#elseif noASearch && !noSearch>
      <#list records as record>
  	  <#if searchableList?seq_contains(record.COLUMN_NAME)>
      ${record.COLUMN_CAMEL_NAME}: {
        value: query.${record.COLUMN_CAMEL_NAME}
      },
      </#if>
      </#list>
      </#if>
    }
  }
})(List);
