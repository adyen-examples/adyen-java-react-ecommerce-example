import { IProduct } from 'app/shared/model/product.model';

export interface IProductCategory {
  id?: number;
  name?: string;
  description?: string | null;
  products?: IProduct[] | null;
}

export const defaultValue: Readonly<IProductCategory> = {};
