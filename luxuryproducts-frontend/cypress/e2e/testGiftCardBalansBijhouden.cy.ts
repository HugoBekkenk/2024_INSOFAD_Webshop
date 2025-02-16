describe('Buying Products with a Gift Card', () => {
  let productData: any;
  let giftCardData: any;
  let receivedGiftCardData: any;

  before(() => {
    cy.fixture('products').then((data) => {
      productData = data;
    });

    cy.fixture('sentGiftCard').then((data) => {
      giftCardData = data;
    });

    cy.fixture('recievedGiftCard').then((data) => {
      receivedGiftCardData = data;
    });
  });
  beforeEach(() => {
    cy.login('test@mail.com', 'Test123!');

    cy.setAuthToken();

    cy.intercept('GET', '/api/products', { fixture: 'products.json' }).as('getProducts');
    cy.intercept('POST', '/api/orders', {
      statusCode: 200,
      body: { message: 'Order placed successfully' }
    }).as('placeOrder');
    
    cy.intercept('GET', '/api/giftCards/mini', { fixture: 'sentGiftCard.json' }).as('getMiniGiftCards');
    cy.intercept('GET', '/api/giftCards/getAllGiftCardsReceivedByUser', { fixture: 'recievedGiftCard.json' }).as('getReceivedGiftCards');

    cy.visit('http://localhost:4200/products');
  });

  it('should add products and a gift card to the cart, apply promo code, and proceed to payment', () => {
    cy.contains(productData[3].name).closest('.card').within(() => {
      cy.get('.btn.btn-primary').contains('Details').click();
    });

    cy.url().should('include', `/products/${productData[3].id}`);

    cy.contains('Platform:').next().contains('Pc').click();
    cy.contains('Edition:').next().contains('Default').click();

    cy.get('button').contains('Buy Now').click();

    cy.visit('http://localhost:4200/products');

    cy.contains(productData[1].name).closest('.card').within(() => {
      cy.get('.btn.btn-primary').contains('Details').click();
    });

    cy.url().should('include', `/products/${productData[1].id}`);

    cy.contains('Platform:').next().contains('Pc').click();
    cy.contains('Edition:').next().contains('Default').click();

    cy.get('button').contains('Buy Now').click();

    cy.get('a').contains('Cart').click();

    cy.url().should('include', '/cart');
    cy.contains(productData[3].name).should('be.visible');
    cy.contains(productData[1].name).should('be.visible');

    cy.get('button').contains('Buy').click();

    cy.url().should('include', '/orders');

    cy.get('input[placeholder="Zip Code"]').type('1234AB');
    cy.get('input[placeholder="House Number"]').type('72');

     cy.wait('@getMiniGiftCards').its('response.statusCode').should('eq', 200);
     cy.get('.multiselect-dropdown').click();
     cy.get('.multiselect-item-checkbox').should('have.length.at.least', 1); 
 
     cy.get('.multiselect-item-checkbox').contains(giftCardData[9].cardCode).click();

    cy.get('body').click(0, 0); 

    cy.get('button').contains('Go To Payment').click({ force: true });

    cy.wait('@placeOrder').its('response.statusCode').should('eq', 200);

    cy.visit('http://localhost:4200/paymentsuccessful')

    cy.get('a').contains('Profile').click();

    cy.url().should('include', '/profile');
    cy.wait('@getReceivedGiftCards').its('response.statusCode').should('eq', 200);
    cy.contains('Recieved Gift Cards Information').should('be.visible');
    cy.contains('Gift Card 10').should('be.visible');
    cy.contains('125.02').should('be.visible');
    cy.contains('GCKFEWU6J#^E').should('be.visible');
  });
});