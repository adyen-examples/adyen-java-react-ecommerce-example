import React from 'react';

import { Alert } from 'reactstrap';

class PageNotFound extends React.Component {
  render() {
    return (
      <div>
        <Alert color="danger">The page does not exist.</Alert>
      </div>
    );
  }
}

export default PageNotFound;
