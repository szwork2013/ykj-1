import React, { PropTypes, Component } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Cascader, Spin, Row, Col, DatePicker, Table} from 'antd';
import { routerRedux } from 'dva/router';
import classNames from 'classnames';

import Container from '../Container';
import Box from '../Box';
import styles from './Print.less';

const FormItem = Form.Item;
const CheckboxGroup = Checkbox.Group;
const Option = Select.Option;
const InputGroup = Input.Group;


const Print = ({ orders, form, dispatch, ...rest }) => {

    const { loading, submiting } = orders;

    const formItemLayout = {
      labelCol: { span: 6 },
      wrapperCol: { span: 10 },
    };

    return (
        <Container
            {...rest}
        >
            <Spin spinning={ loading }>
                <Form>
                    <Box>
                    <font color="white">PrintStart</font>
                        <div>
                            <Row >
                                <Col sm={8} >
                                    <div className={styles['orderNo']} >
                                        <h3>订单号：212204006</h3>
                                    </div>
                                </Col>
                                <Col sm={8} offset={1}>
                                    <div className={styles['company']} >
                                        <h2>杭州高网信息公司&nbsp;&nbsp;&nbsp;&nbsp;销售服务单</h2>
                                    </div>
                                </Col>
                                <Col sm={5} offset={11}>
                                    <div className={styles['image']} >
                                        <img />
                                    </div>
                                </Col>
                            </Row>
                            <div className={styles['orderTableFrame']} >
                                <table width='100%' className={styles['tableBordr']}>
                                    <tbody>
                                        <tr>
                                            <td colSpan="2" className={styles['tdBorder']}><h3>客户姓名</h3></td>
                                            <td colSpan="3" className={styles['tdBorder']}>张伟刚</td>
                                            <td colSpan="2" className={styles['tdBorder']}><h3>订单时间</h3></td>
                                            <td colSpan="3" className={styles['tdBorder']}>2016-06-10</td>
                                        </tr>
                                        <tr>
                                            <td colSpan="2" className={styles['tdBorder']}><h3>客户电话</h3></td>
                                            <td colSpan="3" className={styles['tdBorder']}>15456565676</td>
                                            <td colSpan="2" className={styles['tdBorder']}><h3>订单地址</h3></td>
                                            <td colSpan="3" className={styles['tdBorder']}>杭州西湖区留和路高网信息公司</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div className={styles['orderTableFrame']}>
                                <Row>
                                    <Col sm={ 2 }>
                                        <h3>订单说明：</h3>
                                    </Col>
                                    <Col sm={ 22 }>
                                        <p className="ant-form-text" >订单说明订单说明订单说明订单说明订单说明订单说明订单说明订单说明订单说明订单说明订单说明订单说明订单说明订单说明订单说明订单说明订单说明订单说明订单说明订单说明</p>
                                    </Col>
                                </Row>
                                <br/>
                                <hr style={ { width: '100%', border: '1px solid #eee'} }/>
                            </div>
                            <div className={styles['orderTableFrame']}>
                                <Row>
                                    <h3>订购产品明细</h3>
                                </Row>
                                <br/>
                                <Row>
                                    <Table
                                        columns={
                                            [{
                                                title: '名称',
                                                dataIndex: '1',
                                                key: '1',
                                            },
                                            {
                                                title: '型号',
                                                dataIndex: '2',
                                                key: '2',
                                            },
                                            {
                                                title: '规格',
                                                dataIndex: '3',
                                                key: '3',
                                            },
                                            {
                                                title: '位置',
                                                dataIndex: '4',
                                                key: '4',
                                            },
                                            {
                                                title: '单价（元）',
                                                dataIndex: '5',
                                                key: '5',
                                            },
                                            {
                                                title: '数量',
                                                dataIndex: '6',
                                                key: '6',
                                            },
                                            {
                                                title: '单位',
                                                dataIndex: '7',
                                                key: '7',
                                            },
                                            {
                                                title: '小计（元）',
                                                dataIndex: '8',
                                                key: '8',
                                            },
                                            {
                                                title: '备注',
                                                dataIndex: 'remark',
                                                key: 'remark',
                                            },
                                            ]
                                        }
                                    >
                                    </Table>
                                </Row>
                                <br/>
                                <Row>
                                    <Col sm={ 1 }>
                                        <h4>备注：</h4>
                                    </Col>
                                    <Col sm={ 11 }>
                                        <p className="ant-form-text" >在订外卖的时候多带一份可乐和雪碧</p>
                                    </Col>
                                    <Col sm={ 10 } offset={ 2 }>
                                        <h4>{ `成交总价：10000元` }&nbsp;&nbsp;&nbsp;&nbsp;{ `已收（含定金）：90000元`}&nbsp;&nbsp;&nbsp;&nbsp;{ `未收款：90000元`}&nbsp;&nbsp;</h4>
                                    </Col>
                                </Row>
                                <br/>
                                <Row>
                                    <Col sm={ 2 }>
                                        <h4>跟单人：</h4>
                                    </Col>
                                    <Col sm={ 6 }>
                                        <p className="ant-form-text" >张伟刚</p>
                                    </Col>
                                    <Col sm={ 3 }>
                                        <h4>售后服务电话：</h4>
                                    </Col>
                                    <Col sm={ 5 }>
                                        <p className="ant-form-text" >16765656565</p>
                                    </Col>
                                    <Col sm={ 3 }>
                                        <h4>跟单人电话：</h4>
                                    </Col>
                                    <Col sm={ 5 }>
                                        <p className="ant-form-text" >16787878787</p>
                                    </Col>
                                </Row>
                            </div> 
                            <br/>
                            <div className={styles['orderCodeFrame']}>
                                <div className={styles['codeFrame']}>
                                    
                                </div> 
                                <div className={styles['messageFrame']}>
                                    <div className={styles['wordFrame']}>
                                        <h4>请关注我们，只需要三步，让我们更好为您服务！</h4>
                                        <p className="ant-form-text" >1.使用微信“扫一扫”扫描左侧二维码并关注公众号</p><br/>
                                        <p className="ant-form-text" >2.进入公众号-->点开屏幕底部“售后服务”</p><br/>
                                        <p className="ant-form-text" >3.按照提示操作，在需要输入手机号的地方输入您订单时的手机号码</p>
                                    </div>
                                </div>    
                            </div>         
                        </div>
                        <br/>
                        <font color="white">PrintEnd</font>
                        <Row>
                            <Col sm={ 22 } offset={ 19 }>
                                <FormItem { ...formItemLayout } style={{ marginTop: 24 }}>
                                    <Button type="primary" htmlType="submit" loading={ submiting } onClick={ (e) => {
                                        let bodyHtml = window.document.body.innerHTML;
                                        let sprnstr="PrintStart</font>";
                                        let eprnstr="<font color=";
                                        let printHtml = bodyHtml.substr(bodyHtml.indexOf(sprnstr)+17);
                                        printHtml = printHtml.substring(0, printHtml.indexOf(eprnstr));
                                        window.document.body.innerHTML = printHtml;
                                        window.print();
                                        window.document.body.innerHTML = bodyHtml;
                                    } }>打印</Button>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <Button type="ghost" onClick={ () => dispatch(routerRedux.goBack()) }>返回</Button>
                                </FormItem>
                            </Col>
                        </Row>
                    </Box>
                </Form>
            </Spin>
        </Container>
    )
}

Print.propTypes = {
  
}

export default Form.create({
  
})(Print);
