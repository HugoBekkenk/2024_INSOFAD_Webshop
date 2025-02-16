describe('Admin Promo Code Edit Test', () => {
  beforeEach(() => {
    cy.visit('http://localhost:4200/auth/login');
    cy.get('input#email').type('admin@mail.com');
    cy.get('input#password').type('IJS+N3u#(Z0Ds]3~#qZw');
    cy.get('button').contains('Login').click();
  });

  it('should edit an existing promo code', () => {
    cy.get('a').contains('Admin').click();
    cy.url().should('include', '/admin');

    cy.get('input[type="radio"][id="option4"]').click();
    cy.url().should('include', '/admin-promo-admin');

    cy.scrollTo('bottom');
    cy.get('button').contains('Edit').click();
    cy.url().should('include', '/admin-promo-edit/1');

    cy.get('input#code').clear().type('MAY2024');
    cy.get('input#discount').clear().type('15');
    cy.get('input#expiryDate').clear().type('2024-08-31T23:59:59');
    cy.get('input#maxUsageCount').clear().type('100');
    cy.get('select#type').select('Percentage');

    cy.get('button').contains('Edit Promo Code').click();

    cy.contains('Edit Promo Code').should('be.visible');
  });
});
