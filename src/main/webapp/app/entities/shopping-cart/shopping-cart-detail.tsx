import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './shopping-cart.reducer';
import { IShoppingCart } from 'app/shared/model/shopping-cart.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShoppingCartDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ShoppingCartDetail = (props: IShoppingCartDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { shoppingCartEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ShoppingCart [<b>{shoppingCartEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="placedDate">Placed Date</span>
          </dt>
          <dd>
            {shoppingCartEntity.placedDate ? (
              <TextFormat value={shoppingCartEntity.placedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{shoppingCartEntity.status}</dd>
          <dt>
            <span id="totalPrice">Total Price</span>
          </dt>
          <dd>{shoppingCartEntity.totalPrice}</dd>
          <dt>
            <span id="paymentMethod">Payment Method</span>
          </dt>
          <dd>{shoppingCartEntity.paymentMethod}</dd>
          <dt>Customer Details</dt>
          <dd>{shoppingCartEntity.customerDetails ? shoppingCartEntity.customerDetails.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/shopping-cart" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/shopping-cart/${shoppingCartEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ shoppingCart }: IRootState) => ({
  shoppingCartEntity: shoppingCart.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShoppingCartDetail);
