describe('not found spec', () => {
    it('redirect to the not found page', () => {
        cy.visit('/not-found');

        cy.contains('Page not found').should('be.visible');
    });
});