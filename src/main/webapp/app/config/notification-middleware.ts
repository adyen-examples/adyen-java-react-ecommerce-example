import { isPromise } from 'react-jhipster';
import { toast } from 'react-toastify';

const addErrorAlert = (message, key?, data?) => {
  toast.error(message);
};
export default () => next => action => {
  // If not a promise, continue on
  if (!isPromise(action.payload)) {
    return next(action);
  }

  /**
   *
   * The notification middleware serves to dispatch the initial pending promise to
   * the promise middleware, but adds a `then` and `catch.
   */
  return next(action)
    .then(response => {
      if (action.meta && action.meta.successMessage) {
        toast.success(action.meta.successMessage);
      } else if (response && response.action && response.action.payload && response.action.payload.headers) {
        const headers = response.action.payload.headers;
        let alert: string | null = null;
        Object.entries<string>(headers).forEach(([k, v]) => {
          if (k.toLowerCase().endsWith('app-alert')) {
            alert = v;
          }
        });
        if (alert) {
          toast.success(alert);
        }
      }
      return Promise.resolve(response);
    })
    .catch(error => {
      if (action.meta && action.meta.errorMessage) {
        toast.error(action.meta.errorMessage);
      } else if (error && error.response) {
        const response = error.response;
        const data = response.data;
        if (!(response.status === 401 && (error.message === '' || (data && data.path && data.path.includes('/api/account'))))) {
          let i;
          switch (response.status) {
            // connection refused, server not reachable
            case 0:
              addErrorAlert('Server not reachable', 'error.server.not.reachable');
              break;

            case 400: {
              const headers = Object.entries<string>(response.headers);
              let errorHeader: string | null = null;
              let entityKey: string | null = null;
              headers.forEach(([k, v]: [string, string]) => {
                if (k.toLowerCase().endsWith('app-error')) {
                  errorHeader = v;
                } else if (k.toLowerCase().endsWith('app-params')) {
                  entityKey = v;
                }
              });
              if (errorHeader) {
                const entityName = entityKey;
                addErrorAlert(errorHeader, errorHeader, { entityName });
              } else if (data !== '' && data.fieldErrors) {
                const fieldErrors = data.fieldErrors;
                for (i = 0; i < fieldErrors.length; i++) {
                  const fieldError = fieldErrors[i];
                  if (['Min', 'Max', 'DecimalMin', 'DecimalMax'].includes(fieldError.message)) {
                    fieldError.message = 'Size';
                  }
                  // convert 'something[14].other[4].id' to 'something[].other[].id' so translations can be written to it
                  const convertedField = fieldError.field.replace(/\[\d*\]/g, '[]');
                  const fieldName = convertedField.charAt(0).toUpperCase() + convertedField.slice(1);
                  addErrorAlert(`Error on field "${fieldName}"`, `error.${fieldError.message}`, { fieldName });
                }
              } else if (data !== '' && data.message) {
                addErrorAlert(data.message, data.message, data.params);
              } else {
                addErrorAlert(data);
              }
              break;
            }
            case 404:
              addErrorAlert('Not found', 'error.url.not.found');
              break;

            default:
              if (data !== '' && data.message) {
                addErrorAlert(data.message);
              } else {
                addErrorAlert(data);
              }
          }
        }
      } else if (error && error.config && error.config.url === 'api/account' && error.config.method === 'get') {
        /* eslint-disable no-console */
        console.log('Authentication Error: Trying to access url api/account with GET.');
      } else if (error && error.message) {
        toast.error(error.message);
      } else {
        toast.error('Unknown error!');
      }
      return Promise.reject(error);
    });
};
