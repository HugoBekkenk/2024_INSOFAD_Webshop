// cypress/support/index.d.ts

declare namespace Cypress {
  interface Chainable {
    /**
     * Custom command to set the authentication token
     * @example cy.setAuthToken()
     */
    setAuthToken(): Chainable<void>;

    /**
     * Custom command to log in
     * @example cy.login('test@mail.com', 'Test123!m')
     */
    login(email: string, password: string): Chainable<void>;

    /**
     * Custom command to log in without intercepting
     * @example cy.loginWithoutIntercept('test@mail.com', 'Test123!m')
     */
    loginWithoutIntercept(email: string, password: string): Chainable<void>;
  }
}
