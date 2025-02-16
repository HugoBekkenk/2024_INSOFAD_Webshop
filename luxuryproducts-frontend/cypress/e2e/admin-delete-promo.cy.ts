describe('Admin Promo Code Delete Test', () => {
    beforeEach(() => {
      cy.visit('http://localhost:4200/auth/login');
      cy.get('input#email').type('admin@mail.com');
      cy.get('input#password').type('IJS+N3u#(Z0Ds]3~#qZw');
      cy.get('button').contains('Login').click();
    });
  
    it('should delete an existing promo code', () => {
      cy.get('a').contains('Admin').click();
      cy.url().should('include', '/admin');
  
      cy.get('input[type="radio"][id="option4"]').click();
      cy.url().should('include', '/admin-promo-admin');
  
      cy.scrollTo('bottom');
      cy.get('button').contains('Delete').click();
  
      cy.on('window:confirm', (str) => {
        expect(str).to.equal('Are you sure you want to delete this promo code?');
        return true;
      });

    });
  });
  