import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductCategory from './product-category';
import ProductCategoryDetail from './product-category-detail';
import ProductCategoryUpdate from './product-category-update';
import ProductCategoryDeleteDialog from './product-category-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductCategoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductCategoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductCategoryDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductCategory} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProductCategoryDeleteDialog} />
  </>
);

export default Routes;
