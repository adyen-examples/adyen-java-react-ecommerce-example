import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductOrder from './product-order';
import ProductOrderDetail from './product-order-detail';
import ProductOrderUpdate from './product-order-update';
import ProductOrderDeleteDialog from './product-order-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductOrderUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductOrderUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductOrderDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductOrder} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProductOrderDeleteDialog} />
  </>
);

export default Routes;
