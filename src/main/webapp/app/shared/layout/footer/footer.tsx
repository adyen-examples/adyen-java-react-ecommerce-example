import './footer.scss';

import React from 'react';

import { Col, Row } from 'reactstrap';

const Footer = props => (
  <div className="footer page-content">
    <Row>
      <Col md="12" className="pt-2">
        <p>
          You can find the source code of this application on{' '}
          <a href="https://github.com/adyen-examples/adyen-java-react-ecommerce-example" target="_blank" rel="noopener noreferrer">
            Github
          </a>
        </p>
      </Col>
    </Row>
  </div>
);

export default Footer;
