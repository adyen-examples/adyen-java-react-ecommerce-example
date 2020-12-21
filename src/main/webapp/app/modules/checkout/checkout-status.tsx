import './checkout.scss';

import React from 'react';
import { Link, useParams, useLocation } from 'react-router-dom';
import { Button } from 'reactstrap';

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

export default () => {
  const { type } = useParams<{ type: string }>();
  const query = new URLSearchParams(useLocation().search);
  const reason = query ? query.get('reason') : '';

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
