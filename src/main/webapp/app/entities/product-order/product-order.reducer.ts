import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductOrder, defaultValue } from 'app/shared/model/product-order.model';

export const ACTION_TYPES = {
  FETCH_PRODUCTORDER_LIST: 'productOrder/FETCH_PRODUCTORDER_LIST',
  FETCH_PRODUCTORDER: 'productOrder/FETCH_PRODUCTORDER',
  CREATE_PRODUCTORDER: 'productOrder/CREATE_PRODUCTORDER',
  UPDATE_PRODUCTORDER: 'productOrder/UPDATE_PRODUCTORDER',
  DELETE_PRODUCTORDER: 'productOrder/DELETE_PRODUCTORDER',
  RESET: 'productOrder/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProductOrder>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type ProductOrderState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductOrderState = initialState, action): ProductOrderState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTORDER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTORDER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCTORDER):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCTORDER):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCTORDER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTORDER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTORDER):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCTORDER):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCTORDER):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCTORDER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTORDER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTORDER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCTORDER):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCTORDER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCTORDER):
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

const apiUrl = 'api/product-orders';

// Actions

export const getEntities: ICrudGetAllAction<IProductOrder> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PRODUCTORDER_LIST,
  payload: axios.get<IProductOrder>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IProductOrder> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTORDER,
    payload: axios.get<IProductOrder>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IProductOrder> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCTORDER,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProductOrder> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCTORDER,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProductOrder> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCTORDER,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
