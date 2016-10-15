import React, { PropTypes } from 'react';
import { Router, Route, IndexRedirect, IndexRoute } from 'dva/router';
import IndexPage from './routes/IndexPage';
import RoleListPage from './routes/role/RoleListPage'
import RoleAddPage from './routes/role/RoleAddPage'
import RoleEditPage from './routes/role/RoleEditPage'
import LoginPage from './routes/LoginPage';

/*--------------------------------订单管理-------------------------------------- */
import OrderPage, { OrderAdd, OrderEdit, OrderPrint, OrderEnterOut, OverOrderPage  } from './routes/OrdersPage'
import MeasuresPage, { MeasureAdd, MeasureEdit } from './routes/MeasuresPage'
import DesignsPage, { DesignAdd, DesignEdit } from './routes/DesignsPage'
import DeliverysPage, { DeliveryAdd, DeliveryEdit, DeliveryNote } from './routes/DeliverysPage'
import InstallationsPage, { InstallationAdd, InstallationEdit, InstallationCharges, InstallationNote } from './routes/InstallationsPage'

/*--------------------------------通用设置-------------------------------------- */
import UserPage, { UserAdd, UserEdit } from './routes/UserPage';


/*--------------------------------客户管理-------------------------------------- */
import CustomersPage, { CustomerAdd, CustomerEdit, CustomerHouses, CustomerTracks } from './routes/CustomersPage';

/*--------------------------------采购管理-------------------------------------- */
import SuppliersPage, { SupplierAdd, SupplierEdit } from './routes/SuppliersPage';


/*--------------------------------系统管理-------------------------------------- */
import CodewordsPage, { CodewordAdd, CodewordEdit } from './routes/CodewordsPage';


export default ({ history }) => {
  return (
    <Router history={ history }>
      <Route breadcrumbName="首页" path="/" >
        <IndexRedirect to="customers" />
        <Route path="login" component={ LoginPage } />
        <Route component={ IndexPage }  >

          <Route breadcrumbName="订单管理" path="order">
            <Route breadcrumbName="订单列表" path="orders" >
              <IndexRoute component={ OrderPage } />
              <Route breadcrumbName="已完成订单列表" path="overOrders" component={ OverOrderPage } />
              <Route breadcrumbName="下订单" path="add" component={ OrderAdd } />
              <Route breadcrumbName="编辑订单" path="edit/:id" component={ OrderEdit } />
              <Route breadcrumbName="打印订单" path="print/:id" component={ OrderPrint } />
              <Route breadcrumbName="退补货" path="enterOut/:id/:goodId" component={ OrderEnterOut } />

              <Route breadcrumbName="测量安排" path=":id/measures" >
                <IndexRoute component={ MeasuresPage }  />
                <Route breadcrumbName="添加测量安排" path="add" component={ MeasureAdd } />
                <Route breadcrumbName="编辑测量安排" path="edit/:id" component={ MeasureEdit } />
              </Route>
              <Route breadcrumbName="设计安排" path=":id/designs" >
                <IndexRoute component={ DesignsPage }  />
                <Route breadcrumbName="添加设计安排" path="add" component={ DesignAdd } />
                <Route breadcrumbName="编辑设计安排" path="edit/:id" component={ DesignEdit } />
              </Route>
              <Route breadcrumbName="送货安排" path=":id/deliverys" >
                <IndexRoute component={ DeliverysPage }  />
                <Route breadcrumbName="送货单" path=":id/deliveryNote" component={ DeliveryNote } />
                <Route breadcrumbName="添加送货安排" path="add" component={ DeliveryAdd } />
                <Route breadcrumbName="编辑送货安排" path="edit/:id" component={ DeliveryEdit } />
              </Route>
              <Route breadcrumbName="安装安排" path=":id/installations" >
                <IndexRoute component={ InstallationsPage }  />
                <Route breadcrumbName="添加安装安排" path="add" component={ InstallationAdd } />
                <Route breadcrumbName="编辑安装安排" path="edit/:id" component={ InstallationEdit } />
                <Route breadcrumbName="安装安排服务单" path="note/:id" component={ InstallationNote } />
                <Route breadcrumbName="服务费计算" path="charges/:id" component={ InstallationCharges } />
              </Route>

            </Route>
          </Route>

          <Route breadcrumbName="客户管理" path="customers">
            <IndexRedirect to="customers" />
            <Route breadcrumbName="客户列表" path="customers" >
              <IndexRoute component={ CustomersPage }  />
              <Route breadcrumbName="添加客户信息" path="add" component={ CustomerAdd } />
              <Route breadcrumbName="编辑客户信息" path="edit/:id" component={ CustomerEdit } />
              <Route breadcrumbName="房产信息" path=":id/houses" component={ CustomerHouses } />
              <Route breadcrumbName="跟进记录" path=":id/tracks" component={ CustomerTracks } />
            </Route>
            <Route breadcrumbName="团购列表" path="groupon" >
              <IndexRoute component={ CustomersPage }  />
              <Route breadcrumbName="添加客户信息" path="add" component={ CustomerAdd } />
              <Route breadcrumbName="编辑客户信息" path="edit/:id" component={ CustomerEdit } />
              <Route breadcrumbName="房产信息" path="houses/:id" component={ CustomerHouses } />
            </Route>
          </Route>
          <Route breadcrumbName="采购管理" path="indents">
            <IndexRedirect to="indents" />
            <Route breadcrumbName="采购列表" path="indents" >
              <IndexRoute component={ SuppliersPage }  />
              <Route breadcrumbName="添加采购信息" path="add" component={ SupplierAdd } />
              <Route breadcrumbName="编辑采购信息" path="edit/:id" component={ SupplierEdit } />
            </Route>
            <Route breadcrumbName="品牌/供货商列表" path="suppliers" >
              <IndexRoute component={ SuppliersPage }  />
              <Route breadcrumbName="添加品牌/供货商信息" path="add" component={ SupplierAdd } />
              <Route breadcrumbName="编辑品牌/供货商信息" path="edit/:id" component={ SupplierEdit } />
            </Route>
          </Route>
          <Route breadcrumbName="系统管理" path="setting">
            <Route breadcrumbName="数据字典" path="codewords" >
              <IndexRoute component={ CodewordsPage }  />
              <Route breadcrumbName="添加数据字典" path="add" component={ CodewordAdd } />
              <Route breadcrumbName="编辑数据字典" path="edit/:id" component={ CodewordEdit } />
            </Route>
            <Route breadcrumbName="预警期" path="earlyWarming" >
              <IndexRoute component={ CodewordsPage }  />
              <Route breadcrumbName="添加预警期" path="add" component={ CodewordAdd } />
              <Route breadcrumbName="编辑预警期" path="edit/:id" component={ CodewordEdit } />
            </Route>
          </Route>
          <Route breadcrumbName="通用设置" path="config">
            <Route breadcrumbName="员工列表" path="users" >
              <IndexRoute component={ UserPage }  />
              <Route breadcrumbName="创建员工账号" path="add" component={ UserAdd } />
              <Route breadcrumbName="编辑员工账号" path="edit/:id" component={ UserEdit } />
            </Route>
            
            <Route breadcrumbName="角色" path="role">
              <IndexRoute component={ RoleListPage } />
              <Route breadcrumbName="新增角色" path="add" component={ RoleAddPage } />
              <Route breadcrumbName="编辑角色" path="edit/:id" component={ RoleEditPage } />
            </Route>
          </Route> 
        </Route>
      </Route>
    </Router>
  );
};
