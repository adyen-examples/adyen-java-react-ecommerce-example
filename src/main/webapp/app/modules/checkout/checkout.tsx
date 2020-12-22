import React from 'react';
import { connect } from 'react-redux';
import { Row, Col, ButtonGroup, Button } from 'reactstrap';
import AdyenCheckout from '@adyen/adyen-web';
import '@adyen/adyen-web/dist/adyen.css';

import { getAdyenConfig, getPaymentMethods, initiatePayment, submitAdditionalDetails } from './checkout.reducer';
import { closeShoppingCart, getActiveCartForCurrentUser } from 'app/entities/shopping-cart/shopping-cart.reducer';
import { IRootState } from 'app/shared/reducers';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';

export interface ICheckoutProp extends StateProps, DispatchProps {}

class CheckoutContainer extends React.Component<ICheckoutProp> {
  private paymentContainer = React.createRef<HTMLDivElement>();
  private paymentComponent: any = null;
  private checkout: any;

  componentDidMount() {
    this.props.getActiveCartForCurrentUser();
    this.props.getAdyenConfig();
    this.props.getPaymentMethods();
  }

  componentDidUpdate(prevProps: ICheckoutProp) {
    const { paymentMethodsRes, config, paymentRes, paymentDetailsRes, errorMessage } = this.props;
    if (errorMessage && errorMessage !== prevProps.errorMessage) {
      window.location.href = `/checkout/status/error?reason=${errorMessage}`;
      return;
    }
    if (paymentMethodsRes && config && (paymentMethodsRes !== prevProps.paymentMethodsRes || config !== prevProps.config)) {
      this.checkout = new AdyenCheckout({
        ...config,
        paymentMethodsResponse: this.removeNilFields(paymentMethodsRes),
        onAdditionalDetails: this.onAdditionalDetails,
        onSubmit: this.onSubmit
      });
    }
    if (paymentRes && paymentRes !== prevProps.paymentRes) {
      this.processPaymentResponse(paymentRes);
    }
    if (paymentRes && paymentDetailsRes !== prevProps.paymentDetailsRes) {
      this.processPaymentResponse(paymentDetailsRes);
    }
  }

  removeNilFields = obj => {
    for (const propName in obj) {
      if (obj[propName] === null || obj[propName] === undefined) {
        delete obj[propName];
      }
    }
    return obj;
  };

  processPaymentResponse = paymentRes => {
    if (paymentRes.action) {
      this.paymentComponent.handleAction(paymentRes.action);
    } else {
      let urlPart;
      switch (paymentRes.resultCode) {
        case 'Authorised':
          this.props.closeShoppingCart('scheme', paymentRes.pspReference, OrderStatus.PAID);
          urlPart = 'success';
          break;
        case 'Pending':
          this.props.closeShoppingCart('scheme', paymentRes.pspReference, OrderStatus.PENDING);
          urlPart = 'pending';
          break;
        case 'Refused':
          urlPart = 'failed';
          break;
        default:
          urlPart = 'error';
          break;
      }
      window.location.href = `/checkout/status/${urlPart}?reason=${paymentRes.resultCode}`;
    }
  };

  onSubmit = (state, component) => {
    if (state.isValid) {
      this.props.initiatePayment({
        ...state.data,
        origin: window.location.origin
      });
      this.paymentComponent = component;
    }
  };

  onAdditionalDetails = (state, component) => {
    this.props.submitAdditionalDetails(state.data);
    this.paymentComponent = component;
  };

  handlePaymentSelect = (type: string) => () => {
    this.checkout.create(type).mount(this.paymentContainer?.current);
  };

  render() {
    const { cart } = this.props;

    return (
      <Row className="d-flex justify-content-center" style={{ minHeight: '80vh' }}>
        <Col lg="9" md="12">
          <h2>Make payment</h2>
          <p className="lead">You are paying total of € {cart.totalPrice}</p>
          <Row className="pt-4">
            <Col md="4" className="d-flex flex-column">
              <label>
                <strong>Choose a payment type</strong>
              </label>
              <ButtonGroup vertical>
                <Button onClick={this.handlePaymentSelect('card')}>Credit Card</Button>
                <Button onClick={this.handlePaymentSelect('ideal')}>iDEAL</Button>
              </ButtonGroup>
            </Col>
            <Col md="8">
              <div ref={this.paymentContainer} className="payment"></div>
            </Col>
          </Row>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ checkout, shoppingCart }: IRootState) => ({
  paymentMethodsRes: checkout.paymentMethodsRes,
  paymentRes: checkout.paymentRes,
  paymentDetailsRes: checkout.paymentDetailsRes,
  config: checkout.config,
  errorMessage: checkout.errorMessage,
  cart: shoppingCart.entity
});

const mapDispatchToProps = {
  getAdyenConfig,
  getPaymentMethods,
  initiatePayment,
  submitAdditionalDetails,
  closeShoppingCart,
  getActiveCartForCurrentUser
};
type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CheckoutContainer);
