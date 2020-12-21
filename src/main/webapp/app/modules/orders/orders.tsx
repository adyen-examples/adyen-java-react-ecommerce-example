import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Row, Col, Alert, Button, Badge } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { TextFormat } from 'react-jhipster';

import { IRootState } from 'app/shared/reducers';
import { getCartsForCurrentUser } from 'app/entities/shopping-cart/shopping-cart.reducer';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { refundPayment } from 'app/modules/checkout/checkout.reducer';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';

export type IOrdersProp = StateProps & DispatchProps;

export const Orders = (props: IOrdersProp) => {
  useEffect(() => {
    props.getCartsForCurrentUser();
  }, []);

  const refund = ref => () => {
    props.refundPayment(ref, props.getCartsForCurrentUser);
  };

  const { isAuthenticated, carts, loading } = props;

  return (
    <Row className="d-flex justify-content-center">
      <Col lg="9" md="12">
        {isAuthenticated ? (
          <>
            <h2>Your Orders</h2>
            <p className="lead">You have {carts?.length} past orders</p>
            {carts && carts.length > 0 ? (
              <>
                <div className="list-group">
                  {carts.map((cart, i) => (
                    <div key={`entity-${i}`} className="list-group-item list-group-item-action flex-column align-items-start">
                      <div className="row">
                        <div className="col col-xs-12">
                          <div className="d-flex w-100 justify-content-between">
                            {cart.placedDate ? <TextFormat type="date" value={cart.placedDate} format={APP_DATE_FORMAT} /> : null}
                            <small>ID: {cart.id}</small>
                          </div>
                          <div>
                            <small>Status: <Badge color="info">{cart.status}</Badge></small>
                          </div>
                          <div>
                            <small>Payment type: {cart.paymentMethod}</small>
                          </div>
                          <p>
                            <small>Payment reference: {cart.paymentReference}</small>
                          </p>
                          <div className="d-flex w-100 justify-content-between">
                            <p className="mb-1">Total price: € {cart.totalPrice}</p>
                            <div>
                              {cart.status === OrderStatus.PAID ? (
                                <Button onClick={refund(cart)} color="warning" size="sm">
                                  <FontAwesomeIcon icon="sync" /> <span className="d-none d-md-inline">Cancel &amp; Refund</span>
                                </Button>
                              ) : null}
                              {cart.status === OrderStatus.REFUND_INITIATED ? (
                                <Button color="warning" size="sm" disabled>
                                  Refund pending
                                </Button>
                              ) : null}
                            </div>
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

const mapStateToProps = ({ authentication, shoppingCart }: IRootState) => ({
  isAuthenticated: authentication.isAuthenticated,
  carts: shoppingCart.entities,
  loading: shoppingCart.loading
});

const mapDispatchToProps = {
  getCartsForCurrentUser,
  refundPayment
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Orders);
