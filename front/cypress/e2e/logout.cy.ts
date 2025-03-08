describe('Logout spec', () => {

  it('logs out the user', () => {
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

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions');

    cy.contains('span.link', 'Logout').click();

    // Ugly hack to ensure there is only one trailing slash when running e2e:ci
    const url = Cypress.config().baseUrl.replace(/\/$/, '') + '/';

    cy.url().should('eq', url);

    cy.wait('@session').its('response.body').should('be.empty');
  })
}
)