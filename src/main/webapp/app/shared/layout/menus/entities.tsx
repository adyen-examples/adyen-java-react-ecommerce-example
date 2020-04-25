import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Manage" id="entity-menu" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <MenuItem icon="asterisk" to="/product">
      Product
    </MenuItem>
    <MenuItem icon="asterisk" to="/product-category">
      Product Category
    </MenuItem>
    <MenuItem icon="asterisk" to="/customer-details">
      Customer Details
    </MenuItem>
    <MenuItem icon="asterisk" to="/shopping-cart">
      Shopping Cart
    </MenuItem>
    <MenuItem icon="asterisk" to="/product-order">
      Product Order
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
