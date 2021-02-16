import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PaymentCache from './payment-cache';
import PaymentCacheDetail from './payment-cache-detail';
import PaymentCacheUpdate from './payment-cache-update';
import PaymentCacheDeleteDialog from './payment-cache-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PaymentCacheUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PaymentCacheUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PaymentCacheDetail} />
      <ErrorBoundaryRoute path={match.url} component={PaymentCache} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PaymentCacheDeleteDialog} />
  </>
);

export default Routes;
