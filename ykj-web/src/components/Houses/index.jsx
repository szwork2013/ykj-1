import React, { PropTypes } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Row, Col } from 'antd';
import { routerRedux } from 'dva/router';

import Container from '../Container';
import Box from '../Box';

import CustomerInfo from '../CustomerInfo'
import Edit from './Edit'
import Add from './Add'

const FormItem = Form.Item;
const CheckboxGroup = Checkbox.Group;
const Option = Select.Option;
const Houses = ({ customers, houses, dispatch, form, onSubmit, ...rest }) => {
  const { getFieldProps, getFieldError, isFieldValidating } = form;
  
  let options = { ...customers.current };
  options.dispatch = dispatch;

  const showModal = function() {
    dispatch({
      type: 'houses/show'
    })
  }

  const hideModal = function() {
    dispatch({
      type: 'houses/hide'
    })
  }

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
        <Col offset={1} span={11}><h3>房产信息</h3></Col>
        <Col span={9} style={{textAlign: 'right'}}>
          <a href="javascript:;" onClick={showModal}>新增房产</a>
          <Add visible={houses.visible} dispatch={dispatch}/>
        </Col>
      </Row>
      <br/>
      {
        houses.houses.length > 0 
        ?
        houses.houses.map((house, index) => {
          return (
            <Edit key={index} house={house} dispatch={dispatch}/>
          )
        })
        :
        (
          <Box>
          暂无房产信息
          </Box>
        )
      }
      <Row type="flex" justify="end">
        <Col span={5}>
          <Button type="primary" onClick={ () => dispatch(routerRedux.goBack()) }>返回</Button>
        </Col>
      </Row> 
      <br/>
    </Container>
  )
}

Houses.propTypes = {
  onSubmit: PropTypes.func.isRequired,
}

Houses.defaultProps = {
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

export default Form.create({mapPropsToFields})(Houses);
