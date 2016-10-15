import React, { PropTypes } from 'react';
import { Row, Col, Button, Form, TreeSelect, Tag, Modal, Input } from 'antd';
import { Link } from 'dva/router';
const ButtonGroup = Button.Group;

import styles from './index.less';

const FormItem = Form.Item;

const TagBox = ({ tags, dispatch, form, ...others }) => {
    const { getFieldProps, getFieldError, isFieldValidating } = form;
	const { handleOk, handleClose } = others;
    const tagsArray = tags.tags || [];
    const visible = tags.visible;
    const loading = tags.loading;
    
    const formItemLayout = {
        labelCol: { span: 5 },
        wrapperCol: { span: 5 },
    }

    const onDelete = function(id) {
        handleClose(id);
    }

    const onOk = function() {
        if(!form.getFieldProps('tagName').value){
            dispatch({
                type: 'tags/hide',
            })
            return false;
        }
        form.validateFieldsAndScroll((errors, values) => {
            if (!!errors) {
                return;
            }
            let formData = form.getFieldsValue();
            handleOk(form.getFieldProps('tagName').value);
            dispatch({
                type: 'tags/hide',
            })
        })
        return true;
    }
    
    const addTag = function () {
        form.resetFields()
        dispatch({
            type: 'tags/show',
            payload: {},
        })
    }

    const onCancel = function() {
        dispatch({
            type: 'tags/hide',
        })
    }

    const tagNameProps = getFieldProps('tagName', {
        rules: [
          { required: true, min: 1, message: '请输入标签名称'}
        ]
    });

    const fullRowLayout = {
        labelCol: { span: 6 },
        wrapperCol: { span: 15 },
    }

    return (
        <div>
            {
                tagsArray.map(tag =>
                    <Tag key={tag.id} closable={true} onClose={() => onDelete(tag.customerTagsId)}>
                        {tag.name}
                    </Tag>
                )
            }
            <Button size="small" type="dashed" onClick={addTag}>+ 添加标签</Button>
            <div>
            	<Row>
	                <Modal
	                    visible={ visible }
	                    title="添加标签" onOk={ onOk } onCancel={onCancel}
	                    footer={[
	                        <Button key="back" type="ghost" size="large" onClick={onCancel}>返 回</Button>,
	                        <Button key="submit" type="primary" size="large" loading={loading} onClick={onOk}>
	                            提交
	                        </Button>,
	                    ]}
	                >   
                        <Form horizontal >
                            <FormItem
                                {...fullRowLayout}
                                label="标签名称"
                                hasFeedback
                                help={isFieldValidating('tagName') ? '校验中...' : (getFieldError('tagName') || []).join(', ')}
                            >
                                <Input size="large" placeholder="标签名称" {...tagNameProps}/>
                            </FormItem>
                        </Form>
	                </Modal>
	            </Row>
            </div>
        </div>
    )

}

TagBox.propTypes = { 
    tags: PropTypes.object,
}

TagBox.defaultProps = {
    tags: {},
}

export default Form.create()(TagBox);
