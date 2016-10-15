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
const List = ({ codewords, codewordTypes, form, children, dispatch, ...rest }) => {
  const { getFieldProps } = form;

  const onTableChange = (pagination, filters, sorter) => {
    dispatch({
      type: 'codewords/setQuery',
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
      toolbar={ () => {
        return (
          <div>
            <Button type="primary" size="large">
              {
                codewords.typeId 
                ?
                <Link to="/setting/codewords/add">创建数据字典</Link>
                :
                <Popconfirm title="请选择数据字典类型" onConfirm={()=>{}} onCancel={()=>{}}  okText="确认">
                  <a href="#">创建数据字典</a>
                </Popconfirm>
              }
              
            </Button>
            <Button type="ghost" size="large" onClick={ () => {

              const formData = form.getFieldsValue();
              dispatch({
                type: 'codewords/setQuery',
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
                  type: 'codewords/setQuery',
                  payload: formData,
                });

              } } >搜索</Button>
              <Button type="ghost" >导出</Button>
            </ButtonGroup>

          } >
            <TabPane tab="快捷搜索" key="1">
            
              <Row gutter={ 2 }>
                <Col sm={ 6 }>
                  <FormItem
                    { ...formItemLayout }
                    label="数据字典类型"
                  >
                    <Select {...getFieldProps('codewordTypeValue')} 
                      onChange={(value) => {
                        const { setFieldsValue } = form;
                        setFieldsValue({
                          codewordTypeValue: value
                        })
                        dispatch({
                          type: 'codewords/setQuery',
                          payload: {codewordTypeValue: value}
                        })
                      }}
                    >
                      {
                        codewordTypes.map(item => {
                          return <Option key={item.id} value={`${item.value}-${item.id}`}>{item.name}</Option>
                        })
                      }
                    </Select>
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
            render: (text, record, index) => index+1,
          },
          {
            title: '数据类型',
            dataIndex: 'codewordTypeId',
            key: 'codewordTypeId',
          },
          {
            title: '数据值',
            dataIndex: 'value',
            key: 'value',
            sorter: true,
          },
          {
            title: '修改时间',
            dataIndex: 'updateTime',
            key: 'updateTime',
          },
          {
            title: '操作',
            key: 'operation',
            render: (text, record) => (
              <span>
                <Popconfirm title="确定要删除这个数据字典吗？" onConfirm={ () => {
                  
                  dispatch({
                    type: 'codewords/deleteOne',
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
        dataSource={ codewords.codewords }
        pagination={ codewords.pagination }
        loading={ codewords.loading }
        onChange={ onTableChange }
      />
    </Container>
  )
}

List.propTypes = {

}

export default Form.create({
  mapPropsToFields: (props) => {
    const query = props.codewords.query;
    return {
      codewordTypeValue: {
        value: query.codewordTypeValue
      },
    }
  }
})(List);
