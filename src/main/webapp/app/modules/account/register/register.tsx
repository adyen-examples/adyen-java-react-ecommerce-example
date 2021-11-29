import React, { useState, useEffect } from 'react';

import { connect } from 'react-redux';
import { AvForm, AvField } from 'availity-reactstrap-validation';
import { Row, Col, Alert, Button } from 'reactstrap';

import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { IRootState } from 'app/shared/reducers';
import { handleRegister, reset } from './register.reducer';

export type IRegisterProps = DispatchProps;

export const RegisterPage = (props: IRegisterProps) => {
  const [password, setPassword] = useState('');

  useEffect(
    () => () => {
      props.reset();
    },
    []
  );

  const handleValidSubmit = (event, values) => {
    props.handleRegister(values.username, values.email, values.firstPassword);
    event.preventDefault();
  };

  const updatePassword = event => setPassword(event.target.value);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1 id="register-title" data-cy="registerTitle">
            Registration
          </h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          <AvForm id="register-form" onValidSubmit={handleValidSubmit}>
            <AvField
              name="username"
              label="Username"
              placeholder={'Your username'}
              validate={{
                required: { value: true, errorMessage: 'Your username is required.' },
                pattern: {
                  value: '^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$',
                  errorMessage: 'Your username is invalid.',
                },
                minLength: { value: 1, errorMessage: 'Your username is required to be at least 1 character.' },
                maxLength: { value: 50, errorMessage: 'Your username cannot be longer than 50 characters.' },
              }}
              data-cy="username"
            />
            <AvField
              name="email"
              label="Email"
              placeholder={'Your email'}
              type="email"
              validate={{
                required: { value: true, errorMessage: 'Your email is required.' },
                minLength: { value: 5, errorMessage: 'Your email is required to be at least 5 characters.' },
                maxLength: { value: 254, errorMessage: 'Your email cannot be longer than 50 characters.' },
              }}
              data-cy="email"
            />
            <AvField
              name="firstPassword"
              label="New password"
              placeholder={'New password'}
              type="password"
              onChange={updatePassword}
              validate={{
                required: { value: true, errorMessage: 'Your password is required.' },
                minLength: { value: 4, errorMessage: 'Your password is required to be at least 4 characters.' },
                maxLength: { value: 50, errorMessage: 'Your password cannot be longer than 50 characters.' },
              }}
              data-cy="firstPassword"
            />
            <PasswordStrengthBar password={password} />
            <AvField
              name="secondPassword"
              label="New password confirmation"
              placeholder="Confirm the new password"
              type="password"
              validate={{
                required: { value: true, errorMessage: 'Your confirmation password is required.' },
                minLength: { value: 4, errorMessage: 'Your confirmation password is required to be at least 4 characters.' },
                maxLength: { value: 50, errorMessage: 'Your confirmation password cannot be longer than 50 characters.' },
                match: { value: 'firstPassword', errorMessage: 'The password and its confirmation do not match!' },
              }}
              data-cy="secondPassword"
            />
            <Button id="register-submit" color="primary" type="submit" data-cy="submit">
              Register
            </Button>
          </AvForm>
          <p>&nbsp;</p>
          <Alert color="warning">
            <span>If you want to</span>
            <a className="alert-link"> sign in</a>
            <span>
              , you can try the default accounts:
              <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
              <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).
            </span>
          </Alert>
        </Col>
      </Row>
    </div>
  );
};

const mapDispatchToProps = { handleRegister, reset };
type DispatchProps = typeof mapDispatchToProps;

export default connect(null, mapDispatchToProps)(RegisterPage);
