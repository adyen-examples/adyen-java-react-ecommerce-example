import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductCategory, defaultValue } from 'app/shared/model/product-category.model';

export const ACTION_TYPES = {
  FETCH_PRODUCTCATEGORY_LIST: 'productCategory/FETCH_PRODUCTCATEGORY_LIST',
  FETCH_PRODUCTCATEGORY: 'productCategory/FETCH_PRODUCTCATEGORY',
  CREATE_PRODUCTCATEGORY: 'productCategory/CREATE_PRODUCTCATEGORY',
  UPDATE_PRODUCTCATEGORY: 'productCategory/UPDATE_PRODUCTCATEGORY',
  DELETE_PRODUCTCATEGORY: 'productCategory/DELETE_PRODUCTCATEGORY',
  RESET: 'productCategory/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProductCategory>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ProductCategoryState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductCategoryState = initialState, action): ProductCategoryState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTCATEGORY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTCATEGORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCTCATEGORY):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCTCATEGORY):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCTCATEGORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTCATEGORY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTCATEGORY):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCTCATEGORY):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCTCATEGORY):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCTCATEGORY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTCATEGORY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTCATEGORY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCTCATEGORY):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCTCATEGORY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCTCATEGORY):
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

const apiUrl = 'api/product-categories';

// Actions

export const getEntities: ICrudGetAllAction<IProductCategory> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTCATEGORY_LIST,
    payload: axios.get<IProductCategory>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IProductCategory> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTCATEGORY,
    payload: axios.get<IProductCategory>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IProductCategory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCTCATEGORY,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProductCategory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCTCATEGORY,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProductCategory> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCTCATEGORY,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
