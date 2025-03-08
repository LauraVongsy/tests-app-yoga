describe('Login spec', () => {

  beforeEach(() => {
    cy.visit('/login')
  })

  it('displays an enabled submit button after fields are filled correctly', () => {

    cy.get('button[type="submit"]').should('be.disabled');

    cy.get('input[formControlName=email]').type('user@example.com');
    cy.get('input[formControlName=password]').type('password');

    cy.get('button[type="submit"]').should('not.be.disabled');
  });

  it('displays a disabled submit button if fields are filled incorrectly', () => {

    cy.get('input[formControlName=email]').type('invalidEmail');
    cy.get('input[formControlName=password]').type('password');

    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('does not log the user (wrong credentials)', () => {

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: {
        error: 'Bad credentials',
      },
    }).as('wrongLogin');

    cy.get('input[formControlName=email]').type('wrongEmail@wrong.com');
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.wait('@wrongLogin');

    cy.contains('An error occurred').should('be.visible');
  });

  it('logs the user sucessfully', () => {

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

    cy.url().should('include', '/sessions')
  })
});