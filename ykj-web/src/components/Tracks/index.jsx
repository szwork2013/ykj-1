import React, { PropTypes } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Row, Col, Icon, Popconfirm } from 'antd';
import { routerRedux } from 'dva/router';

import Container from '../Container';
import Box from '../Box';
import BoxTable from '../BoxTable';

import CustomerInfo from '../CustomerInfo'
import EditModal from './EditModal'
import AddModal from './AddModal'

const FormItem = Form.Item;
const CheckboxGroup = Checkbox.Group;
const Option = Select.Option;
const Tracks = ({ customers, tracks, dispatch, form, ...rest }) => {
  const { getFieldProps, getFieldError, isFieldValidating } = form;

  const onTableChange = (pagination, filters, sorter) => {
    dispatch({
      type: 'tracks/list',
      payload: {
        page: pagination.current,
        sort: sorter.field ? `${sorter.field},${sorter.order}` : undefined,
      },
    });
  }

  const onDelete = function(id) {
    dispatch({
      type: 'tracks/deleteOne',
      payload: id
    })
  }
  
  let options = { ...customers.current };
  options.dispatch = dispatch;

  const formItemLayout = {
    labelCol: { span: 3 },
    wrapperCol: { span: 5 },
  };
  const rowItemLayout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 13 },
  }
  const fullRowLayout = {
    labelCol: { span: 3 },
    wrapperCol: { span: 19 },
  }
  return (
    <Container
      {...rest}
    >
      <Box>
        <CustomerInfo options={options}/>
      </Box>
      <Row>
        <Col span={4}><h3>跟进记录</h3></Col>
        <Col offset={18} span={2}>
          <a href="javascript:;" 
            onClick={() => {
              dispatch({
                type: 'tracks/toggleAddModal',
                payload: true
              })
            }}>
            新增跟进记录
          </a>
        </Col>
      </Row>
      <br/>
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
                      title: '跟进人',
                      dataIndex: 'customerResponsibleName',
                      key: 'customerResponsibleName',
                  },
                  {
                      title: '跟进时间',
                      dataIndex: 'time',
                      key: 'time',
                      sorter: true,
                  },
                  {
                      title: '跟进方式',
                      dataIndex: 'way',
                      key: 'way',
                      sorter: true,
                  },
                  {
                      title: '内容',
                      dataIndex: 'content',
                      key: 'content',
                  }, 
                  {
                      title: '操作',
                      key: 'operation',
                      width: 150,
                      render: (text, record) => (
                          <span>
                            <a href="javascript:;"
                              onClick={() => {
                                dispatch({
                                  type: 'tracks/view',
                                  payload: record.id
                                })
                              }}><Icon type="edit"/>编辑</a>
                            <span className="ant-divider"></span>
                            <Popconfirm title="确定要删除这项更进记录吗？" onConfirm={ () => onDelete(record.id) } onCancel={() => { } }>
                              <a href="#"><Icon type="delete" />删除</a>
                            </Popconfirm>
                          </span>
                      ),
                  }
              ]
          }
          rowKey={ record => record.id }
          dataSource={ tracks.tracks }
          pagination={ customers.pagination }
          loading={ customers.loading }
          onChange={ onTableChange }
        />
      <br/>
      <Row type="flex" justify="end">
        <Col span={2}>
          <Button type="primary" onClick={ () => dispatch(routerRedux.goBack()) }>返回</Button>
        </Col>
      </Row> 
      <br/>
      <AddModal
        tracks = {tracks}
        dispatch = {dispatch}
      />
      <EditModal
        tracks = {tracks}
        dispatch = {dispatch}
      />
    </Container>
  )
}

Tracks.propTypes = {
  onSubmit: PropTypes.func.isRequired,
}

Tracks.defaultProps = {
  onSubmit: () => {},
}

function mapPropsToFields(props) {
  const query = props.customers.current;
  return {
    buildingName: '123',
    buildingNo: 345,
    buildingPosition: 'sdf',
    decorateType: 124,
    roomModel: '123123',
    area: 123,
    roomStyle: '1dfg',
    decorateProcess: 'asd',
  }
}

export default Form.create({mapPropsToFields})(Tracks);
