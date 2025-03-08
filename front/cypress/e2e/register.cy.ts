describe('Register spec', () => {
  beforeEach(() => {
    cy.visit('/register')
  })

  it('displays a disabled button if all fields are not filled correctly', () => {
    cy.get('button[type="submit"]').should('be.disabled');

    cy.get('input[formControlName=firstName]').type('new');
    cy.get('button[type="submit"]').should('be.disabled');

    cy.get('input[formControlName=lastName]').type('user');
    cy.get('button[type="submit"]').should('be.disabled');

    cy.get('input[formControlName=email]').type('user@email.com');
    cy.get('button[type="submit"]').should('be.disabled');

    cy.get('input[formControlName=password]').type('test!1234');

    cy.get('button[type="submit"]').should('not.be.disabled');
  });

  it('displays a disabled button if email is invalid', () => {
    cy.get('input[formControlName=firstName]').type('new');
    cy.get('input[formControlName=lastName]').type('user');
    cy.get('input[formControlName=email]').type('invalidEmail');
    cy.get('input[formControlName=password]').type('test!1234');

    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('Registered successfully', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {
      body: {
        firstName: 'Laura',
        lastName: 'Vongsy',
        email: 'vongsy.laura@yoga.com',
        password: 'test!1234'
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')


    cy.get('input[formControlName=firstName]').type('Laura')
    cy.get('input[formControlName=lastName]').type('Vongsy')
    cy.get('input[formControlName=email]').type('vongsy.laura@yoga.com')
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)


    cy.url().should('include', '/login')
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 2,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("vongsy.laura@yoga.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })
});