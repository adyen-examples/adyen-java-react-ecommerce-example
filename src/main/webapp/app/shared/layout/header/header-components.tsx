import React from 'react';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/logo.svg" alt="Logo" />
  </div>
);

export const Brand = () => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <BrandIcon />
    <span className="brand-title">Demo Store</span>
    <span className="navbar-version">{VERSION}</span>
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>Home</span>
    </NavLink>
  </NavItem>
);

export const Cart = props => (
  <NavItem>
    <NavLink tag={Link} to="/cart" className="d-flex align-items-center">
      <FontAwesomeIcon icon="shopping-cart" />
      <span>My Cart</span>
    </NavLink>
  </NavItem>
);

export const Orders = props => (
  <NavItem>
    <NavLink tag={Link} to="/orders" className="d-flex align-items-center">
      <FontAwesomeIcon icon="list" />
      <span>My Orders</span>
    </NavLink>
  </NavItem>
);
