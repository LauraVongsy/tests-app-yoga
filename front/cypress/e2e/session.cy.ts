describe('Session Management', () => {
    beforeEach(() => {
        cy.visit('/login');

        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'yogiMaster',
                firstName: 'Emma',
                lastName: 'Williams',
                admin: true,
            },
        });

        cy.intercept('GET', '/api/session', [
            {
                id: 1,
                name: 'Morning Yoga Flow',
                description: 'Start your day with a refreshing yoga session.',
                date: '2024-06-15',
                teacher_id: 2,
            },
        ]).as('session');

        cy.intercept('GET', '/api/teacher', [
            {
                id: 2,
                firstName: 'Sophia',
                lastName: 'Johnson',
                email: 'sophia@yoga.com',
                password: 'secure123',
                admin: true,
            },
        ]).as('teacher');

        cy.get('input[formControlName=email]').type('yoga@studio.com');
        cy.get('input[formControlName=password]').type(`${'flow!2024'}{enter}{enter}`);
        cy.url().should('include', '/sessions');
    });

    describe('Create Session', () => {
        it('creates a session successfully', () => {
            cy.intercept('POST', '/api/session', {
                statusCode: 201,
                body: {
                    id: 1,
                    name: 'Morning Yoga Flow',
                    description: 'Start your day with a refreshing yoga session.',
                    date: '2024-06-15',
                    teacher_id: 2,
                },
            }).as('createSession');

            cy.get('button[routerLink="create"]').click();
            cy.get('input[formControlName=name]').type('Morning Yoga Flow');
            cy.get('textarea[formControlName="description"]').type(
                'Start your day with a refreshing yoga session.'
            );
            cy.get('input[formControlName="date"]').type('2024-06-15');
            cy.get('mat-select[formControlName="teacher_id"]').click();
            cy.get('mat-option').contains('Sophia Johnson').click();
            cy.get('button[type="submit"]').click();
            cy.contains('Session created').should('be.visible');
            cy.wait('@session');
            cy.contains('Morning Yoga Flow').should('be.visible');
        });
    });

    describe('Update Session', () => {
        it('updates a session successfully', () => {
            cy.intercept('GET', '/api/session/1', {
                id: 1,
                name: 'Morning Yoga Flow',
                description: 'Start your day with a refreshing yoga session.',
                date: '2024-06-15',
                teacher_id: 2,
            }).as('session1');

            cy.intercept('PUT', '/api/session/1', {
                statusCode: 200,
                body: {
                    id: 1,
                    name: 'Evening Relaxation Yoga',
                    description: 'Unwind and relax with this calming yoga session.',
                    date: '2024-06-20',
                    teacher_id: 2,
                },
            }).as('updateSession');

            cy.contains('Edit').click();
            cy.wait('@session1');
            cy.get('input[formControlName=name]').clear().type('Evening Relaxation Yoga');
            cy.get('textarea[formControlName="description"]').clear().type('Unwind and relax with this calming yoga session.');
            cy.get('input[formControlName="date"]').clear().type('2024-06-20');
            cy.get('button[type="submit"]').click();
            cy.wait('@updateSession');
            cy.contains('Session updated').should('be.visible');
            cy.wait('@session');
        });
    });

    describe('View Session Details', () => {
        it('displays session details', () => {
            cy.intercept('GET', '/api/session/1', {
                statusCode: 200,
                body: {
                    id: 1,
                    name: 'Morning Yoga Flow',
                    description: 'Start your day with a refreshing yoga session.',
                    date: '2025-08-12T00:00:00.000+00:00',
                    teacher_id: 2,
                    users: [3],
                    createdAt: "2025-06-05T10:15:30",
                    updatedAt: "2025-07-10T14:45:00",
                },
            }).as('session');

            cy.contains('Detail').click();
            cy.wait('@session');

            cy.get('h1').should('contain', 'Morning Yoga Flow');
            cy.get('mat-card-content').should('contain', 'Start your day with a refreshing yoga session.');
            cy.get('span.ml1').should('contain', 'August 12, 2025');
            cy.get('span.ml1').should('contain', '1 attendee');
        });
    });

    describe('Delete Session', () => {
        it('deletes a session successfully', () => {
            cy.intercept('GET', '/api/session/1', {
                statusCode: 200,
                body: {
                    id: 1,
                    name: 'Morning Yoga Flow',
                    description: 'Start your day with a refreshing yoga session.',
                    date: '2025-08-12T00:00:00.000+00:00',
                    teacher_id: 2,
                    users: [3],
                    createdAt: "2025-06-05T10:15:30",
                    updatedAt: "2025-07-10T14:45:00",
                },
            }).as('session');

            cy.intercept('DELETE', '/api/session/1', {
                statusCode: 204,
            }).as('deleteSession');

            cy.contains('Detail').click();
            cy.wait('@session');

            cy.contains('Delete').click();
            cy.wait('@deleteSession');
            cy.contains('Session deleted').should('be.visible');
        });
    });
});
