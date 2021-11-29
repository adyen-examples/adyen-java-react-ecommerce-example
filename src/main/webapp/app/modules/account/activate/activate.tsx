import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Row, Col, Alert } from 'reactstrap';
import { getUrlParameter } from 'react-jhipster';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { activateAction, reset } from './activate.reducer';

const successAlert = (
  <Alert color="success">
    <strong>Your user account has been activated.</strong> Please
    <Link to="/login" className="alert-link">
      sign in
    </Link>
    .
  </Alert>
);

const failureAlert = (
  <Alert color="danger">
    <strong>Your user could not be activated.</strong> Please use the registration form to sign up.
  </Alert>
);

export const ActivatePage = (props: RouteComponentProps<{ key: any }>) => {
  const dispatch = useAppDispatch();
  useEffect(() => {
    const key = getUrlParameter('key', props.location.search);
    dispatch(activateAction(key));
    return () => {
      dispatch(reset());
    };
  }, []);

  const { activationSuccess, activationFailure } = useAppSelector(state => state.activate);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1>Activation</h1>
          {activationSuccess ? successAlert : undefined}
          {activationFailure ? failureAlert : undefined}
        </Col>
      </Row>
    </div>
  );
};

export default ActivatePage;
