import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction, ICrudSearchAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IShoppingCart, defaultValue } from 'app/shared/model/shopping-cart.model';
import { IProduct } from 'app/shared/model/product.model';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';

export const ACTION_TYPES = {
  FETCH_SHOPPINGCART_LIST: 'shoppingCart/FETCH_SHOPPINGCART_LIST',
  FETCH_SHOPPINGCART: 'shoppingCart/FETCH_SHOPPINGCART',
  CREATE_SHOPPINGCART: 'shoppingCart/CREATE_SHOPPINGCART',
  UPDATE_SHOPPINGCART: 'shoppingCart/UPDATE_SHOPPINGCART',
  DELETE_SHOPPINGCART: 'shoppingCart/DELETE_SHOPPINGCART',
  ADD_PRODUCT: 'shoppingCart/ADD_PRODUCT',
  REMOVE_PRODUCT: 'shoppingCart/REMOVE_PRODUCT',
  RESET: 'shoppingCart/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IShoppingCart>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type ShoppingCartState = Readonly<typeof initialState>;

// Reducer

export default (state: ShoppingCartState = initialState, action): ShoppingCartState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SHOPPINGCART_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SHOPPINGCART):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SHOPPINGCART):
    case REQUEST(ACTION_TYPES.UPDATE_SHOPPINGCART):
    case REQUEST(ACTION_TYPES.DELETE_SHOPPINGCART):
    case REQUEST(ACTION_TYPES.ADD_PRODUCT):
    case REQUEST(ACTION_TYPES.REMOVE_PRODUCT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_SHOPPINGCART_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SHOPPINGCART):
    case FAILURE(ACTION_TYPES.CREATE_SHOPPINGCART):
    case FAILURE(ACTION_TYPES.UPDATE_SHOPPINGCART):
    case FAILURE(ACTION_TYPES.DELETE_SHOPPINGCART):
    case FAILURE(ACTION_TYPES.ADD_PRODUCT):
    case FAILURE(ACTION_TYPES.REMOVE_PRODUCT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPPINGCART_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPPINGCART):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHOPPINGCART):
    case SUCCESS(ACTION_TYPES.UPDATE_SHOPPINGCART):
    case SUCCESS(ACTION_TYPES.ADD_PRODUCT):
    case SUCCESS(ACTION_TYPES.REMOVE_PRODUCT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SHOPPINGCART):
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

const apiUrl = 'api/shopping-carts';

// Actions

export const getEntities: ICrudGetAllAction<IShoppingCart> = () => ({
  type: ACTION_TYPES.FETCH_SHOPPINGCART_LIST,
  payload: axios.get<IShoppingCart>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IShoppingCart> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHOPPINGCART,
    payload: axios.get<IShoppingCart>(requestUrl),
  };
};

export const getActiveCartForCurrentUser: ICrudSearchAction<IShoppingCart> = () => {
  const requestUrl = `${apiUrl}/current-user-active`;
  return {
    type: ACTION_TYPES.FETCH_SHOPPINGCART,
    payload: axios.get<IShoppingCart>(requestUrl)
  };
};

export const getCartsForCurrentUser: ICrudSearchAction<IShoppingCart> = () => {
  const requestUrl = `${apiUrl}/current-user`;
  return {
    type: ACTION_TYPES.FETCH_SHOPPINGCART_LIST,
    payload: axios.get<IShoppingCart>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IShoppingCart> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SHOPPINGCART,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IShoppingCart> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHOPPINGCART,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IShoppingCart> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SHOPPINGCART,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const addProduct: ICrudPutAction<IProduct> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.ADD_PRODUCT,
    payload: axios.put(`${apiUrl}/add-product/${entity?.id}`)
  });
  return result;
};

export const removeOrder: ICrudDeleteAction<IShoppingCart> = id => async dispatch => {
  const requestUrl = `${apiUrl}/remove-order/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.REMOVE_PRODUCT,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const closeShoppingCart = (paymentType: string, paymentRef: string, status: OrderStatus) => async dispatch => {
  return await dispatch({
    type: ACTION_TYPES.UPDATE_SHOPPINGCART,
    payload: axios.put(`${apiUrl}/close?paymentType=${paymentType}&paymentRef=${paymentRef}&status=${status}`)
  });
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
