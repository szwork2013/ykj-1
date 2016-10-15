import React, { PropTypes } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Row, Col, Modal, Popconfirm, InputNumber } from 'antd';

import Container from '../Container';
import Box from '../Box';

const FormItem = Form.Item;
const ButtonGroup = Button.Group;
const Option = Select.Option;
const Detail = ({ house, form, dispatch, type, onSubmit, visible, ...rest }) => {
  const { getFieldProps, getFieldError, isFieldValidating } = form;
  
  const save = function() {
    onSubmit(form);
    hideModal();
  }
  const update = function(e) {
    onSubmit(e, form);
  }
  const onDelete = function(id) {
    dispatch({
      type: 'houses/deleteOne',
      payload: id
    })
  }
  const handleCancel = function() {
    hideModal();
  }
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

  const buildingNameProps = getFieldProps('buildingName', {
    rules: [
      { required: true, min: 1, message: '楼盘名称至少 1 个字符'}
    ]
  });

  const buildingNoProps = getFieldProps('buildingNo', {
  });

  const buildingPositionProps = getFieldProps('buildingPosition', {
    
  });

  const decorateTypeProps = getFieldProps('decorateType', {
    rules: [
      { required: true, message: '装修类型必须选择'}
    ]
  });

  const roomModelProps = getFieldProps('roomModel', {
    rules: [
      { required: true, message: '户型必须选择'}
    ]
  });

  const areaProps = getFieldProps('area', {
    rules: [
      { type: 'number', message: '户型必须选择'}
    ]
  });

  const roomStyleProps = getFieldProps('roomStyle', {
    rules: [
      { required: true, message: '风格必须选择'}
    ]
  });

  const decorateProcessProps = getFieldProps('decorateProcess', {
    rules: [
      { required: true, message: '装修进度必须选择'}
    ]
  });
  
  const formItemLayout = {
    labelCol: { span: 3 },
    wrapperCol: { span: 5 },
  };
  const rowItemLayout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 12 },
  }
  const content = (
    <Row>
      <Row>
        <Col span="12">
          <FormItem
            {...rowItemLayout}
            label="楼盘名称"
            hasFeedback
            help={isFieldValidating('buildingName') ? '校验中...' : (getFieldError('buildingName') || []).join(', ')}
          >
            <Input {...buildingNameProps} />
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem
            {...rowItemLayout}
            label="楼幢房号"
            hasFeedback
            help={isFieldValidating('buildingNo') ? '校验中...' : (getFieldError('buildingNo') || []).join(', ')}
          >
            <Input {...buildingNoProps} />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="12">
          <FormItem
            {...rowItemLayout}
            label="区域"
            hasFeedback
            help={isFieldValidating('buildingPosition') ? '校验中...' : (getFieldError('buildingPosition') || []).join(', ')}
          >
            <Input {...buildingPositionProps} />
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem
            {...rowItemLayout}
            label="装修类型"
            hasFeedback
            help={isFieldValidating('decorateType') ? '校验中...' : (getFieldError('decorateType') || []).join(', ')}
          >
            <Select {...decorateTypeProps} style={{ width: '100%' }} >
              <Option value="888">清包</Option>
            </Select>
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="12">
          <FormItem
            {...rowItemLayout}
            label="户型"
            hasFeedback
            help={isFieldValidating('roomModel') ? '校验中...' : (getFieldError('roomModel') || []).join(', ')}
          >
            <Select {...roomModelProps} style={{ width: '100%' }} >
              <Option value="666">四居室</Option>
            </Select>
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem
            {...rowItemLayout}
            label="面积"
            hasFeedback
            help={isFieldValidating('area') ? '校验中...' : (getFieldError('area') || []).join(', ')}
          >
            <InputNumber {...areaProps} style={{width: '100%'}}/>
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="12">
          <FormItem
            {...rowItemLayout}
            label="风格"
            hasFeedback
            help={isFieldValidating('roomStyle') ? '校验中...' : (getFieldError('roomStyle') || []).join(', ')}
          >
            <Select {...roomStyleProps} style={{ width: '100%' }}>
              <Option value="999">美式乡村</Option>
            </Select>
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem
            {...rowItemLayout}
            label="装修进度"
            hasFeedback
            help={isFieldValidating('decorateProcess') ? '校验中...' : (getFieldError('decorateProcess') || []).join(', ')}
          >
            <Select {...decorateProcessProps} style={{ width: '100%' }}>
              <Option value="555">收工</Option>
            </Select>
          </FormItem>
        </Col>
      </Row>
    </Row>
  )
  const add = (
    <Form horizontal >
        <Modal width={800} title="房产信息" visible={ visible || false }
          onOk={(e) => save(e)} onCancel={handleCancel}
        >
          {content}
        </Modal> 
    </Form>
  )
  const edit = (
    <Form horizontal >
        <Row>
          <Col offset="10" span="4" style={{'textAlign': 'center'}}>
            <h3>{ house.buildingName }</h3>
          </Col>
          <Col span="7" style={{'textAlign': 'right'}}>
            <ButtonGroup>
              <Button type="primary" htmlType="submit" onClick={(e) => update(e)}>保存</Button>
              <Popconfirm title="确定要删除该房产吗？" onConfirm={ () => onDelete(house.id) } onCancel={() => { } }>
                <Button type="ghost">删除</Button>
              </Popconfirm>
            </ButtonGroup>
          </Col>
        </Row>
        <br/>
        {content}
    </Form>
  )
  const render = function(type){
    if (type === 'add') {
      return add;
    } else if (type === 'edit') {
      return edit;
    } else {
      return edit;
    }
  }
  return render(type);
}

Detail.propTypes = { 
    house: PropTypes.object,
}

Detail.defaultProps = {
    house: {},
}

function mapPropsToFields(props) {
  const query = props.house || {};
  return {
    buildingName: {
      value: query.buildingName
    },
    buildingNo: {
      value: query.buildingNo
    },
    buildingPosition: {
      value: query.buildingPosition
    },
    decorateType: {
      value: query.decorateType ? `${query.decorateType}` : undefined
    },
    roomModel: {
      value: query.roomModel ? `${query.roomModel}` : undefined
    },
    area: {
      value: query.area
    },
    roomStyle: {  
      value: query.roomStyle ? `${query.roomStyle}` : undefined
    },
    decorateProcess: {
      value: query.decorateProcess ? `${query.decorateProcess}` : undefined
    },

  }
}

export default Form.create({mapPropsToFields})(Detail);
