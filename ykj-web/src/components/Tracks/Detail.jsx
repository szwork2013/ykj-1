import React, { PropTypes } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Row, Col, Modal, Popconfirm, DatePicker, Upload, Icon } from 'antd';

import Container from '../Container';
import Box from '../Box';

const FormItem = Form.Item;
const ButtonGroup = Button.Group;
const Option = Select.Option;
const Detail = ({ tracks, form, dispatch, type, onSubmit, handleCancel, visible, ...rest }) => {
  const { getFieldProps, getFieldError, isFieldValidating, setFieldsValue } = form;

  const track = tracks.current

  const timeProps = getFieldProps('time', {
    rules: [
      { required: true, message: '必须填写跟进时间'}
    ]
  });

  const wayProps = getFieldProps('way', {
    rules: [
      { required: true, message: '必须填写跟进方式'}
    ]
  });

  const contentProps = getFieldProps('content', {
  });

  const attachmentRootProps = getFieldProps('attachmentRoot', {
    action: '/upload',
  });

  const nextTrackTimeProps = getFieldProps('nextTrackTime', {
    rules: [
      { required: true, message: '必须填写下次跟进时间'}
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
  const fullRowLayout = {
    labelCol: { span: 3 },
    wrapperCol: { span: 18 },
  }

  const  content = (
    <Row>
      <Row>
        <Col span="12">
          <FormItem
            {...rowItemLayout}
            label="跟进时间"
            hasFeedback
            help={isFieldValidating('time') ? '校验中...' : (getFieldError('time') || []).join(', ')}
          >
            <DatePicker showTime format="YYYY-MM-dd HH:mm:ss" {...timeProps} style={{width: '100%'}}
              onChange={(date, stringDate) => {
                setFieldsValue({
                  time: stringDate
                })
              }}
            />
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem
            {...rowItemLayout}
            label="跟进方式"
            hasFeedback
            help={isFieldValidating('way') ? '校验中...' : (getFieldError('way') || []).join(', ')}
          >
            <Select {...wayProps} style={{ width: '100%' }} >
              <Option value="0">电话</Option>
              <Option value="1">微信</Option>
              <Option value="2">短信</Option>
              <Option value="3">面谈</Option>
            </Select>
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="24">
          <FormItem
            {...fullRowLayout}
            label="内容"
            hasFeedback
            help={isFieldValidating('content') ? '校验中...' : (getFieldError('content') || []).join(', ')}
          >
            <Input type='textarea' rows={6} {...contentProps} />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="12">
          <FormItem
            {...rowItemLayout}
            label="附件"
            hasFeedback
            help={isFieldValidating('attachmentRoot') ? '校验中...' : (getFieldError('attachmentRoot') || []).join(', ')}
          >
            <Upload {...attachmentRootProps}>
              <Button type="ghost">
                <Icon type="upload" /> 点击上传附件
              </Button>
            </Upload>
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem
            {...rowItemLayout}
            label="下次跟进时间"
            hasFeedback
            help={isFieldValidating('nextTrackTime') ? '校验中...' : (getFieldError('nextTrackTime') || []).join(', ')}
          >
            <DatePicker showTime format="YYYY-MM-dd HH:mm:ss" {...nextTrackTimeProps} style={{width: '100%'}}
              onChange={(date, stringDate) => {
                setFieldsValue({
                  nextTrackTime: stringDate
                })
              }}
            />
          </FormItem>
        </Col>
      </Row>
    </Row>
  )

  const add = (
    <Form horizontal >
      <Modal title="新增跟进记录" visible={ tracks.showAddModal }
        onOk={ () => onSubmit(form) } onCancel={ handleCancel }
        width={ 800 }
      >
        {content}
      </Modal>
    </Form>
  )
  const edit = (
    <Form horizontal >
      <Modal title="编辑跟进记录" visible={ tracks.showEditModal }
        onOk={ () => onSubmit(form) } onCancel={ handleCancel }
        width={ 800 }
      >
        {content}
      </Modal>
    </Form>
  )

  const render = function(type) {
    if (type === 'add') {
      return add;
    } else if (type === 'edit') {
      return edit;
    } else {
      return edit;
    }
  }

  return render(type)
}

Detail.propTypes = { 
    track: PropTypes.object,
}

Detail.defaultProps = {
    track: {},
}

function mapPropsToFields(props) {
  const track = props.tracks.current
  return {
    time: {
      value: track.time
    },
    way: {
      value: track.way === 0 || track.way ? `${track.way}` : undefined
    },
    content: {
      value: track.content
    },
    attachmentRoot: {
      value: track.attachmentRoot
    },
    nextTrackTime: {
      value: track.nextTrackTime
    },
  }
}

export default Form.create({mapPropsToFields})(Detail);
