import { IProductCategory } from 'app/shared/model/product-category.model';
import { Size } from 'app/shared/model/enumerations/size.model';

export interface IProduct {
  id?: number;
  name?: string;
  description?: string;
  price?: number;
  itemSize?: Size;
  imageContentType?: string;
  image?: any;
  productCategory?: IProductCategory;
}

export const defaultValue: Readonly<IProduct> = {};
