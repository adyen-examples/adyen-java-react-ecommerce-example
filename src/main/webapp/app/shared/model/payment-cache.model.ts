import { IUser } from 'app/shared/model/user.model';

export interface IPaymentCache {
  id?: number;
  orderRef?: string;
  originalHost?: string;
  paymentData?: string;
  paymentType?: string;
  user?: IUser;
}

export const defaultValue: Readonly<IPaymentCache> = {};
