import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ShoppingCart from './shopping-cart';
import ShoppingCartDetail from './shopping-cart-detail';
import ShoppingCartUpdate from './shopping-cart-update';
import ShoppingCartDeleteDialog from './shopping-cart-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ShoppingCartUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ShoppingCartUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ShoppingCartDetail} />
      <ErrorBoundaryRoute path={match.url} component={ShoppingCart} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ShoppingCartDeleteDialog} />
  </>
);

export default Routes;
