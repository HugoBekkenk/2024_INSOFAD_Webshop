
describe('Buying Products with a Gift Card', () => {
  let productData: any;
  let giftCardData: any;

  before(() => {
    cy.fixture('products').then((data) => {
      productData = data;
    });
    cy.fixture('sentGiftCard').then((data) => {
      giftCardData = data;
    });
  });

  beforeEach(() => {
    cy.loginWithoutIntercept('test@mail.com', 'Test123!');

    cy.setAuthToken();

    cy.intercept('GET', '/api/products', { fixture: 'products.json' }).as('getProducts');
    cy.intercept('POST', '/api/orders', {
      statusCode: 200,
      body: { message: 'Order placed successfully' }
    }).as('placeOrder');
    cy.intercept('GET', '/api/promocodes', {
      statusCode: 200,
      body: [
        { code: 'FREE_DISCOUNT', discount: 10, type: 'PERCENTAGE' }
      ]
    }).as('getPromoCodes');
    cy.intercept('GET', '/api/giftCards/mini', { fixture: 'sentGiftCard.json' }).as('getMiniGiftCards');

    cy.visit('http://localhost:4200/products');
  });

  it('should add products and a gift card to the cart, apply promo code, and proceed to payment', () => {
    cy.contains(productData[2].name).closest('.card').within(() => {
      cy.get('.btn.btn-primary').contains('Details').click();
    });

    cy.url().should('include', `/products/${productData[2].id}`);

    cy.contains('Platform:').next().contains('Pc').click();
    cy.contains('Edition:').next().contains('Default').click();

    cy.get('button').contains('Buy Now').click();

    cy.visit('http://localhost:4200/products');

    cy.contains(productData[4].name).closest('.card').within(() => {
      cy.get('.btn.btn-primary').contains('Details').click();
    });

    cy.url().should('include', `/products/${productData[4].id}`);

    cy.contains('Platform:').next().contains('Pc').click();
    cy.contains('Edition:').next().contains('Default').click();

    cy.get('button').contains('Buy Now').click();

    cy.get('a').contains('Cart').click();

    cy.url().should('include', '/cart');
    cy.contains(productData[2].name).should('be.visible');
    cy.contains(productData[4].name).should('be.visible');

    cy.get('input[placeholder="Promo code"]').type('FREE_DISCOUNT');
    cy.get('button').contains('Apply Promo Code').click();

    cy.get('.total-price-section').within(() => {
      cy.contains('Order Total: €106.18');
    });

    cy.get('button').contains('Buy').click();

    cy.url().should('include', '/orders');

    cy.get('input[placeholder="Zip Code"]').type('1234AB');
    cy.get('input[placeholder="House Number"]').type('72');

     cy.wait('@getMiniGiftCards').its('response.statusCode').should('eq', 200);
     cy.get('.multiselect-dropdown').click();
     cy.get('.multiselect-item-checkbox').should('have.length.at.least', 1); 
 
     cy.get('.multiselect-item-checkbox').contains(giftCardData[0].cardCode).click();
 
    cy.get('body').click(0, 0); 

    cy.get('button').contains('Go To Payment').click({ force: true });

    cy.wait('@placeOrder').its('response.statusCode').should('eq', 200);

    cy.visit('http://localhost:4200/paymentsuccessful')
  });
});