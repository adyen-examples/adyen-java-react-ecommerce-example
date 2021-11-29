import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICustomerDetails, defaultValue } from 'app/shared/model/customer-details.model';

export const ACTION_TYPES = {
  FETCH_CUSTOMERDETAILS_LIST: 'customerDetails/FETCH_CUSTOMERDETAILS_LIST',
  FETCH_CUSTOMERDETAILS: 'customerDetails/FETCH_CUSTOMERDETAILS',
  CREATE_CUSTOMERDETAILS: 'customerDetails/CREATE_CUSTOMERDETAILS',
  UPDATE_CUSTOMERDETAILS: 'customerDetails/UPDATE_CUSTOMERDETAILS',
  PARTIAL_UPDATE_CUSTOMERDETAILS: 'customerDetails/PARTIAL_UPDATE_CUSTOMERDETAILS',
  DELETE_CUSTOMERDETAILS: 'customerDetails/DELETE_CUSTOMERDETAILS',
  RESET: 'customerDetails/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICustomerDetails>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type CustomerDetailsState = Readonly<typeof initialState>;

// Reducer

export default (state: CustomerDetailsState = initialState, action): CustomerDetailsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERDETAILS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERDETAILS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_CUSTOMERDETAILS):
    case REQUEST(ACTION_TYPES.UPDATE_CUSTOMERDETAILS):
    case REQUEST(ACTION_TYPES.DELETE_CUSTOMERDETAILS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_CUSTOMERDETAILS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERDETAILS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERDETAILS):
    case FAILURE(ACTION_TYPES.CREATE_CUSTOMERDETAILS):
    case FAILURE(ACTION_TYPES.UPDATE_CUSTOMERDETAILS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_CUSTOMERDETAILS):
    case FAILURE(ACTION_TYPES.DELETE_CUSTOMERDETAILS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERDETAILS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERDETAILS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_CUSTOMERDETAILS):
    case SUCCESS(ACTION_TYPES.UPDATE_CUSTOMERDETAILS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_CUSTOMERDETAILS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_CUSTOMERDETAILS):
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

const apiUrl = 'api/customer-details';

// Actions

export const getEntities: ICrudGetAllAction<ICustomerDetails> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMERDETAILS_LIST,
    payload: axios.get<ICustomerDetails>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ICustomerDetails> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMERDETAILS,
    payload: axios.get<ICustomerDetails>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ICustomerDetails> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CUSTOMERDETAILS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICustomerDetails> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CUSTOMERDETAILS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ICustomerDetails> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_CUSTOMERDETAILS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICustomerDetails> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CUSTOMERDETAILS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
