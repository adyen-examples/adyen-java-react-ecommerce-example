import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { getEntities as getShoppingCarts } from 'app/entities/shopping-cart/shopping-cart.reducer';
import { getEntity, updateEntity, createEntity, reset } from './product-order.reducer';

export interface IProductOrderUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductOrderUpdate = (props: IProductOrderUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { productOrderEntity, products, shoppingCarts, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/product-order');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getProducts();
    props.getShoppingCarts();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...productOrderEntity,
        ...values,
        product: products.find(it => it.id.toString() === values.productId.toString()),
        cart: shoppingCarts.find(it => it.id.toString() === values.cartId.toString()),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="storeApp.productOrder.home.createOrEditLabel" data-cy="ProductOrderCreateUpdateHeading">
            Create or edit a ProductOrder
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : productOrderEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="product-order-id">ID</Label>
                  <AvInput id="product-order-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="quantityLabel" for="product-order-quantity">
                  Quantity
                </Label>
                <AvField
                  id="product-order-quantity"
                  data-cy="quantity"
                  type="string"
                  className="form-control"
                  name="quantity"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    min: { value: 0, errorMessage: 'This field should be at least 0.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="totalPriceLabel" for="product-order-totalPrice">
                  Total Price
                </Label>
                <AvField
                  id="product-order-totalPrice"
                  data-cy="totalPrice"
                  type="text"
                  name="totalPrice"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    min: { value: 0, errorMessage: 'This field should be at least 0.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="product-order-product">Product</Label>
                <AvInput
                  id="product-order-product"
                  type="select"
                  data-cy="product"
                  className="form-control"
                  name="productId"
                  value={isNew ? products[0] && products[0].id : productOrderEntity?.product?.id}
                  required
                >
                  <option value="" key="0" />
                  {products
                    ? products.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>This field is required.</AvFeedback>
              </AvGroup>
              <AvGroup>
                <Label for="product-order-cart">Cart</Label>
                <AvInput
                  id="product-order-cart"
                  type="select"
                  data-cy="cart"
                  className="form-control"
                  name="cartId"
                  value={isNew ? shoppingCarts[0] && shoppingCarts[0].id : productOrderEntity?.cart?.id}
                  required
                >
                  <option value="" key="0" />
                  {shoppingCarts
                    ? shoppingCarts.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>This field is required.</AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/product-order" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  products: storeState.product.entities,
  shoppingCarts: storeState.shoppingCart.entities,
  productOrderEntity: storeState.productOrder.entity,
  loading: storeState.productOrder.loading,
  updating: storeState.productOrder.updating,
  updateSuccess: storeState.productOrder.updateSuccess,
});

const mapDispatchToProps = {
  getProducts,
  getShoppingCarts,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductOrderUpdate);
