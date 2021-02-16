import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './product-order.reducer';
import { IProductOrder } from 'app/shared/model/product-order.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductOrderProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ProductOrder = (props: IProductOrderProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { productOrderList, match, loading } = props;
  return (
    <div>
      <h2 id="product-order-heading">
        Product Orders
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Product Order
        </Link>
      </h2>
      <div className="table-responsive">
        {productOrderList && productOrderList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Quantity</th>
                <th>Total Price</th>
                <th>Product</th>
                <th>Cart</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productOrderList.map((productOrder, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${productOrder.id}`} color="link" size="sm">
                      {productOrder.id}
                    </Button>
                  </td>
                  <td>{productOrder.quantity}</td>
                  <td>{productOrder.totalPrice}</td>
                  <td>{productOrder.product ? <Link to={`product/${productOrder.product.id}`}>{productOrder.product.name}</Link> : ''}</td>
                  <td>{productOrder.cart ? <Link to={`shopping-cart/${productOrder.cart.id}`}>{productOrder.cart.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${productOrder.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${productOrder.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${productOrder.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Product Orders found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ productOrder }: IRootState) => ({
  productOrderList: productOrder.entities,
  loading: productOrder.loading
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductOrder);
