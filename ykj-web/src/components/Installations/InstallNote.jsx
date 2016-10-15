import React, { PropTypes } from 'react';
import { Form, Input, Select, TreeSelect, Checkbox, Button, Cascader, Spin, Row, Col, Upload, Icon, DatePicker, InputNumber, Table } from 'antd';
import { routerRedux } from 'dva/router';

import Container from '../Container';
import Box from '../Box';
import InstallGoods from './InstallGoods';
import styles from './InstallNote.less';

const FormItem = Form.Item;
const CheckboxGroup = Checkbox.Group;
const Option = Select.Option;

const InstallNote = (props) => {
  const { installations, installGoods, form, type, onSubmit, moreProps, dispatch, ...rest } = props
  const { loading } = installations;
  const { getFieldProps, getFieldError, isFieldValidating, setFieldsValue } = form;
  const installation = installations.current
  
  const rowItemLayout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 12 },
  }
  const fullRowLayout = {
    labelCol: { span: 3 },
    wrapperCol: { span: 18 },
  }
  return (
    <Container
      {...rest}
    >
    <Spin spinning={ loading }>
      <Box>
        <Form horizontal >
          <Row>
            <Col span={12}><h3>安装服务单单预览</h3></Col>
          </Row>
          <br/>
          <Box>
            <font color="white">PrintStart</font>
            <div className={styles['container']}>
              <div className={styles['note-title']}>
                <Row>
                  <Col offset="6" span="12" style={{textAlign: 'center'}}>
                    <h1>
                      驱蚊器公司
                      &nbsp;&nbsp;
                      安装服务单
                      &nbsp;&nbsp;
                      logo
                    </h1>
                  </Col>
                </Row>
              </div>
              <div className={styles['note-number']}>
                <Row>
                  <Col span="24">
                    <label className={styles['sub-title']}>服务单号:</label>
                    <span className={styles['content-font']}>123</span>
                  </Col>
                </Row>
              </div>
              <div className={styles['item']}>
                <Row>
                  <Col span="12">
                    <label>安装日期:</label>
                    <span className={styles['content-font']}>2016-09-09</span>
                  </Col>
                  <Col span="12">
                    <label>客户姓名:</label>
                    <span className={styles['content-font']}>王华</span>
                  </Col>
                </Row>
              </div>
              <div className={styles['item']}>
                <Row>
                  <Col span="12">
                    <label>客户电话:</label>
                    <span className={styles['content-font']}>王华</span>
                  </Col>
                  <Col span="12">
                    <label>安装师傅:</label>
                    <span className={styles['content-font']}>张伟刚</span>
                  </Col>
                </Row>
              </div>
              <div className={styles['item']}>
                <Row>
                  <Col span="24">
                    <label>安装地址:</label>
                    <span className={styles['content-font']}>131231</span>
                  </Col>
                </Row>
              </div>
              <div className={styles['item']}>
                <h3 className={styles['sub-title']}>商品列表</h3>
                <br/>
                <Table
                  columns={[
                    {
                      title: '商品名称',
                      dataIndex: 'name',
                      key: 'name',
                    },
                    {
                      title: '商品型号',
                      dataIndex: 'no',
                      key: 'no',
                    },
                    {
                      title: '送货数量',
                      dataIndex: 'deliveryNum',
                      key: 'deliveryNum',
                    },
                    {
                      title: '使用数量',
                      dataIndex: '1',
                      key: '1',
                    },
                    {
                      title: '剩余数量',
                      dataIndex: '2',
                      key: '2',
                    },
                    {
                      title: '备注',
                      dataIndex: 'remark',
                      key: 'remark',
                    }
                  ]}
                  dataSource={installGoods.installGoods}
                  pagination={false}
                >
                </Table>
              </div>
              <div className={styles['item']}>
                <Row>
                  <Col span="12">
                    <label>安装质量:</label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <span>满意</span>&nbsp;&nbsp;&nbsp;&nbsp;
                    <span>一般</span>&nbsp;&nbsp;&nbsp;&nbsp;
                    <span>不满意</span>
                  </Col>
                  <Col span="12">
                    <label>意见或建议:</label>
                    <p></p>
                  </Col>
                </Row>
              </div>
              <div className={styles['item']}>
                <Row>
                  <Col span="12">
                    <label>服务态度:</label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <span>满意</span>&nbsp;&nbsp;&nbsp;&nbsp;
                    <span>一般</span>&nbsp;&nbsp;&nbsp;&nbsp;
                    <span>不满意</span>
                  </Col>
                </Row>
              </div>
              <div className={styles['item']}>
                <Row>
                  <Col span="12">
                    <label>安装工签字:</label>
                    <p></p>
                  </Col>
                  <Col span="12">
                    <label>客户验收签字:</label>
                    <p></p>
                  </Col>
                </Row>
              </div>
              <div className={styles['item']}>
                <Row>
                  <Col span="6">
                    <img src="" alt="" width="120" height="120"/>
                  </Col>
                  <Col span="18">
                    <Box>
                      <h4>请关注我们，只需要三步，让我们更好为您服务！</h4>
                      <div>1.使用微信“扫一扫”扫描左侧二维码并关注公众号</div>
                      <div>2.进入公众号-->点开屏幕底部“售后服务”</div>
                      <div>3.按照提示操作，在需要输入手机号的地方输入您订单时的手机号码</div>
                    </Box>
                  </Col>
                </Row>
              </div>
            </div>
            <font color="white">PrintEnd</font>
          </Box>
          <br/>
          <Row>
            <Col style={{textAlign: 'center'}}>
              <Button type="primary" onClick={() => {
                const bodyHtml = window.document.body.innerHTML;
                let sprnstr="PrintStart</font>";
                let eprnstr="<font color=";
                let printHtml = bodyHtml.substr(bodyHtml.indexOf(sprnstr)+17);
                printHtml = printHtml.substring(0, printHtml.indexOf(eprnstr));
                window.document.body.innerHTML = printHtml;
                window.print();
                window.document.body.innerHTML = bodyHtml;
                location.reload();
              }}>打印</Button>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <Button type="ghost" onClick={ () => dispatch(routerRedux.goBack()) }>返回</Button>
            </Col>
          </Row>
        </Form>
      </Box>
    </Spin>
    </Container>
  )
}

InstallNote.propTypes = {
  onSubmit: PropTypes.func.isRequired,
  moreProps: PropTypes.func.isRequired,
}

InstallNote.defaultProps = {
  onSubmit: () => {},
  moreProps: () => ({}),
}

export default Form.create()(InstallNote);
