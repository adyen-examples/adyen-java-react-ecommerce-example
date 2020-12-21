import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { refundPayment } from 'app/modules/checkout/checkout.reducer';
import { getEntities } from './shopping-cart.reducer';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';

export interface IShoppingCartProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ShoppingCart = (props: IShoppingCartProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const refund = ref => () => {
    props.refundPayment(ref, props.getEntities);
  };

  const { shoppingCartList, match, loading } = props;
  return (
    <div>
      <h2 id="shopping-cart-heading">
        Shopping Carts
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Shopping Cart
        </Link>
      </h2>
      <div className="table-responsive">
        {shoppingCartList && shoppingCartList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Placed Date</th>
                <th>Status</th>
                <th>Total Price</th>
                <th>Payment Method</th>
                <th>Payment Reference</th>
                <th>Payment Modification Reference</th>
                <th>Customer Details</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {shoppingCartList.map((shoppingCart, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${shoppingCart.id}`} color="link" size="sm">
                      {shoppingCart.id}
                    </Button>
                  </td>
                  <td>
                    {shoppingCart.placedDate ? <TextFormat type="date" value={shoppingCart.placedDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{shoppingCart.status}</td>
                  <td>{shoppingCart.totalPrice}</td>
                  <td>{shoppingCart.paymentMethod}</td>
                  <td>{shoppingCart.paymentReference}</td>
                  <td>{shoppingCart.paymentModificationReference}</td>
                  <td>
                    {shoppingCart.customerDetails ? (
                      <Link to={`customer-details/${shoppingCart.customerDetails.id}`}>{shoppingCart.customerDetails.user?.login}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${shoppingCart.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      {shoppingCart.status === OrderStatus.PAID ? (
                        <Button color="warning" size="sm" onClick={refund(shoppingCart)}>
                          <FontAwesomeIcon icon="sync" /> <span className="d-none d-md-inline">Refund</span>
                        </Button>
                      ) : null}
                      <Button tag={Link} to={`${match.url}/${shoppingCart.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${shoppingCart.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Shopping Carts found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ shoppingCart }: IRootState) => ({
  shoppingCartList: shoppingCart.entities,
  loading: shoppingCart.loading
});

const mapDispatchToProps = {
  getEntities,
  refundPayment
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShoppingCart);
