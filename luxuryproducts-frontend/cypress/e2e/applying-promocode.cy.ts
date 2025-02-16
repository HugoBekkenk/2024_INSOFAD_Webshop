describe('Product Purchase Flow', () => {
  beforeEach(() => {
    cy.visit('http://localhost:4200/auth/login');
    cy.get('input#email').type('admin@mail.com');
    cy.get('input#password').type('IJS+N3u#(Z0Ds]3~#qZw');
    cy.get('button').contains('Login').click();
  });
  
  it('should purchase a product with a variant, apply a promo code, and place the order', () => {
    
    cy.visit('/');
 
    cy.get('a.nav-link').contains('Products').click();
 
    cy.get('.card').first().contains('Details').click();
 
    cy.get('.product-details-container').should('be.visible');

    
    cy.get('.btn-group button').first().click();
 
    cy.get('.btn-group button').first().should('have.class', 'btn-primary');
 
    cy.get('.btn-group button').contains('Definitive +€10.00').click();

    cy.get('.btn-group button').contains('Definitive +€10.00').should('have.class', 'btn-primary');

    cy.get('button').contains('Buy Now').click();

    cy.get('a.nav-link').contains('Cart').click();

    cy.get('input[name="promoCode"]').type('MAY2024');

    cy.get('button').contains('Apply Promo Code').click();

    cy.contains('Order Total').should('be.visible');

 

  });
});
 