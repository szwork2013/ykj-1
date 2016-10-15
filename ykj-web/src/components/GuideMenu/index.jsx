import React, { PropTypes } from 'react';
import { Link } from 'dva/router';

import { Menu, Icon } from 'antd';

import styles from './index.less';


const GuideMenu = ({ menuCurrent, menuOpenKeys, dispatch }) => {
  const onToggle = info => {
    dispatch({ type: 'app/changeMenu', payload: {
      menu_open_keys: info.open ? info.keyPath : info.keyPath.slice(1),
    } })
  }

  return (
    <Menu
      className={ styles['guide-menu'] }
      openKeys={menuOpenKeys}
      selectedKeys={[menuCurrent]}
      mode="inline"

      onOpen={ onToggle }
      onClose={ onToggle }
      onClick={ (e) => {
        dispatch({ type: 'app/changeMenu', payload: {
          menu_current: e.key,
          menu_open_keys: e.keyPath.slice(1),
        } })
      } }
    >
      <Menu.SubMenu
        key="/order"
        title={ <span><Icon type="mail" /><span>订单管理</span></span> }
      >
        <Menu.Item key="/order/orders">
          <Link to="/order/orders">订单列表</Link>
        </Menu.Item>

        <Menu.Item key="/order/orders/overOrders">
          <Link to="/order/orders/overOrders">已完成订单列表</Link>
        </Menu.Item>

        <Menu.Item key="/order/orders/add">
          <Link to="/order/orders/add">下订单</Link>
        </Menu.Item>
      </Menu.SubMenu>
      
      <Menu.SubMenu
        key="/config"
        title={ <span><Icon type="mail" /><span>通用设置</span></span> }
      >
        <Menu.Item key="/config/user">
          <Link to="/config/users">员工管理</Link>
        </Menu.Item>

        <Menu.Item key="/config/manager">
          <Link to="/config/manager">管理员管理</Link>
        </Menu.Item>

        <Menu.Item key="/config/role">
          <Link to="/config/role">角色管理</Link>
        </Menu.Item>
      </Menu.SubMenu>
      
      <Menu.SubMenu
        key="/stock"
        title={ <span><Icon type="mail" /><span>库存管理</span></span> }
      >
        <Menu.Item key="/stock/list">
          <Link to="/stock/list">库存列表</Link>
        </Menu.Item>
        <Menu.Item key="/stock/goods">
          <Link to="/stock/goods">商品列表</Link>
        </Menu.Item>
        <Menu.Item key="/stock/inbound">
          <Link to="/stock/inbound">商品入库</Link>
        </Menu.Item>
        <Menu.Item key="/stock/outbound">
          <Link to="/stock/outbound">商品出库</Link>
        </Menu.Item>
        <Menu.Item key="/stock/inboundHistpry">
          <Link to="/stock/espoints">入库历史</Link>
        </Menu.Item>
        <Menu.Item key="/stock/outboundHistpry">
          <Link to="/stock/espoints">出库历史</Link>
        </Menu.Item>
        <Menu.Item key="/stock/inventory">
          <Link to="/stock/inventory">库存盘点</Link>
        </Menu.Item>
      </Menu.SubMenu>
      <Menu.SubMenu
        key="/customers"
        title={ <span><Icon type="mail" /><span>客户管理</span></span> }
      >
        <Menu.Item key="/customers/customers">
          <Link to="/customers/customers">客户列表</Link>
        </Menu.Item>
        <Menu.Item key="/customers/groupon">
          <Link to="/customers/groupon">团购列表</Link>
        </Menu.Item>
      </Menu.SubMenu>
      <Menu.SubMenu
        key="/setting"
        title={ <span><Icon type="mail" /><span>系统管理</span></span> }
      >
        <Menu.Item key="/setting/codewords">
          <Link to="/setting/codewords">数据字典</Link>
        </Menu.Item>
        <Menu.Item key="/setting/earlyWarming">
          <Link to="/setting/earlyWarming">预警期</Link>
        </Menu.Item>
      </Menu.SubMenu>

      <Menu.SubMenu
        key="/indents"
        title={ <span><Icon type="mail" /><span>采购管理</span></span> }
      >
        <Menu.Item key="/indents/indents">
          <Link to="/indents/indents">采购列表</Link>
        </Menu.Item>
        <Menu.Item key="/indents/suppliers">
          <Link to="/indents/suppliers">品牌/供货商列表</Link>
        </Menu.Item>
      </Menu.SubMenu>
    </Menu>
  )
}

GuideMenu.propTypes = {
  menuCurrent: PropTypes.string,
  dispatch: PropTypes.func.isRequired,
}

export default GuideMenu;
