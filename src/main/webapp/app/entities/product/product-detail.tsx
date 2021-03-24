import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product.reducer';
import { AUTHORITIES } from 'app/config/constants';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export interface IProductDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductDetail = (props: IProductDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { productEntity, isAdmin } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productDetailsHeading">Product</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{productEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{productEntity.name}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{productEntity.description}</dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{productEntity.price}</dd>
          <dt>
            <span id="itemSize">Item Size</span>
          </dt>
          <dd>{productEntity.itemSize}</dd>
          <dt>
            <span id="image">Image</span>
          </dt>
          <dd>
            {productEntity.image ? (
              <div>
                {productEntity.imageContentType ? (
                  <a onClick={openFile(productEntity.imageContentType, productEntity.image)}>
                    <img src={`data:${productEntity.imageContentType};base64,${productEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {productEntity.imageContentType}, {byteSize(productEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>Product Category</dt>
          <dd>{productEntity.productCategory ? productEntity.productCategory.name : ''}</dd>
        </dl>
        {isAdmin ? (
          <>
            <Button tag={Link} to="/product" replace color="info">
              <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
            </Button>
            &nbsp;
            <Button tag={Link} to={`/product/${productEntity.id}/edit`} replace color="primary">
              <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
            </Button>
          </>
        ) : (
          <Button tag={Link} to="/" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
        )}
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ product, authentication }: IRootState) => ({
  productEntity: product.entity,
  isAdmin: hasAnyAuthority(authentication.account.authorities, [AUTHORITIES.ADMIN])
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductDetail);
