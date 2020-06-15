---
title: Building an E-Commerce application using Java & React
description: 'How to build an e-commerce application with shopping cart and payments easily using Java, JHipster, Spring Boot, React, and Adyen payments'
published: false
featured: false
tags: [java, react, spring, jhipster]
series:
canonical_url:
cover_image: https://i.imgur.com/lS92nEc.jpg
---

E-commerce applications are the backbone of today's online shopping world. In this post, we will see how to build an e-commerce application easily using Java, JHipster, Spring Boot, and React. Since we will be scaffolding the application, the post will focus on how to build a shopping cart and payment integration rather than how to build a Java web application.

## Tools and technology we will use

We will use the below tools and technology to build this application:

- [**JHipster**](https://www.jhipster.tech/): JHipster is a rapid application development platform. It can quickly create web applications and microservices with production-grade code. Head over to the [installation instructions](https://www.jhipster.tech/installation/) to set it up. JHipster can scaffold applications with a wide variety of languages, frameworks, and configurations. For this tutorial, we will stick with the following major options. You don't have to install anything for this, as JHipster will manage these for you.
  - [**Spring Framework**](https://spring.io/): Spring is an application framework in Java that comes with all the bells and whistles required for enterprise-grade Java application development. It comes with Spring Boot which makes development faster and convenient. This lets us focus more on our business needs rather than spending time setting up technical integrations.
  - [**React**](https://reactjs.org/): A popular JavaScript UI library that helps build modern scalable front ends. We will be writing React code using TypeScript. We will also be using a few other components like Redux and React Router from the React ecosystem.
  - [**Bootstrap**](https://getbootstrap.com/): An UI framework for web applications with a variety of themes and customizations.
  - [**Gradle**](https://gradle.org/): Gradle is a Java build orchestration tool that provides a highly customizable and easy-to-use domain-specific language (DSL).
  - [**Webpack**](https://webpack.js.org/): A front-end build tool for modern web applications
- [**Adyen Payments Platform**](https://www.adyen.com/): Adyen is one of the leading payment platforms for medium to large scale businesses. It provides a plethora of payment options and provides SDKs for easy integrations. And I also happen to work for Adyen 😄
- [**Docker**](https://www.docker.com/): A containerization technology, which we will use it to quickly run our database. Make sure you have Docker and Docker compose installed. If you can run a local MySQL setup, you won't need Docker.
- [**Git**](https://git-scm.com/): Distributed version control system for source code management. Make sure you have Git installed.

### Prerequisite

To follow this tutorial effectively you would need to be familiar with at least the below tools and technology

- Java
- Spring Framework
- React
- Redux
- Bootstrap

> We have a [sample application](https://github.com/adyen-examples/adyen-java-react-ecommerce-example) built to accompany this post. Each section here is points to a particular [commit](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commits/master) in the sample app to help give you a better picture of what is being changed.

## Designing the entity model

Since we are going to scaffold our application, it is important to make sure that we have the correct entity model for the e-commerce application. We will use the [JHipster Domain Language(JDL)](https://www.jhipster.tech/jdl/) to do this. Below is the JDL model for an e-commerce application:

```groovy
/** Product sold by the Online store */
entity Product {
    name String required
    description String
    price BigDecimal required min(0)
    size Size required
    image ImageBlob
}

enum Size {
    S, M, L, XL, XXL
}

/** Product categories to group products */
entity ProductCategory {
    name String required
    description String
}

/** Additional details for users as we can't modify built-in user entity vis JDL */
entity CustomerDetails {
    gender Gender required
    phone String required
    addressLine1 String required
    addressLine2 String
    city String required
    country String required
}

enum Gender {
    MALE, FEMALE, OTHER
}

/** Shopping cart to hold users orders */
entity ShoppingCart {
    placedDate Instant required
    status OrderStatus required
    totalPrice BigDecimal required min(0)
    paymentMethod PaymentMethod required
}

enum OrderStatus {
    COMPLETED, PAID, PENDING, CANCELLED
}

enum PaymentMethod {
    CREDIT_CARD, IDEAL
}

/** Product order keeps track of orders */
entity ProductOrder {
    quantity Integer required min(0)
    totalPrice BigDecimal required min(0)
}

// Every user will have a customer detail
relationship OneToOne {
    CustomerDetails{user(login) required} to User
}

// Many product orders can be tracked back to a product
relationship ManyToOne {
    ProductOrder{product(name) required} to Product
}

relationship OneToMany {
    // Every customer can have many shopping carts
    CustomerDetails{cart} to ShoppingCart{customerDetails required},
    // Every shopping cart can have many product orders
    ShoppingCart{order} to ProductOrder{cart required},
    // Every product category can have many products
    ProductCategory{product} to Product{productCategory(name) required}
}

service * with serviceClass
paginate Product, CustomerDetails, ProductCategory with pagination
```

The `User` entity is built-in from JHipster and hence we don't have to define it in JDL. However, we can define their relationships. Here is a UML visualization of the same:

![Entity model for e-commerce](https://i.imgur.com/p403lEc.png)

Head over to [JDL Studio](https://start.jhipster.tech/jdl-studio/) if you want to visualize the model and make any changes.

Next, create a new folder and save the above to a file named `app.jdl` within that folder.

## Scaffolding the application

Now that we have our model in place, we can go ahead and scaffold a Java web application using JHipster. First, let's define our application. Add the below snippet to the file (`app.jdl`) we created earlier.

```groovy
application {
  config {
    baseName store
    packageName com.adyen.demo.store
    authenticationType jwt
    prodDatabaseType mysql
    buildTool gradle
    clientFramework react
    useSass true
    enableTranslation false
  }
  entities *
}
```

We just defined an application named **store** that uses JSON Web Token (JWT) as the authentication mechanism, MySQL as the production database, Gradle as the build tool and React as the client-side framework. You can see all the options supported by JHipster [here](https://www.jhipster.tech/jdl/applications). We also defined that the application uses all the entities we defined with `entities *`.

Now, let's invoke JHipster to scaffold the application. Run the below command inside the folder where we created `app.jdl`:

```shell
jhipster import-jdl app.jdl
```

This will create our application, install all necessary dependencies, and initialize & commit everything to Git. Make sure you have Git installed on your system.

Let's check out the application. Run the below command to run the application in development mode:

```shell
./gradlew
```

After running the application, visit [https://localhost:8080](https://localhost:8080) and use the default users mentioned on the home page to log in and explore the application. You can find the [commit](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/1833f6d96de51ed1838f159c404bee24533fcf4f) in the sample application.

You can also run the generated unit and integration tests with this command:

```shell
./gradlew npm_test test integrationTest
```

So far, the generated application doesn't have any specific business logic or custom screens. It is just a CRUD application for the model we defined. If you are familiar with Spring Framework and React you should be able to navigate the source code created easily. The Spring/React application created by JHipster is not the focus of this post, and for that I recommend you head over to documentation provided by JHipster, Spring, and React.

## Building a products landing page

Now that our application and all the CRUD APIs are ready, let us build a product landing page that lists all the products offered by the store.

We will convert `src/main/webapp/app/modules/home/home.tsx` to be our product landing page. This involves updating the JSX to show the products list and using the product redux reducer to fetch the data from product API. [Here](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/8bef6c1737a2fdc4feaedd0751f5dd0b5044f536#diff-718161e5814fcf204ef9e82fdc962d7d) is the complete diff for `home.tsx` and [here](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/8bef6c1737a2fdc4feaedd0751f5dd0b5044f536) is the entire changelog for this step.

![Product listing home page](https://i.imgur.com/kxNLwS8.png)

Start the application client-side in dev mode to speed up development. Keep the application running in a terminal using `./gradlew` if it not already running from the previous step. In a new terminal, run `npm start` and it will start a development server for the client-side, which proxies API calls to the backend and open up a new browser window pointing to [https://localhost:9000](https://localhost:9000).

At this point, the front-end and back-end are running in development mode with hot reload functionality. This means the entire application will automatically reload when we make any changes (the browser will reload as well). For backend changes, the reload will happen when you compile using your IDE or by running `./gradlew compileJava`.

Update `home.tsx` according to the [changelog](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/8bef6c1737a2fdc4feaedd0751f5dd0b5044f536) and see the changes reflected on the home page.

## Building the shopping cart

Now let us build a persistent shopping cart page, where we can list all the items added to the cart by the user. The user can also start checkout from this page. The shopping cart will hold the items added until the payment is complete even if the user logs out or uses the application in a different machine as the state is persisted automatically using the generated CRUD API:

![Shopping cart](https://i.imgur.com/vLcJ7Ly.png)

For this feature, we also add/update the below on the server-side:

- [security configurations](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/b23f455e8105e32a2154ca9ee9544231355cf57c#diff-ff9a3a62938424f5f25aa23632e0130b) to ensure that a user can update only his/her shopping cart when logged in. Only administrators will be able to see the shopping cart of other users and manage all entities.
- [New REST endpoints](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/b23f455e8105e32a2154ca9ee9544231355cf57c#diff-d193fb13f737cee08eebe3d91ad724ef) to add and remove products to and from a shopping cart.
- [Service methods](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/b23f455e8105e32a2154ca9ee9544231355cf57c#diff-5ba862ad08c22a3dbadff2c8aaf7ee31)
- [Database operations](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/b23f455e8105e32a2154ca9ee9544231355cf57c#diff-67890efa54cdc134785c6d61ae09dec5).

These updates are quite straightforward due to the framework provided by JHipster and Spring.

On the client-side, we will update:

- [shopping-cart reducer](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/b23f455e8105e32a2154ca9ee9544231355cf57c#diff-a3e50072c5d00cf79e3b620f3c5103c0) to talk to the new endpoints.
- Add a new route and [module](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/b23f455e8105e32a2154ca9ee9544231355cf57c#diff-ea8adcc088ab63d7cf36005231489d6e) to show the shopping cart.

The shopping cart React page uses the below snippet. Note that the listing content is quite similar to the product listing page.

```tsx
//import ...;

export interface ICartProp extends StateProps, DispatchProps {}

export const Cart = (props: ICartProp) => {
  useEffect(() => {
    props.getEntityForCurrentUser();
  }, []);

  const remove = id => () => {
    props.removeOrder(id);
  };

  const { isAuthenticated, cart, loading } = props;

  return (
    <Row className="d-flex justify-content-center">
      <Col lg="9" md="12">
        {isAuthenticated ? (
          <>
            <h2>Your shopping cart</h2>
            <p className="lead">You have {cart?.orders?.length} items in your cart</p>
            {cart.orders && cart.orders.length > 0 ? (
              <>
                <div className="list-group">
                  {cart.orders.map((order, i) => (
                    <a className="list-group-item list-group-item-action flex-column align-items-start">
                      <div className="row">{/*... list content */}</div>
                    </a>
                  ))}
                </div>
                <div className="d-flex justify-content-between py-4">
                  <h3>
                    Total price: <TextFormat value={cart.totalPrice as any} type="number" format={'$ 0,0.00'} />
                  </h3>
                  <Button tag={Link} to={`/checkout`} color="primary" size="lg">
                    <FontAwesomeIcon icon="cart-arrow-down" /> <span className="d-none d-md-inline">Checkout</span>
                  </Button>
                </div>
              </>
            ) : (
              !loading && <div className="alert alert-warning">No items found</div>
            )}
          </>
        ) : (
          <div>
            <Alert color="warning">Not authorized. Please log in first</Alert>
          </div>
        )}
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ authentication, shoppingCart }: IRootState) => ({
  isAuthenticated: authentication.isAuthenticated,
  cart: shoppingCart.entity,
  loading: shoppingCart.loading
});

const mapDispatchToProps = {
  getEntityForCurrentUser,
  removeOrder
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Cart);
```

Here is the entire [changelog](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/b23f455e8105e32a2154ca9ee9544231355cf57c) for this feature. Make the changes to the application and see the changes reflected on the shopping cart page.

Please note that I also made some improvements to the fake data generated by JHipster in [this commit](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/8dacb111116517b0cebfefb10c7a8680960dcea0) and made improvements to product and cart pages in [this commit](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/91f49fc6708b78862c9ea003e317aea7040e7335). I also fixed the tests in [this commit](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/0787f9b5a868799073a602b2f5245e82dda1f3cc). Update your application according to these changelogs as well.

## Payments integration

Now that our shopping cart is ready, we can integrate the [Adyen checkout API](https://docs.adyen.com/checkout) to make payments. First, make sure you [sign up](https://www.adyen.com/signup) for an Adyen test account. Follow [this guide](https://docs.adyen.com/checkout/get-started) to get your API keys and Merchant Account. You will also need to [generate an origin key](https://docs.adyen.com/user-management/how-to-get-an-origin-key) per domain you use to collect payments. In our case for development use, we need to create an origin key for `http://localhost:9000` and `http://localhost:8080`.

We will use the [Adyen Java API library](https://github.com/Adyen/adyen-java-api-library) to make API calls. We will add the dependency to our [Gradle build](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/30899562564be5f2b6dd1291af812c3f4ff8e22e#diff-c197962302397baf3a4cc36463dce5ea).

```groovy
implementation group: "com.adyen", name: "adyen-java-api-library", version: "5.0.0"
```

We also need to exclude the Adyen domain in the [content security policy](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/30899562564be5f2b6dd1291af812c3f4ff8e22e#diff-ff9a3a62938424f5f25aa23632e0130b) defined in `src/main/java/com/adyen/demo/store/config/SecurityConfiguration.java`.

We will create a new Spring REST controller that will use the Adyen Java library and make payment API calls for us. [Here](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/30899562564be5f2b6dd1291af812c3f4ff8e22e#diff-a8a69c0ec7bcbfd1141d8a56cfd4d9e3) is the `src/main/java/com/adyen/demo/store/web/rest/CheckoutResource.java` class. Here is a method from this class.

```java
@PostMapping("/checkout/payment-methods")
public ResponseEntity<PaymentMethodsResponse> paymentMethods() throws EntityNotFoundException, IOException, ApiException {
    PaymentMethodsRequest paymentMethodsRequest = new PaymentMethodsRequest();
    paymentMethodsRequest.setMerchantAccount(merchantAccount);
    paymentMethodsRequest.setCountryCode("NL");
    paymentMethodsRequest.setShopperLocale("nl-NL");
    paymentMethodsRequest.setChannel(PaymentMethodsRequest.ChannelEnum.WEB);

    Amount amount = getAmountFromCart();
    paymentMethodsRequest.setAmount(amount);
    log.debug("REST request to get Adyen payment methods {}", paymentMethodsRequest);
    PaymentMethodsResponse response = checkout.paymentMethods(paymentMethodsRequest);
    return ResponseEntity.ok()
        .body(response);
}
```

The controller ensures that all actions are done against the active shopping cart of the user logged into the session. This ensures that security issues like man-in-the-middle attacks and request spoofing do not happen. When payment is completed successfully, we close the active shopping cart, ensuring every user has only one active shopping cart at a time.

On the client-side, we will create a React page to show the [payment options](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/30899562564be5f2b6dd1291af812c3f4ff8e22e#diff-3723704a01d50dc81a81a4a3e56936eb) and payment [result status](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/30899562564be5f2b6dd1291af812c3f4ff8e22e#diff-8e5b7843a18c84ef4f242a352d2572fe), a [redux reducer](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/30899562564be5f2b6dd1291af812c3f4ff8e22e#diff-6d93eb93377c66204b2e844472d95860) to talk to the new API endpoints. We will also download and add the Adyen client-side resources to our [index.html](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/30899562564be5f2b6dd1291af812c3f4ff8e22e#diff-747e7a37d03935942014106202566830) file.

Here are the important bits of the checkout page since this is where we handle the Adyen javascript integration from within React.

```tsx
//import ...;

export interface ICheckoutProp extends StateProps, DispatchProps {}

class CheckoutContainer extends React.Component<ICheckoutProp> {
  private paymentContainer = React.createRef<HTMLDivElement>();
  //...

  componentDidMount() {
    this.props.getEntityForCurrentUser();
    this.props.getAdyenConfig();
    this.props.getPaymentMethods();
  }

  componentDidUpdate(prevProps: ICheckoutProp) {
    const { paymentMethodsRes, config, paymentRes, paymentDetailsRes, errorMessage } = this.props;
    if (errorMessage && errorMessage !== prevProps.errorMessage) {
      window.location.href = `/status/error?reason=${errorMessage}`;
      return;
    }
    if (paymentMethodsRes && config && (paymentMethodsRes !== prevProps.paymentMethodsRes || config !== prevProps.config)) {
      this.checkout = new AdyenCheckout({
        ...config,
        paymentMethodsResponse: this.removeNilFields(paymentMethodsRes),
        onAdditionalDetails: this.onAdditionalDetails,
        onSubmit: this.onSubmit
      });
    }
    if (paymentRes && paymentRes !== prevProps.paymentRes) {
      this.processPaymentResponse(paymentRes);
    }
    if (paymentRes && paymentDetailsRes !== prevProps.paymentDetailsRes) {
      this.processPaymentResponse(paymentDetailsRes);
    }
  }

  removeNilFields = obj => {
    //...
  };

  processPaymentResponse = paymentRes => {
    if (paymentRes.action) {
      this.paymentComponent.handleAction(paymentRes.action);
    } else {
      //...
      window.location.href = `/checkout/status/${urlPart}?reason=${paymentRes.resultCode}&paymentType=unknown`;
    }
  };

  onSubmit = (state, component) => {
    if (state.isValid) {
      this.props.initiatePayment({
        ...state.data,
        origin: window.location.origin
      });
      this.paymentComponent = component;
    }
  };

  onAdditionalDetails = (state, component) => {
    this.props.submitAdditionalDetails(state.data);
    this.paymentComponent = component;
  };

  handlePaymentSelect = (type: string) => () => {
    this.checkout.create(type).mount(this.paymentContainer?.current);
  };

  render() {
    const { cart } = this.props;

    return (
      <Row className="d-flex justify-content-center" style={{ minHeight: '80vh' }}>
        <Col lg="9" md="12">
          <h2>Make payment</h2>
          <p className="lead">You are paying total of € {cart.totalPrice}</p>
          <Row className="pt-4">
            <Col md="4" className="d-flex flex-column">
              <label>
                <strong>Choose a payment type</strong>
              </label>
              <ButtonGroup vertical>
                <Button onClick={this.handlePaymentSelect('card')}>Credit Card</Button>
                <Button onClick={this.handlePaymentSelect('ideal')}>iDEAL</Button>
              </ButtonGroup>
            </Col>
            <Col md="8">
              <div ref={this.paymentContainer} className="payment"></div>
            </Col>
          </Row>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ checkout, shoppingCart }: IRootState) => ({
  //...
});

const mapDispatchToProps = {
  //...
};
type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CheckoutContainer);
```

Here is the entire [changelog](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/30899562564be5f2b6dd1291af812c3f4ff8e22e) for this feature. Make the changes to the application accordingly and see the changes reflected on the shopping cart page. Make sure to set the following environment variables first.

```shell
export ADYEN_API_KEY=yourAdyenApiKey
export ADYEN_MERCHANT_ACCOUNT=yourAdyenMerchantAccount
export ADYEN_ORIGIN_KEY=yourAdyenOriginKeyForCorrectDomain
```

## Running the app in production

Now that we have made all the required changes, let us compile and run our app in production mode.

First, let us run the generated unit and integration tests to ensure we haven't broken anything:

```shell
./gradlew npm_test test integrationTest
```

Now, let's start a MySQL database as our application uses an in-memory H2 database for development and MySQL for production, this makes development easier. We will be using Docker compose to run the DB. You can also manually run a MySQL DB if you prefer.

```shell
docker-compose -f src/main/docker/mysql.yml up -d
```

The above command will start up MySQL DB from the included Docker compose file. Now, run the below command to run the application in production mode:

```shell
./gradlew -Pprod
```

You can also package the application using the command `./gradlew -Pprod clean bootJar` and then run the JAR using `java -jar build/libs/*.jar`

Now, visit [https://localhost:8080](https://localhost:8080) and use the default users mentioned on the home page to log in and explore the application. You can use the [test cards](https://docs.adyen.com/development-resources/test-cards/test-card-numbers) from Adyen to simulate payments

## Conclusion

That's it. We have successfully built an e-commerce application complete with a product checkout and payment flow that can accept multiple forms of payment. I highly recommend that you check out the [sample application](https://github.com/adyen-examples/adyen-java-react-ecommerce-example) to get a better context of what has been built.

The example application also has a [user registration flow](https://github.com/adyen-examples/adyen-java-react-ecommerce-example/commit/172eb995a83dea1dcd45140732aaf2906fb15afd) that you can checkout

I hope this helps anyone trying to build shopping carts and payment flows for their Java E-Commerce application.

---

If you like this article, please leave a like or a comment. Let us know if you would like to see more of similar content on our blogs.

Cover image credit: Photo by [Paul Felberbauer](https://unsplash.com/@servuspaul?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText) on [Unsplash](https://unsplash.com/s/photos/online-payment?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText)
