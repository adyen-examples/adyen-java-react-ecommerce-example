import axios from 'axios';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IShoppingCart } from 'app/shared/model/shopping-cart.model';
import { IPayload, IPayloadResult } from 'react-jhipster';

export const ACTION_TYPES = {
  FETCH_CONFIG: 'checkout/FETCH_CONFIG',
  FETCH_PAYMENTMETHODS: 'checkout/FETCH_PAYMENTMETHODS',
  SUBMIT_PAYMENT: 'checkout/SUBMIT_PAYMENT',
  REFUND_PAYMENT: 'checkout/REFUND_PAYMENT',
  SUBMIT_PAYMENTDETAILS: 'checkout/SUBMIT_PAYMENTDETAILS',
};

const initialState = {
  loading: false,
  errorMessage: null,
  paymentMethodsRes: null,
  paymentRes: null,
  paymentDetailsRes: null,
  config: {
    paymentMethodsConfiguration: {
      ideal: {
        showImage: true,
      },
      card: {
        hasHolderName: true,
        holderNameRequired: true,
        name: 'Credit or debit card',
      },
    },
    locale: 'en-US',
    showPayButton: true,
  },
};

export type CheckoutState = Readonly<typeof initialState>;

// Reducer

export default (state: CheckoutState = initialState, action): CheckoutState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CONFIG):
    case REQUEST(ACTION_TYPES.FETCH_PAYMENTMETHODS):
    case REQUEST(ACTION_TYPES.SUBMIT_PAYMENT):
    case REQUEST(ACTION_TYPES.SUBMIT_PAYMENTDETAILS):
      return {
        ...state,
        errorMessage: null,
        loading: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_CONFIG):
    case FAILURE(ACTION_TYPES.FETCH_PAYMENTMETHODS):
    case FAILURE(ACTION_TYPES.SUBMIT_PAYMENT):
    case FAILURE(ACTION_TYPES.SUBMIT_PAYMENTDETAILS):
      return {
        ...state,
        loading: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONFIG):
      return {
        ...state,
        loading: false,
        config: {
          ...state.config,
          ...action.payload.data,
        },
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAYMENTMETHODS):
      return {
        ...state,
        loading: false,
        paymentMethodsRes: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.SUBMIT_PAYMENT):
      return {
        ...state,
        loading: false,
        paymentRes: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.SUBMIT_PAYMENTDETAILS):
      return {
        ...state,
        loading: false,
        paymentDetailsRes: action.payload.data,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/checkout';

// Actions

export const getAdyenConfig = () => {
  const requestUrl = `${apiUrl}/config`;
  return {
    type: ACTION_TYPES.FETCH_CONFIG,
    payload: axios.get(requestUrl),
  };
};

export const getPaymentMethods = () => {
  const requestUrl = `${apiUrl}/payment-methods`;
  return {
    type: ACTION_TYPES.FETCH_PAYMENTMETHODS,
    payload: axios.post(requestUrl),
  };
};

export const initiatePayment = data => {
  const requestUrl = `${apiUrl}/initiate-payment`;
  return {
    type: ACTION_TYPES.SUBMIT_PAYMENT,
    payload: axios.post(requestUrl, data),
  };
};

export const submitAdditionalDetails = data => {
  const requestUrl = `${apiUrl}/submit-additional-details`;
  return {
    type: ACTION_TYPES.SUBMIT_PAYMENTDETAILS,
    payload: axios.post(requestUrl, data),
  };
};

export const refundPayment: (cart: IShoppingCart, action: () => void) => IPayload<string> | IPayloadResult<string> = (
  cart: IShoppingCart,
  action: () => void
) => async dispatch => {
  const requestUrl = `${apiUrl}/refund-payment`;
  await dispatch({
    type: ACTION_TYPES.REFUND_PAYMENT,
    payload: axios.post(requestUrl, cart),
  });
  return dispatch(action());
};
