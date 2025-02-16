
describe('Create Gift Card via Admin Portal', () => {
  beforeEach(() => {
    cy.login('admin@mail.com', 'IJS+N3u#(Z0Ds]3~#qZw');
    cy.setAuthToken();

    cy.intercept('POST', '/api/giftCards', {
      statusCode: 200,
      body: { message: 'Gift card has been added' }
    }).as('createGiftCard');

    cy.visit('http://localhost:4200/admin');
  });

  it('should create a gift card using data from JSON file', () => {
    cy.fixture('createGiftCard').then((giftCardData) => {
      cy.get('input[type="radio"][id="option3"]').click();

      cy.get('input[placeholder="Name"]').type(giftCardData.name);
      cy.get('input[placeholder="Amount"]').type(giftCardData.amount.toString());
      cy.get('input[placeholder="Image URL"]').type(giftCardData.image);

      cy.get('button').contains('Add Gift Card').click();

      cy.wait('@createGiftCard').its('response.statusCode').should('eq', 200);

      cy.contains('Gift Cards').click(); 
    });
  });
});
