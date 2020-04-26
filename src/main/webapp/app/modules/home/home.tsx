import './home.scss';

import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';
import { Row, Col, Alert, Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getSortState, JhiItemCount, JhiPagination, TextFormat } from 'react-jhipster';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from 'app/entities/product/product.reducer';
import { addProduct } from 'app/entities/shopping-cart/shopping-cart.reducer';
import { IProductProps } from 'app/entities/product/product';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { IProduct } from 'app/shared/model/product.model';

export interface IHomeProp extends IProductProps, StateProps, DispatchProps {}

export const Home = (props: IHomeProp) => {
  const [paginationState, setPaginationState] = useState(getSortState(props.location, ITEMS_PER_PAGE));
  const [filterState, setFilterState] = useState('');

  const getAllEntities = () => {
    props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`);
  };

  const sortEntities = () => {
    getAllEntities();
    props.history.push(
      `${props.location.pathname}?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`
    );
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === 'asc' ? 'desc' : 'asc',
      sort: p
    });
  };

  const filter = (p: IProduct) => p.name && (p.name?.toLowerCase() + p.description?.toLowerCase()).includes(filterState.toLowerCase());

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage
    });

  const handleFilter = evt => setFilterState(evt.target.value);

  const addToCart = (p: IProduct) => () => props.addProduct(p);

  const { account, productList, match, loading, totalItems } = props;

  return (
    <Row className="d-flex justify-content-center">
      <Col lg="9" md="12">
        <h2>Welcome to Adyen Demo Store!</h2>
        <p className="lead">This is an e-commerce application showcasing the Adyen Payment Platform</p>
        {account && account.login ? (
          <>
            <div>
              <Alert color="success">You are logged in with username {account.login}.</Alert>
            </div>
            {productList && productList.length > 0 ? (
              <>
                <div className="mb-2 d-flex justify-content-end align-items-center">
                  <span className="mr-2 col-2">Filter by name</span>
                  <input type="search" className="form-control" value={filterState} onChange={handleFilter} />
                  <span className="mx-2 col-1">Sort by</span>
                  <div className="btn-group sort-btns" role="group">
                    <button type="button" className="btn btn-light" onClick={sort('name')}>
                      <span className="d-flex">
                        <span>Name </span>&nbsp;
                        <FontAwesomeIcon icon="sort" />
                      </span>
                    </button>
                    <button type="button" className="btn btn-light" onClick={sort('price')}>
                      <span className="d-flex">
                        <span>Price</span>&nbsp;
                        <FontAwesomeIcon icon="sort" />
                      </span>
                    </button>
                    <button type="button" className="btn btn-light" onClick={sort('size')}>
                      <span className="d-flex">
                        <span>Size</span>&nbsp;
                        <FontAwesomeIcon icon="sort" />
                      </span>
                    </button>
                    <button type="button" className="btn btn-light" onClick={sort('productCategory.id')}>
                      <span className="d-flex">
                        <span>Category</span>&nbsp;
                        <FontAwesomeIcon icon="sort" />
                      </span>
                    </button>
                  </div>
                </div>
                <div className="list-group">
                  {productList.filter(filter).map((product, i) => (
                    <a key={`entity-${i}`} className="list-group-item list-group-item-action flex-column align-items-start">
                      <div className="row">
                        <div className="col-2 col-xs-12 justify-content-center">
                          {product.image ? (
                            <img src={`data:${product.imageContentType};base64,${product.image}`} style={{ maxHeight: '130px' }} />
                          ) : null}
                        </div>
                        <div className="col col-xs-12">
                          <div className="d-flex w-100 justify-content-between">
                            <Button tag={Link} to={`/product/${product.id}`} color="link" size="sm" className="px-0">
                              {product.name}
                            </Button>
                            {product.productCategory && <small>Category: {product.productCategory.name}</small>}
                          </div>
                          <div>
                            <small className="mb-1">{product.description}</small>{' '}
                          </div>
                          <p>
                            <small>
                              Size: <span>{product.size}</span>
                            </small>
                          </p>
                          <div className="d-flex w-100 justify-content-between">
                            <p className="mb-1">€ {product.price}</p>
                            <div>
                              <Button onClick={addToCart(product)} color="secondary" size="sm">
                                <FontAwesomeIcon icon="cart-plus" /> <span className="d-none d-md-inline">Add to Cart</span>
                              </Button>
                            </div>
                          </div>
                        </div>
                      </div>
                    </a>
                  ))}
                </div>
                <div className={productList && productList.length > 0 ? 'p-4' : 'd-none'}>
                  <Row>
                    <Col className="d-flex flex-column align-items-start justify-content-center">
                      <Row className="justify-content-start">
                        <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
                      </Row>
                      <Row className="justify-content-start">
                        <JhiPagination
                          activePage={paginationState.activePage}
                          onSelect={handlePagination}
                          maxButtons={5}
                          itemsPerPage={paginationState.itemsPerPage}
                          totalItems={props.totalItems}
                        />
                      </Row>
                    </Col>
                    <Col className="d-flex align-items-center justify-content-end">
                      <Row className="justify-content-end">
                        <Button tag={Link} to={`/cart`} color="primary" size="lg">
                          <FontAwesomeIcon icon="shopping-cart" /> <span className="d-none d-md-inline">View Cart</span>
                        </Button>
                      </Row>
                    </Col>
                  </Row>
                </div>
              </>
            ) : (
              !loading && <div className="alert alert-warning">No Products found</div>
            )}
          </>
        ) : (
          <div>
            <Alert color="warning">
              If you want to
              <Link to="/login" className="alert-link">
                {' '}
                sign in
              </Link>
              , you can try the default accounts:
              <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
              <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).
            </Alert>

            <Alert color="warning">
              You do not have an account yet?&nbsp;
              <Link to="/account/register" className="alert-link">
                Register a new account
              </Link>
            </Alert>
          </div>
        )}
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ product, authentication }: IRootState) => ({
  account: authentication.account,
  isAuthenticated: authentication.isAuthenticated,
  productList: product.entities,
  loading: product.loading,
  totalItems: product.totalItems
});

const mapDispatchToProps = {
  getEntities,
  addProduct
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Home);
