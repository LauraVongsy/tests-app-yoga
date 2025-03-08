describe('Me spec', () => {


    beforeEach(() => {
        cy.visit('/login')

        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'userName',
                firstName: 'firstName',
                lastName: 'lastName',
                admin: true
            },
        })

        cy.intercept(
            {
                method: 'GET',
                url: '/api/session',
            },
            []).as('session')

        const user = {
            firstName: 'Jean',
            lastName: 'Pierre',
            email: 'jean.pierre@test.com',
            admin: false,
            createdAt: new Date(2025, 2, 2),
            updatedAt: new Date(2025, 3, 5),
        };

        cy.intercept('GET', '/api/user/1', {
            body: user,
        }).as('getUser');

        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

        cy.wait('@session');

        cy.url().should('include', '/sessions');

        cy.contains('span.link', 'Account').should('be.visible');

        cy.contains('span.link', 'Account').click();
    });

    it('displays user details and navigate back', () => {
        cy.wait('@getUser');

        cy.contains('Name: Jean PIERRE').should('be.visible');
        cy.contains('Email: jean.pierre@test.com').should('be.visible');
        cy.contains('Delete my account:').should('be.visible');
        cy.contains('Create at:').should('be.visible');
        cy.contains('Last update:').should('be.visible');

        cy.get('button[mat-icon-button]').click();
        cy.url().should('not.include', '/me');

        cy.wait('@session');

        cy.url().should('include', '/sessions');
    });

    it('deletes account', () => {
        cy.wait('@getUser');

        cy.intercept('DELETE', '/api/user/1', {
            statusCode: 204,
        }).as('deleteUser');

        cy.intercept('POST', '/api/auth/logout', {
            statusCode: 204,
        }).as('logout');

        cy.intercept(
            {
                method: 'GET',
                url: '/api/session',
            },
            []
        ).as('session');

        cy.contains('Delete my account:').click();
        cy.get('button.mat-raised-button').click();

        cy.wait('@deleteUser');

        cy.url().should('not.include', '/me');

        cy.contains('.mat-simple-snack-bar-content', 'Your account has been deleted !').should('be.visible');

    });
});