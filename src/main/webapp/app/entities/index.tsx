import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Product from './product';
import ProductCategory from './product-category';
import CustomerDetails from './customer-details';
import ShoppingCart from './shopping-cart';
import ProductOrder from './product-order';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}product`} component={Product} />
      <ErrorBoundaryRoute path={`${match.url}product-category`} component={ProductCategory} />
      <ErrorBoundaryRoute path={`${match.url}customer-details`} component={CustomerDetails} />
      <ErrorBoundaryRoute path={`${match.url}shopping-cart`} component={ShoppingCart} />
      <ErrorBoundaryRoute path={`${match.url}product-order`} component={ProductOrder} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
