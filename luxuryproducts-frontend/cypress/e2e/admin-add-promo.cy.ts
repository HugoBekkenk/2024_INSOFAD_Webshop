describe('Admin Promo Code Creation Test', () => {
    beforeEach(() => {
      cy.visit('http://localhost:4200/auth/login');
      cy.get('input#email').type('admin@mail.com');
      cy.get('input#password').type('IJS+N3u#(Z0Ds]3~#qZw');
      cy.get('button').contains('Login').click();
    });
  
    it('should create a new promo code', () => {
      cy.get('a').contains('Admin').click();
      cy.url().should('include', '/admin');
  
      cy.get('input[type="radio"][id="option4"]').click();
      cy.url().should('include', '/admin-promo-admin');
  
        cy.scrollTo('bottom');
        cy.get('a').contains('+ Add New Promocode').click();
        cy.url().should('include', '/admin-promo-add');



      cy.get('input#code').type('SUMMER2024');
      cy.get('input#discount').type('20');
      cy.get('input#expiryDate').type('2024-06-30T23:59');
      cy.get('input#maxUsageCount').type('100');
      cy.get('select#type').select('Percentage');
  
      cy.get('button').contains('Create Promo Code').click();
  
      cy.contains('Manage Promocodes').should('be.visible');
    });
  });
  