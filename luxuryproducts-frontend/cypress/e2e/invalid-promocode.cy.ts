describe('Product Purchase Flow', () => {
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

    cy.get('input[name="promoCode"]').type('INVALID_PROMOCODE');

    cy.get('button').contains('Apply Promo Code').click();
 
    cy.get('.alert').contains('Invalid promo code').should('be.visible');
  });
});
 