describe('visit admin product page', () => {
    it('displays 13 products by default', () => {
        cy.fixture('products').then((json) => {
            cy.intercept('GET', '/api/products', json).as('getProducts')
        })
        cy.visit('/products')
        cy.wait('@getProducts')
        cy.get('[id^=filterOption1]').click()
        cy.get("[id^=product-card]").should('have.length', 13)
    })


    it('display 10 gift cards by default', () => {
        cy.fixture('products').then((json) => {
            cy.intercept('GET', '/api/products', json).as('getProducts')
        })
        cy.visit('/products')
        cy.wait('@getProducts')
        cy.get('[id^=filterOption2]').click()
        cy.get("[id^=product-card]").should('have.length', 10)
     })
})