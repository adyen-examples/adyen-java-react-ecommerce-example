import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
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

  const handleSyncList = () => {
    props.getEntities();
  };

  const { shoppingCartList, match, loading } = props;
  return (
    <div>
      <h2 id="shopping-cart-heading" data-cy="ShoppingCartHeading">
        Shopping Carts
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Shopping Cart
          </Link>
        </div>
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
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${shoppingCart.id}`} color="link" size="sm">
                      {shoppingCart.id}
                    </Button>
                  </td>
                  <td>{shoppingCart.id}</td>
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
                      <Button tag={Link} to={`${match.url}/${shoppingCart.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      {shoppingCart.status === OrderStatus.PAID || shoppingCart.status === OrderStatus.REFUND_FAILED ? (
                        <Button color="warning" size="sm" onClick={refund(shoppingCart)}>
                          <FontAwesomeIcon icon="sync" /> <span className="d-none d-md-inline">Refund</span>
                        </Button>
                      ) : null}
                      <Button tag={Link} to={`${match.url}/${shoppingCart.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${shoppingCart.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
  loading: shoppingCart.loading,
});

const mapDispatchToProps = {
  getEntities,
  refundPayment,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShoppingCart);
