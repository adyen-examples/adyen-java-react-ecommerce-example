import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './payment-cache.reducer';
import { IPaymentCache } from 'app/shared/model/payment-cache.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPaymentCacheDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PaymentCacheDetail = (props: IPaymentCacheDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { paymentCacheEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          PaymentCache [<b>{paymentCacheEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="orderRef">Order Ref</span>
          </dt>
          <dd>{paymentCacheEntity.orderRef}</dd>
          <dt>
            <span id="originalHost">Original Host</span>
          </dt>
          <dd>{paymentCacheEntity.originalHost}</dd>
          <dt>
            <span id="paymentData">Payment Data</span>
          </dt>
          <dd>{paymentCacheEntity.paymentData}</dd>
          <dt>
            <span id="paymentType">Payment Type</span>
          </dt>
          <dd>{paymentCacheEntity.paymentType}</dd>
          <dt>User</dt>
          <dd>{paymentCacheEntity.user ? paymentCacheEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/payment-cache" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment-cache/${paymentCacheEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ paymentCache }: IRootState) => ({
  paymentCacheEntity: paymentCache.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PaymentCacheDetail);
