package edu.ncsu.csc.iTrust2.models.enums;

/**
 * For keeping track of various types of users that are known to the system.
 * Different users have different functionality.
 *
 * @author Kai Presler-Marshall
 * @author Jack MacDonald
 *
 */
public enum Role {

    /**
     * Patient
     */
    ROLE_PATIENT ( 1, "patient/index" ),

    /**
     * Billing staff
     */
     ROLE_BILLING_STAFF ( 1, "billingstaff/index" ),

    /**
     * HCP (general)
     */
    ROLE_HCP ( 2, "hcp/index" ),
    /**
     * Admin
     */
    ROLE_ADMIN ( 3, "admin/index" ),
    /**
     * Emergency responder
     */
    ROLE_ER ( 4, "er/index" ),
    /**
     * Lab Tech
     */
    ROLE_LABTECH ( 5, "labtech/index" ),
    /**
     * Optometrist
     */
    ROLE_OD ( 6, "hcp/index" ),
    /**
     * Ophthalmologist
     */
    ROLE_OPH ( 7, "hcp/index" ),
    /**
     * Virologist
     */
    ROLE_VIROLOGIST ( 8, "hcp/index" ),
    /**
     * Vaccinator
     */
    ROLE_VACCINATOR ( 9, "vaccinator/index" );

    /**
     * Numeric code of the Role
     */
    private int    code;

    /**
     * Landing screen the User should be shown when logging in
     */
    private String landingPage;

    /**
     * Create a Role from a code and landing screen.
     *
     * @param code
     *            Code of the Role.
     * @param landingPage
     *            Landing page (upon logging in) for the User
     */
    private Role ( final int code, final String landingPage ) {
        this.code = code;
        this.landingPage = landingPage;
    }

    /**
     * Gets the numeric code of the Role
     *
     * @return Code of this role
     */
    public int getCode () {
        return this.code;
    }

    /**
     * Gets the landing page for this user
     *
     * @return Landing page for the user
     */
    public String getLanding () {
        return this.landingPage;
    }

}
