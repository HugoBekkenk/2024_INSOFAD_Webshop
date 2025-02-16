Cypress.Commands.add('login', () => {
    cy.visit('/auth/login')
    cy.get('input[id=email]').type("admin@mail.com")
    cy.get('input[id=password]').type("IJS+N3u#(Z0Ds]3~#qZw")
    cy.get('button').contains('Login').click()
    cy.url().should('contain', '')
  })

describe('visit admin product page', () => {
    beforeEach(() => {
        cy.login()
        cy.wait(500)
        cy.fixture('products').then((products) => {
            cy.intercept('GET', 'api/products', products).as('getProducts')
        })

        cy.visit('/admin')
        cy.get('[id^=filterOption1]').click()
        cy.wait('@getProducts')
    })

    it('displays 13 products and 10 giftcards by default', () => {
        cy.get("[id^=product-card]").should('have.length', 13)
        
        cy.get('[id^=filterOption2]').click()
        cy.get("[id^=product-card]").should('have.length', 10)
    })


    it('displays 1 less product when one is deleted', () => {
        cy.get("[id^=product-card]").should('have.length', 13)
        cy.intercept('DELETE', 'api/admin/product/13', {
            statusCode: 200,
            body: {
                "Product deleted with id 13": String
            },
          }).as('deleteProduct')  

        cy.fixture('productsDelete').then((productsAfterDelete) => {
            cy.intercept('GET', '/api/products', productsAfterDelete).as('getProductsAfterDelete')
        })

        cy.get('button[id^=removeProduct0]').click()
        cy.wait('@deleteProduct')
        cy.wait('@getProductsAfterDelete')
        cy.get("[id^=product-card]").should('have.length', 12)
    })

    it('display 1 less variant when one is deleted', () => {
        cy.get("[id^=variant13]").should('have.length', 2)
        cy.intercept('DELETE', 'api/admin/variant/25', {
            statusCode: 200,
            body: {
                "Variant deleted with id 25": String
            },
          }).as('deleteVariant')  

        cy.fixture('variantDelete').then((productsAfterDelete) => {
            cy.intercept('GET', '/api/products', productsAfterDelete).as('getProductsAfterDelete')
        })

        cy.get('button[id^=removeVariant25]').click()
        cy.wait('@deleteVariant')
        cy.wait('@getProductsAfterDelete')
        cy.get("[id^=variant13]").should('have.length', 1)
    })

    it('display 1 less option when one is deleted', () => {
        cy.get("[id^=option25-]").should('have.length', 3)
        cy.intercept('DELETE', 'api/admin/option/63', {
            statusCode: 200,
            body: {
                "Option deleted with id 63": String
            },
          }).as('deleteOption')  

        cy.fixture('optionDelete').then((productsAfterDelete) => {
            cy.intercept('GET', '/api/products', productsAfterDelete).as('getProductsAfterDelete')
        })

        cy.get('button[id^=removeOption63]').click()
        cy.wait('@deleteOption')
        cy.wait('@getProductsAfterDelete')
        cy.get("[id^=option25-]").should('have.length', 2)
    })

    it('should be able to create a product and display it after', () => {
        cy.get('button[id^=addProduct]').click()
        cy.get('[id=form1\\.1]').type("Genshin Impact")
        cy.get('[id=form1\\.2]').type("Gacha")
        cy.get('[id=form2\\.1]').type("19.99")
        cy.get('[id=form2\\.2]').type("28-sep-2020")
        cy.get('[id=form2\\.3]').type("20")
        cy.get('[id=form3\\.1]').type("https://cdn1.epicgames.com/offer/879b0d8776ab46a59a129983ba78f0ce/genshintall_1200x1600-4a5697be3925e8cb1f59725a9830cafc")
        cy.get('[id=form3\\.2]').type("Hoyoverse")
        cy.get('[id=form4\\.1]').type("Genshin Impact is an action role-playing game developed by miHoYo, published by miHoYo in mainland China and worldwide by Cognosphere, HoYoverse. It was released for Android, iOS, PlayStation 4, and Windows in 2020, and on PlayStation 5 in 2021. The game features an anime-style open-world environment and an action-based battle system using elemental magic and character-switching.")
        cy.get('[id=form4\\.2]').type("CPU: Intel Core i7 equivalent or higher, RAM: 16 GB, VIDEO CARD: NVIDIA GeForce GTX 1060 6 GB and higher, DEDICATED VIDEO RAM: 6 GB, PIXEL SHADER: 5.1, VERTEX SHADER: 5.1, OS: Windows 7 SP1 64-bit, Windows 8.1 64-bit or Windows 10 64-bit, FREE DISK SPACE: 30 GB.")
        cy.get('[id=form5\\.1]').type("Platform")
        cy.get('[id=form5\\.2]').type("Pc")
        cy.get('[id=form5\\.3]').type("0")
    
        cy.intercept('POST', 'api/admin/product', {
            statusCode: 200,
            body: {
                "Created a product": String
            },
          }).as('createProduct') 
        
        cy.fixture('productCreate').then((productsAfterCreate) => {
            cy.intercept('GET', '/api/products', productsAfterCreate).as('getProductsAfterCreate')
        })
    
        cy.get('button[id^=createProduct]').click()
    
        cy.wait('@createProduct')
        cy.wait('@getProductsAfterCreate')
    
        cy.get('[id^=filterOption1]').click()
        cy.get("[id^=product-card]").should('have.length', 14)
        cy.get("[id=productName25]").should('be.visible').and('contain', 'Genshin Impact')
        cy.get("[id=productDescription25]").should('be.visible').and('contain', 'Genshin Impact is an action role-playing game developed by miHoYo, published by miHoYo in mainland China and worldwide by Cognosphere, HoYoverse. It was released for Android, iOS, PlayStation 4, and Windows in 2020, and on PlayStation 5 in 2021. The game features an anime-style open-world environment and an action-based battle system using elemental magic and character-switching.')
        cy.get("[id=productPrice25]").should('be.visible').and('contain', '€19.99')
        cy.get("[id=variant25-28]").should('be.visible').and('contain', 'Platform:')
        cy.get("[id=option28-69]").should('be.visible').and('contain', ' Pc +€0.00')
        
    })

    it('should be able to create a variant and display it after', () => {
        cy.get('button[id^=addVariant13]').click()
        cy.get('[id=form1\\.1-13]').type("Format")
        cy.get('[id=form1\\.2-13]').type("Digital")
        cy.get('[id=form1\\.3-13]').type("0")
    
        cy.intercept('POST', 'api/admin/variant', {
            statusCode: 200,
            body: {
                "Created a variant": String
            },
          }).as('createVariant') 
        
        cy.fixture('variantCreate').then((productsAfterCreate) => {
            cy.intercept('GET', '/api/products', productsAfterCreate).as('getProductsAfterCreate')
        })
    
        cy.get('button[id^=createVariant13]').click()
    
        cy.wait('@createVariant')
        cy.wait('@getProductsAfterCreate')
    
        cy.get('[id^=filterOption1]').click()
        cy.get("[id=variant13-30]").should('be.visible').and('contain', 'Format:')
        cy.get("[id=option30-71]").should('be.visible').and('contain', 'Digital +€0.00')
        
    })

    it('should be able to create a option and display it after', () => {
        cy.get('button[id^=addOption26]').click()
        cy.get('[id=form1\\.1-26]').type("Collectors")
        cy.get('[id=form1\\.2-26]').type("20")
    
        cy.intercept('POST', 'api/admin/option', {
            statusCode: 200,
            body: {
                "Created a option": String
            },
          }).as('createOption') 
        
        cy.fixture('optionCreate').then((productsAfterCreate) => {
            cy.intercept('GET', '/api/products', productsAfterCreate).as('getProductsAfterCreate')
        })
    
        cy.get('button[id^=createOption26]').click()
    
        cy.wait('@createOption')
        cy.wait('@getProductsAfterCreate')
    
        cy.get('[id^=filterOption1]').click()
        cy.get("[id=option26-72]").should('be.visible').and('contain', 'Collectors +€20.00')
        
    })
    

})