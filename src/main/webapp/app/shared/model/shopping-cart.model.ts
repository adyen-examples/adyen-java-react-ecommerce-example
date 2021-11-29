import dayjs from 'dayjs';
import { IProductOrder } from 'app/shared/model/product-order.model';
import { ICustomerDetails } from 'app/shared/model/customer-details.model';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';
import { PaymentMethod } from 'app/shared/model/enumerations/payment-method.model';

export interface IShoppingCart {
  id?: number;
  placedDate?: string;
  status?: OrderStatus;
  totalPrice?: number;
  paymentMethod?: PaymentMethod;
  paymentReference?: string | null;
  paymentModificationReference?: string | null;
  orders?: IProductOrder[] | null;
  customerDetails?: ICustomerDetails;
}

export const defaultValue: Readonly<IShoppingCart> = {};
