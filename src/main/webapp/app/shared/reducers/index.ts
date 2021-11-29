import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication from './authentication';
import applicationProfile from './application-profile';

import administration from 'app/modules/administration/administration.reducer';
import userManagement from 'app/modules/administration/user-management/user-management.reducer';
import register from 'app/modules/account/register/register.reducer';
import activate from 'app/modules/account/activate/activate.reducer';
import password from 'app/modules/account/password/password.reducer';
import settings from 'app/modules/account/settings/settings.reducer';
import passwordReset from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import product from 'app/entities/product/product.reducer';
// prettier-ignore
import productCategory from 'app/entities/product-category/product-category.reducer';
// prettier-ignore
import customerDetails from 'app/entities/customer-details/customer-details.reducer';
// prettier-ignore
import shoppingCart from 'app/entities/shopping-cart/shopping-cart.reducer';
// prettier-ignore
import productOrder from 'app/entities/product-order/product-order.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  product,
  productCategory,
  customerDetails,
  shoppingCart,
  productOrder,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
