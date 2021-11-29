import React from 'react';

import { connect } from 'react-redux';
import { AvForm, AvField } from 'availity-reactstrap-validation';
import { Button, Alert, Col, Row } from 'reactstrap';

import { handlePasswordResetInit, reset } from '../password-reset.reducer';

export type IPasswordResetInitProps = DispatchProps;

export class PasswordResetInit extends React.Component<IPasswordResetInitProps> {
  componentWillUnmount() {
    this.props.reset();
  }

  handleValidSubmit = (event, values) => {
    this.props.handlePasswordResetInit(values.email);
    event.preventDefault();
  };

  render() {
    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h1>Reset your password</h1>
            <Alert color="warning">
              <p>Enter the email address you used to register</p>
            </Alert>
            <AvForm onValidSubmit={this.handleValidSubmit}>
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
                data-cy="emailResetPassword"
              />
              <Button color="primary" type="submit" data-cy="submit">
                Reset password
              </Button>
            </AvForm>
          </Col>
        </Row>
      </div>
    );
  }
}

const mapDispatchToProps = { handlePasswordResetInit, reset };

type DispatchProps = typeof mapDispatchToProps;

export default connect(null, mapDispatchToProps)(PasswordResetInit);
