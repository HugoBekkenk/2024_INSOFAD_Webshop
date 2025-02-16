
describe('Gift Card Sending Process', () => {
  context('when user is logged in', () => {
    beforeEach(() => {
      cy.login('test@mail.com', 'Test123!');

      cy.setAuthToken();

      cy.intercept('GET', '/api/giftCards', { fixture: 'giftCards.json' }).as('getGiftCards');

      cy.visit('http://localhost:4200/gift-cards', {
        onBeforeLoad(win) {
          const authToken = win.localStorage.getItem('authToken');
          if (!authToken) {
            throw new Error('Auth token is null');
          }
        }
      });

      cy.wait('@getGiftCards');
    });

    it('should send a gift card successfully', () => {
      cy.intercept('POST', '/api/giftCards/test@mail.com/1', {
        statusCode: 200,
        body: { message: 'Gift Card sent to user with email : test@mail.com' }
      }).as('sendGiftCard');

      cy.get('.btn.btn-primary').contains('Details').click();

      cy.url().should('include', '/gift-cards');

      cy.get('button').contains('Send Now').click();

      cy.get('input[name="email"]').type('test@mail.com');

      cy.window().then((win) => {
        cy.stub(win, 'alert').as('alert');
      });

      cy.get('button[type="submit"]').contains('Submit').click();

      cy.wait('@sendGiftCard');

      cy.get('@alert').should('have.been.calledOnce');
      cy.get('@alert').should('have.been.calledWith', 'Gift Card sent to user with email : test@mail.com');
    });
  });

  context('when the user gift card is being sent to does not exist', () => {
    beforeEach(() => {
      cy.login('test@mail.com', 'Test123!');

      cy.setAuthToken();

      cy.intercept('GET', '/api/giftCards', { fixture: 'giftCards.json' }).as('getGiftCards');

      cy.visit('http://localhost:4200/gift-cards', {
        onBeforeLoad(win) {
          const authToken = win.localStorage.getItem('authToken');
          if (!authToken) {
            throw new Error('Auth token is null');
          }
        }
      });

      cy.wait('@getGiftCards');
    });

    it('should show "Unauthorized" alert when trying to send a gift card', () => {
      cy.intercept('POST', '/api/giftCards/test@mail.com/1', {
        statusCode: 401,
        body: { error: { message: 'Unauthorized' } }
      }).as('sendGiftCardUnauthorized');

      cy.get('.btn.btn-primary').contains('Details').click();

      cy.url().should('include', '/gift-cards');

      cy.get('button').contains('Send Now').click();

      cy.get('input[name="email"]').type('test@mail.com');

      cy.window().then((win) => {
        const stub = cy.stub(win, 'alert').callsFake((msg) => {
          expect(msg).to.equal('Unauthorized');
        });

        cy.wrap(stub).as('alert');
      });

      cy.get('button[type="submit"]').contains('Submit').click();

      cy.wait('@sendGiftCardUnauthorized');

      cy.get('@alert').should('have.been.calledOnce');
    });
  });
});