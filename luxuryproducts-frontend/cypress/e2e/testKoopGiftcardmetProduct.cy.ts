

describe('Buying a Product and a Gift Card', () => {
  let productData: any;

  before(() => {
    cy.fixture('products').then((data) => {
      productData = data;
    });
  });

  beforeEach(() => {
    cy.login('test@mail.com', 'Test123!');

    cy.setAuthToken();

    cy.visit('http://localhost:4200/products');
  });

  it('should add a product and a gift card to the cart and verify the cart contents', () => {
    cy.contains(productData[5].name).closest('.card').within(() => {
      cy.get('.btn.btn-primary').contains('Details').click();
    });

    cy.url().should('include', `/products/${productData[5].id}`);

    cy.contains('Platform:').next().contains('Pc').click();
    cy.contains('Edition:').next().contains('Default').click();

    cy.get('button').contains('Buy Now').click();

    cy.visit('http://localhost:4200/products');

    cy.scrollTo('bottom');
    cy.contains(productData[13].name).closest('.card').within(() => {
      cy.get('.btn.btn-primary').contains('Details').click();
    });

    cy.url().should('include', `/products/${productData[13].id}`);

    cy.get('button').contains('Buy Now').click();

    cy.get('a').contains('Cart').click();

    cy.url().should('include', '/cart');
    cy.contains(productData[5].name).should('be.visible');
    cy.contains(productData[13].name).should('be.visible');
    cy.get('input[placeholder="Promo code"]').type('MAY2024');
    cy.get('button').contains('Apply Promo Code').click();

    cy.get('.total-price-section').within(() => {
      cy.contains('Order Total: €43.99');
    });

    cy.get('button').contains('Buy').click();

    cy.url().should('include', '/orders');

    cy.get('input[placeholder="Zip Code"]').type('1234AB');
    cy.get('input[placeholder="House Number"]').type('72');
    cy.get('button').contains('Go To Payment').click();

  });
});