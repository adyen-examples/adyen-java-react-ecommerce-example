import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { createEntitySlice, EntityState, IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { defaultValue, IShoppingCart } from 'app/shared/model/shopping-cart.model';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';
import { IProduct } from 'app/shared/model/product.model';

const initialState: EntityState<IShoppingCart> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const apiUrl = 'api/shopping-carts';

// Actions

export const getEntities = createAsyncThunk(
  'shoppingCart/fetch_entity_list',
  async ({ page, size, sort }: IQueryParams) => {
    const requestUrl = `${apiUrl}?cacheBuster=${new Date().getTime()}`;
    return axios.get<IShoppingCart[]>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const getEntity = createAsyncThunk(
  'shoppingCart/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IShoppingCart>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const getActiveCartForCurrentUser = createAsyncThunk(
  'shoppingCart/get_active_cart_for_current_user',
  async () => {
    const requestUrl = `${apiUrl}/current-user-active`;
    return axios.get<IShoppingCart>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const getCartsForCurrentUser = createAsyncThunk(
  'shoppingCart/get_carts_for_current_user',
  async () => {
    const requestUrl = `${apiUrl}/current-user`;
    return axios.get<IShoppingCart[]>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'shoppingCart/create_entity',
  async (entity: IShoppingCart, thunkAPI) => {
    const result = await axios.post<IShoppingCart>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'shoppingCart/update_entity',
  async (entity: IShoppingCart, thunkAPI) => {
    const result = await axios.put<IShoppingCart>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'shoppingCart/partial_update_entity',
  async (entity: IShoppingCart, thunkAPI) => {
    const result = await axios.patch<IShoppingCart>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'shoppingCart/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IShoppingCart>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const addProduct = createAsyncThunk('shoppingCard/add_product', async (entity: IProduct, thunkAPI) => {
  const requestUrl = `${apiUrl}/add-product/${entity?.id}`;
  return axios.put<IShoppingCart>(requestUrl);
});

export const removeOrder = createAsyncThunk(
  'shoppingCard/remove_order',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/remove-order/${id}`;
    const result = await axios.delete(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const closeShoppingCart = createAsyncThunk(
  'shoppingCart/close_shopping_cart',
  async ({ paymentType, paymentRef, status }: { paymentType: string; paymentRef: string; status: OrderStatus }, thunkAPI) => {
    const requestUrl = `${apiUrl}/close?paymentType=${paymentType}&paymentRef=${paymentRef}&status=${status}`;
    const result = await axios.put(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const ShoppingCartSlice = createEntitySlice({
  name: 'shoppingCart',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addMatcher(isFulfilled(deleteEntity, removeOrder), state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities, getCartsForCurrentUser), (state, action) => {
        return {
          ...state,
          loading: false,
          entities: action.payload.data,
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity, addProduct), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity, removeOrder), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = ShoppingCartSlice.actions;

// Reducer
export default ShoppingCartSlice.reducer;
