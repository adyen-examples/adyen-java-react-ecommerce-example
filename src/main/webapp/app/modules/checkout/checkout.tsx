import React, { useEffect, useRef, useState } from 'react';
import { Row, Col, ButtonGroup, Button } from 'reactstrap';
import AdyenCheckout from '@adyen/adyen-web';
import '@adyen/adyen-web/dist/adyen.css';

import { getAdyenConfig, getPaymentMethods, initiatePayment, submitAdditionalDetails } from './checkout.reducer';
import { closeShoppingCart, getActiveCartForCurrentUser } from 'app/entities/shopping-cart/shopping-cart.reducer';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';
import { useAppDispatch, useAppSelector } from 'app/config/store';

const CheckoutContainer = () => {
  const paymentContainer = useRef();
  const [paymentComponent, setPaymentComponent] = useState<any>(null);
  const [checkout, setCheckout] = useState<AdyenCheckout>(null);

  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getActiveCartForCurrentUser());
    dispatch(getAdyenConfig());
    dispatch(getPaymentMethods());
  }, []);

  const paymentMethodsRes = useAppSelector(state => state.checkout.paymentMethodsRes);
  const paymentRes = useAppSelector(state => state.checkout.paymentRes);
  const paymentDetailsRes = useAppSelector(state => state.checkout.paymentDetailsRes);
  const config = useAppSelector(state => state.checkout.config);
  const errorMessage = useAppSelector(state => state.checkout.errorMessage);
  const cart = useAppSelector(state => state.shoppingCart.entity);

  useEffect(() => {
    if (errorMessage) {
      // TODO use react router
      window.location.href = `/checkout/status/error?reason=${errorMessage}`;
      return;
    }
  }, [errorMessage]);

  useEffect(() => {
    if (paymentMethodsRes && config) {
      setCheckout(
        new AdyenCheckout({
          ...config,
          paymentMethodsResponse: paymentMethodsRes,
          onAdditionalDetails,
          onSubmit,
        })
      );
    }
  }, [config, paymentMethodsRes]);

  useEffect(() => {
    if (paymentRes) {
      processPaymentResponse(paymentRes);
    }
  }, [paymentRes]);

  useEffect(() => {
    if (paymentDetailsRes) {
      processPaymentResponse(paymentDetailsRes);
    }
  }, [paymentDetailsRes]);

  const processPaymentResponse = response => {
    if (response.action) {
      paymentComponent.handleAction(response.action);
    } else {
      let urlPart;
      switch (response.resultCode) {
        case 'Authorised':
          dispatch(closeShoppingCart({ paymentType: 'scheme', paymentRef: response.pspReference, status: OrderStatus.PAID }));
          urlPart = 'success';
          break;
        case 'Pending':
          dispatch(closeShoppingCart({ paymentType: 'scheme', paymentRef: response.pspReference, status: OrderStatus.PENDING }));
          urlPart = 'pending';
          break;
        case 'Refused':
          urlPart = 'failed';
          break;
        default:
          urlPart = 'error';
          break;
      }
      window.location.href = `/checkout/status/${urlPart}?reason=${response.resultCode}`;
    }
  };

  const onSubmit = (state, component) => {
    if (state.isValid) {
      dispatch(
        initiatePayment({
          ...state.data,
          origin: window.location.origin,
        })
      );
      setPaymentComponent(component);
    }
  };

  const onAdditionalDetails = (state, component) => {
    dispatch(submitAdditionalDetails(state.data));
    setPaymentComponent(component);
  };

  const handlePaymentSelect = (type: string) => () => {
    checkout.create(type).mount(paymentContainer?.current);
  };

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
              <Button onClick={handlePaymentSelect('card')}>Credit Card</Button>
              <Button onClick={handlePaymentSelect('ideal')}>iDEAL</Button>
            </ButtonGroup>
          </Col>
          <Col md="8">
            <div ref={paymentContainer} className="payment" />
          </Col>
        </Row>
      </Col>
    </Row>
  );
};

export default CheckoutContainer;
