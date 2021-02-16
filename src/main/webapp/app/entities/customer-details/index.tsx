import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CustomerDetails from './customer-details';
import CustomerDetailsDetail from './customer-details-detail';
import CustomerDetailsUpdate from './customer-details-update';
import CustomerDetailsDeleteDialog from './customer-details-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CustomerDetailsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CustomerDetailsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CustomerDetailsDetail} />
      <ErrorBoundaryRoute path={match.url} component={CustomerDetails} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CustomerDetailsDeleteDialog} />
  </>
);

export default Routes;
