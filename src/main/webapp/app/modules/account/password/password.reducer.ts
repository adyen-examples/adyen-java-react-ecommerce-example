import axios from 'axios';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

export const ACTION_TYPES = {
  UPDATE_PASSWORD: 'account/UPDATE_PASSWORD',
  RESET: 'account/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  updateSuccess: false,
  updateFailure: false,
};

export type PasswordState = Readonly<typeof initialState>;

// Reducer
export default (state: PasswordState = initialState, action): PasswordState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.UPDATE_PASSWORD):
      return {
        ...initialState,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case FAILURE(ACTION_TYPES.UPDATE_PASSWORD):
      return {
        ...initialState,
        loading: false,
        updateSuccess: false,
        updateFailure: true,
      };
    case SUCCESS(ACTION_TYPES.UPDATE_PASSWORD):
      return {
        ...initialState,
        loading: false,
        updateSuccess: true,
        updateFailure: false,
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

// Actions
const apiUrl = 'api/account';

export const savePassword = (currentPassword, newPassword) => ({
  type: ACTION_TYPES.UPDATE_PASSWORD,
  payload: axios.post(`${apiUrl}/change-password`, { currentPassword, newPassword }),
  meta: {
    successMessage: '<strong>Password changed!</strong>',
    errorMessage: '<strong>An error has occurred!</strong> The password could not be changed.',
  },
});

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
