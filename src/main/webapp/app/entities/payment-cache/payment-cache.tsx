import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './payment-cache.reducer';
import { IPaymentCache } from 'app/shared/model/payment-cache.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPaymentCacheProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const PaymentCache = (props: IPaymentCacheProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { paymentCacheList, match, loading } = props;
  return (
    <div>
      <h2 id="payment-cache-heading">
        Payment Caches
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Payment Cache
        </Link>
      </h2>
      <div className="table-responsive">
        {paymentCacheList && paymentCacheList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Order Ref</th>
                <th>Original Host</th>
                <th>Payment Data</th>
                <th>Payment Type</th>
                <th>User</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {paymentCacheList.map((paymentCache, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${paymentCache.id}`} color="link" size="sm">
                      {paymentCache.id}
                    </Button>
                  </td>
                  <td>{paymentCache.orderRef}</td>
                  <td>{paymentCache.originalHost}</td>
                  <td>{paymentCache.paymentData}</td>
                  <td>{paymentCache.paymentType}</td>
                  <td>{paymentCache.user ? paymentCache.user.login : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${paymentCache.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${paymentCache.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${paymentCache.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Payment Caches found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ paymentCache }: IRootState) => ({
  paymentCacheList: paymentCache.entities,
  loading: paymentCache.loading
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PaymentCache);
