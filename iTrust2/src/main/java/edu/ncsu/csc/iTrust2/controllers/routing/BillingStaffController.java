package edu.ncsu.csc.iTrust2.controllers.routing;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.ncsu.csc.iTrust2.models.enums.Role;

/**
 * Controller to manage basic abilities for Billing Staff roles
 *
 * @author rdudhat
 *
 */
public class BillingStaffController {

    /**
     * Returns the billing memeber for the given model
     *
     * @param model
     *            model to check
     * @return role
     */
    @RequestMapping ( value = "billingstaff/index" )
    @PreAuthorize ( "hasRole('ROLE_BILLING_STAFF')" )
    public String index ( final Model model ) {
        return Role.ROLE_BILLING_STAFF.getLanding();
    }

    /**
     * Returns the view bills page for the given model
     *
     * @param model
     *            model to check
     * @return view bills html page
     */
    @RequestMapping ( value = "billingstaff/viewbills" )
    @PreAuthorize ( "hasRole('ROLE_BILLING_STAFF')" )
    public String viewBills ( final Model model ) {
        return "/billingstaff/viewbills";
    }

    /**
     * Returns the pay bills page for the given model
     *
     * @param model
     *            model to check
     * @return pay bills html page
     */
    @RequestMapping ( value = "billingstaff/paybills" )
    @PreAuthorize ( "hasRole('ROLE_BILLING_STAFF')" )
    public String payBills ( final Model model ) {
        return "/billingstaff/paybills";
    }

    /**
     * Returns the maintain CPT code page for the given model
     *
     * @param model
     *            model to check
     * @return view maintain CPT Code html page
     */
    @RequestMapping ( value = "billingstaff/maintainCPTCodes" )
    @PreAuthorize ( "hasRole('ROLE_BILLING_STAFF')" )
    public String maintainCPTCodes ( final Model model ) {
        return "/billingstaff/maintainCPTCodes";
    }

}
