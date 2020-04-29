import './checkout.scss';

import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, useParams, useLocation } from 'react-router-dom';
import { Button } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { closeShoppingCart } from 'app/entities/shopping-cart/shopping-cart.reducer';

export const Message = ({ type, reason }) => {
  let msg: JSX.Element, img: string;
  switch (type) {
    case 'pending':
      msg = <span>Your order has been received! Payment completion pending.</span>;
      img = 'success';
      break;
    case 'failed':
      msg = <span>The payment was refused. Please try a different payment method or card.</span>;
      img = 'failed';
      break;
    case 'error':
      msg = (
        <span>
          Error! Reason: {reason || 'Internal error'}, refer to&nbsp;
          <a href="https://docs.adyen.com/development-resources/response-handling">Response handling.</a>
        </span>
      );
      img = 'failed';
      break;
    default:
      msg = <span>Your order has been successfully placed.</span>;
      img = 'success';
  }

  return (
    <>
      <div className={`status-image ${img}`} />
      {['failed', 'error'].includes(type) ? null : <div className="status-image thank-you" />}
      <p className="status-message">{msg}</p>
    </>
  );
};

export type ICheckoutStatusProp = DispatchProps;

export const CheckoutStatus = (props: ICheckoutStatusProp) => {
  const { type } = useParams();
  const query = new URLSearchParams(useLocation().search);
  const reason = query ? query.get('reason') : '';
  const paymentType = query ? query.get('paymentType') : '';

  useEffect(() => {
    reason === 'Authorised' && props.closeShoppingCart(paymentType);
  }, []);

  return (
    <div className="status-container">
      <div className="status">
        <Message type={type} reason={reason} />
        <Button tag={Link} to="/" className="button">
          Return Home
        </Button>
      </div>
    </div>
  );
};

const mapDispatchToProps = {
  closeShoppingCart
};

type DispatchProps = typeof mapDispatchToProps;

export default connect(null, mapDispatchToProps)(CheckoutStatus);
