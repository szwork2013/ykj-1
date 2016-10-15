import React, { PropTypes } from 'react';
import { Row, Col, Button, Form, TreeSelect, Tag, Modal, Input } from 'antd';
import { Link } from 'dva/router';

import TagBox from '../TagBox';
import TreeBox from '../TreeBox'
const ButtonGroup = Button.Group;

import styles from './index.less';

const FormItem = Form.Item;

const CustomerInfo = ({ options, children, ...others }) => {
    const { tags, dispatch, tree, changeable, showTagBox } = options;
    
    const formItemLayout = {
        labelCol: { span: 5 },
        wrapperCol: { span: 10 },
    }

    return (
        <div {...others}>
            <Row>
                <Col span={ 12 }>
                    <Row type="flex" justify="space-around" align="middle">
                        <Col span={ 8 } offset={4}>
                            <img src={options.img} className={styles['img']} alt=""/>
                        </Col>
                        <Col span={ 11 } offset={1}>
                            <div >
                                <h2>{options.name}</h2>
                                <h2>{options.phone}</h2>
                            </div>
                        </Col>
                    </Row>
                </Col>
                <Col span={ 12 } >
                    <Row gutter={ 12 }>
                        <Form horizontal>
                            <FormItem
                                {...formItemLayout}
                                label="客户负责人"
                            >
                                <TreeBox 
                                    dispatch={dispatch}
                                    tree={tree}
                                    multiple={ false }
                                    checkable={ false }
                                    changeable={ changeable || false }
                                    treeProps = {{
                                        value: `${options.id}`
                                    }}
                                />
                            </FormItem>
                            <FormItem
                                {...formItemLayout}
                                label="录入时间"
                            >
                                {options.createDate}
                            </FormItem>
                            <FormItem
                                {...formItemLayout}
                                label="最后跟踪"
                            >
                                {options.time}
                            </FormItem>
                        </Form>
                    </Row>
                </Col>
            </Row>
            { children }
        </div>
    )
}

CustomerInfo.propTypes = { 
    
}

export default CustomerInfo;
