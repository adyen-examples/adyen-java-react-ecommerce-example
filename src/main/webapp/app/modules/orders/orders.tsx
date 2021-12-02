import React, { useEffect } from 'react';
import { Row, Col, Alert, Button, Badge } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { TextFormat } from 'react-jhipster';

import { getCartsForCurrentUser } from 'app/entities/shopping-cart/shopping-cart.reducer';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { refundPayment } from 'app/modules/checkout/checkout.reducer';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Orders = () => {
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const carts = useAppSelector(state => state.shoppingCart.entities);
  const loading = useAppSelector(state => state.shoppingCart.loading);

  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getCartsForCurrentUser());
  }, [dispatch]);

  const refund = ref => () => {
    dispatch(refundPayment({ cart: ref, action: () => dispatch(getCartsForCurrentUser()) }));
  };

  const getStatusAction = cart => {
    switch (cart.status) {
      case OrderStatus.PAID:
      case OrderStatus.REFUND_FAILED:
        return (
          <Button onClick={refund(cart)} color="warning" size="sm">
            <FontAwesomeIcon icon="sync" /> <span className="d-none d-md-inline">Cancel &amp; Refund</span>
          </Button>
        );
      case OrderStatus.REFUND_INITIATED:
        return <Badge color="info">Refund pending</Badge>;
      default:
        return null;
    }
  };

  return (
    <Row className="d-flex justify-content-center">
      <Col lg="9" md="12">
        {isAuthenticated ? (
          <>
            <h2>
              Your Orders
              <Button color="primary" className="float-right jh-create-entity" onClick={() => dispatch(getCartsForCurrentUser())}>
                <FontAwesomeIcon icon="sync" />
                &nbsp; Refresh
              </Button>
            </h2>
            <p className="lead">You have {carts?.length} past orders</p>
            {carts && carts.length > 0 ? (
              <>
                <div className="list-group">
                  {[...carts]
                    .sort((a, b) => b.id - a.id)
                    .map((cart, i) => (
                      <div key={`entity-${i}`} className="list-group-item list-group-item-action flex-column align-items-start">
                        <div className="row">
                          <div className="col col-xs-12">
                            <div className="d-flex w-100 justify-content-between">
                              {cart.placedDate ? <TextFormat type="date" value={cart.placedDate} format={APP_DATE_FORMAT} /> : null}
                              <small>ID: {cart.id}</small>
                            </div>
                            <div>
                              <small>
                                Status: <Badge color="info">{cart.status}</Badge>
                              </small>
                            </div>
                            <div>
                              <small>Payment type: {cart.paymentMethod}</small>
                            </div>
                            <p>
                              <small>Payment reference: {cart.paymentReference}</small>
                            </p>
                            <div className="d-flex w-100 justify-content-between">
                              <p className="mb-1">Total price: € {cart.totalPrice}</p>
                              <div>{getStatusAction(cart)}</div>
                            </div>
                          </div>
                        </div>
                      </div>
                    ))}
                </div>
              </>
            ) : (
              !loading && <div className="alert alert-warning">No items found</div>
            )}
          </>
        ) : (
          <div>
            <Alert color="warning">Not authorized. Please log in first</Alert>
          </div>
        )}
      </Col>
    </Row>
  );
};

export default Orders;
