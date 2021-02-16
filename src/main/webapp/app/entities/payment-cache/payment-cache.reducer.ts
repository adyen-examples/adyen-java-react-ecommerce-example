import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPaymentCache, defaultValue } from 'app/shared/model/payment-cache.model';

export const ACTION_TYPES = {
  FETCH_PAYMENTCACHE_LIST: 'paymentCache/FETCH_PAYMENTCACHE_LIST',
  FETCH_PAYMENTCACHE: 'paymentCache/FETCH_PAYMENTCACHE',
  CREATE_PAYMENTCACHE: 'paymentCache/CREATE_PAYMENTCACHE',
  UPDATE_PAYMENTCACHE: 'paymentCache/UPDATE_PAYMENTCACHE',
  DELETE_PAYMENTCACHE: 'paymentCache/DELETE_PAYMENTCACHE',
  RESET: 'paymentCache/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPaymentCache>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type PaymentCacheState = Readonly<typeof initialState>;

// Reducer

export default (state: PaymentCacheState = initialState, action): PaymentCacheState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PAYMENTCACHE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PAYMENTCACHE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PAYMENTCACHE):
    case REQUEST(ACTION_TYPES.UPDATE_PAYMENTCACHE):
    case REQUEST(ACTION_TYPES.DELETE_PAYMENTCACHE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PAYMENTCACHE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PAYMENTCACHE):
    case FAILURE(ACTION_TYPES.CREATE_PAYMENTCACHE):
    case FAILURE(ACTION_TYPES.UPDATE_PAYMENTCACHE):
    case FAILURE(ACTION_TYPES.DELETE_PAYMENTCACHE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAYMENTCACHE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAYMENTCACHE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PAYMENTCACHE):
    case SUCCESS(ACTION_TYPES.UPDATE_PAYMENTCACHE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PAYMENTCACHE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/payment-caches';

// Actions

export const getEntities: ICrudGetAllAction<IPaymentCache> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PAYMENTCACHE_LIST,
  payload: axios.get<IPaymentCache>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IPaymentCache> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PAYMENTCACHE,
    payload: axios.get<IPaymentCache>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IPaymentCache> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PAYMENTCACHE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPaymentCache> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PAYMENTCACHE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPaymentCache> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PAYMENTCACHE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
