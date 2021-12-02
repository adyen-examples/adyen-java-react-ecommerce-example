import axios from 'axios';
import { IShoppingCart } from 'app/shared/model/shopping-cart.model';
import { AnyAction, createAsyncThunk, createSlice, isPending, isRejected } from '@reduxjs/toolkit';
import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';

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

const apiUrl = 'api/checkout';

// Actions

export const getAdyenConfig = createAsyncThunk(
  'checkout/fetch_config',
  async () => {
    const requestUrl = `${apiUrl}/config`;
    return axios.get<object>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const getPaymentMethods = createAsyncThunk(
  'checkout/fetch_payment_methods',
  async () => {
    const requestUrl = `${apiUrl}/payment-methods`;
    return axios.post<unknown>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const initiatePayment = createAsyncThunk(
  'checkout/submit_payment',
  async (data: unknown) => {
    const requestUrl = `${apiUrl}/initiate-payment`;
    return axios.post<unknown>(requestUrl, data);
  },
  { serializeError: serializeAxiosError }
);

export const submitAdditionalDetails = createAsyncThunk(
  'checkout/submit_payment_details',
  async (data: unknown) => {
    const requestUrl = `${apiUrl}/submit-additional-details`;
    return axios.post<unknown>(requestUrl, data);
  },
  { serializeError: serializeAxiosError }
);

export const refundPayment = createAsyncThunk(
  'checkout/refund_payment',
  async ({ cart, action }: { cart: IShoppingCart; action: () => void }, thunkAPI) => {
    const requestUrl = `${apiUrl}/refund-payment`;
    await axios.post(requestUrl, cart);
    action();
  },
  { serializeError: serializeAxiosError }
);

// slice

export const CheckoutSlice = createSlice({
  name: 'checkout',
  initialState,
  reducers: {},
  extraReducers(builder) {
    builder
      .addCase(submitAdditionalDetails.fulfilled, (state, action) => {
        state.loading = false;
        state.paymentDetailsRes = action.payload.data;
      })
      .addCase(initiatePayment.fulfilled, (state, action) => {
        state.loading = false;
        state.paymentRes = action.payload.data;
      })
      .addCase(getPaymentMethods.fulfilled, (state, action) => {
        state.loading = false;
        state.paymentMethodsRes = action.payload.data;
      })
      .addCase(getAdyenConfig.fulfilled, (state, action) => {
        state.loading = false;
        state.config = { ...state.config, ...action.payload.data };
      })
      .addMatcher(isRejected(submitAdditionalDetails, initiatePayment, getPaymentMethods, getAdyenConfig), (state, action) => {
        state.loading = false;
        state.errorMessage = action.payload;
      })
      .addMatcher(isPending(submitAdditionalDetails, initiatePayment, getPaymentMethods, getAdyenConfig), state => {
        state.loading = true;
        state.errorMessage = null;
      });
  },
});

export default CheckoutSlice.reducer;
