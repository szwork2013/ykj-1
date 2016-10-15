import './index.html';
import './index.less';
import dva, { connect } from 'dva';
import { browserHistory } from 'dva/router';

import codewords from './codewords.json';

// 1. Initialize
const app = dva({
  history: browserHistory,
});

// 2. Plugins
app.use({
  // onReducer: r => (state, action) => {
  //   console.log(state);
  //   console.log(action);
  // },
  // onError: err => {
  //   console.log('dddd')
  //   console.log(err)
  // }
  // onEffect: (sagaWithCatch, routerRedux, model) => {
  //   console.log(sagaWithCatch)
  // }
});

// 3. Model
app.model(require('./models/role'));
app.model(require('./models/app'));
app.model(require('./models/auth'));
app.model(require('./models/login'));
app.model(require('./models/permission'));
app.model(require('./models/permissions'));
app.model(require('./models/roles'));


/**************订单管理*****************/
app.model(require('./models/orders'));
app.model(require('./models/measures'));
app.model(require('./models/designs'));
app.model(require('./models/deliverys'));
app.model(require('./models/deliveryGoods'));
app.model(require('./models/installations'));
app.model(require('./models/installGoods'));

/**************客户管理*****************/
app.model(require('./models/customers'));
app.model(require('./models/houses'));
app.model(require('./models/tracks'));
app.model(require('./models/tags'));

/**************采购管理*****************/
app.model(require('./models/suppliers'));

/**************系统管理*****************/
app.model(require('./models/codewords'));
app.model(require('./models/users'))

// 4. Router
app.router(require('./router'));

// 5. Start
app.start(document.getElementById('root'));
