import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Row, Col, Alert, Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getActiveCartForCurrentUser, removeOrder } from 'app/entities/shopping-cart/shopping-cart.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Cart = () => {
  const dispatch = useAppDispatch();

  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const cart = useAppSelector(state => state.shoppingCart.entity);
  const loading = useAppSelector(state => state.shoppingCart.loading);

  useEffect(() => {
    dispatch(getActiveCartForCurrentUser());
  }, []);

  const remove = id => () => {
    dispatch(removeOrder(id));
  };

  return (
    <Row className="d-flex justify-content-center">
      <Col lg="9" md="12">
        {isAuthenticated ? (
          <>
            <h2>Your shopping cart</h2>
            <p className="lead">You have {cart?.orders?.length} items in your cart</p>
            {cart.orders && cart.orders.length > 0 ? (
              <>
                <div className="list-group">
                  {cart.orders.map((order, i) => (
                    <div key={`entity-${i}`} className="list-group-item list-group-item-action flex-column align-items-start">
                      <div className="row">
                        <div className="col-2 col-xs-12 justify-content-center">
                          {order.product && order.product.image ? (
                            <img
                              src={`data:${order.product.imageContentType};base64,${order.product.image}`}
                              style={{ maxHeight: '130px' }}
                            />
                          ) : null}
                        </div>
                        <div className="col col-xs-12">
                          <div className="d-flex w-100 justify-content-between">
                            <Button tag={Link} to={`/product/${order.product?.id}`} color="link" size="sm" className="px-0">
                              {order.product?.name}
                            </Button>
                            <small>Quantity: {order.quantity}</small>
                          </div>
                          <div>
                            <small>
                              Size: <span>{order.product?.itemSize}</span>
                            </small>
                          </div>
                          <p>
                            <small>Item Cost: € {order.product?.price}</small>
                          </p>
                          <div className="d-flex w-100 justify-content-between">
                            <p className="mb-1">Total cost: € {order.totalPrice}</p>
                            <div>
                              <Button onClick={remove(order?.id)} color="danger" size="sm">
                                <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Remove</span>
                              </Button>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
                <div className="d-flex justify-content-between py-4">
                  <h3>Total price: € {cart.totalPrice}</h3>
                  <Button tag={Link} to={`/checkout`} color="primary" size="lg">
                    <FontAwesomeIcon icon="cart-arrow-down" /> <span className="d-none d-md-inline">Checkout</span>
                  </Button>
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

export default Cart;
