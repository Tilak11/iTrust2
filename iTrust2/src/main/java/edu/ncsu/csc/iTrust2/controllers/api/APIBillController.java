package edu.ncsu.csc.iTrust2.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.iTrust2.forms.OfficeVisitForm;
import edu.ncsu.csc.iTrust2.models.Bill;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.services.BillService;
import edu.ncsu.csc.iTrust2.services.UserService;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

/**
 * API controller for interacting with the OfficeVisit model. Provides standard
 * CRUD routes as appropriate for different user types
 *
 * @author Shruti Jadhav
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIBillController extends APIController {

    /** OfficeVisit service */
    @Autowired
    private BillService       billService;

    /** User service */
    @Autowired
    private UserService<User> userService;

    /** LoggerUtil */
    @Autowired
    private LoggerUtil        loggerUtil;

    /**
     * Retrieves a list of all bills in the database
     *
     * @return list of bills
     */
    @GetMapping ( BASE_PATH + "/bills" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP') or hasAnyRole('ROLE_BILLING_STAFF')" )
    public List<Bill> getBills () {
        return billService.findAll();
    }

    /**
     * Retrieves a list of all bills in the database for the current patient
     *
     * @return list of bills
     */
    @GetMapping ( BASE_PATH + "/bills/mybills" )
    @PreAuthorize ( "hasAnyRole('ROLE_PATIENT')" )
    public List<Bill> getBillsForPatient () {
        final User self = userService.findByName( LoggerUtil.currentUser() );
        return billService.findByPatient( self );
    }

    /**
     * Retrieves a specific Bill in the database, with the given ID
     *
     * @param id
     *            ID of the bill to retrieve
     * @return list of bills
     */
    @GetMapping ( BASE_PATH + "/bills/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_BILLING_STAFF')" )
    public ResponseEntity getBill ( @PathVariable final Long id ) {
        if ( !billService.existsById( id ) ) {
            return new ResponseEntity( HttpStatus.NOT_FOUND );
        }

        return new ResponseEntity( billService.findById( id ), HttpStatus.OK );
    }

    /**
     * Creates and saves a new Bill from the RequestBody provided.
     *
     * @param visitForm
     *            The office visit to be validated and saved
     * @return response
     */
    @PostMapping ( BASE_PATH + "/bills" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP') or hasAnyRole('ROLE_BILLING_STAFF')" )
    public ResponseEntity createBill ( @RequestBody final OfficeVisitForm ovf ) {
        try {
            ovf.setHcp( LoggerUtil.currentUser() );
            final Bill bill = billService.build( ovf );
            if ( null != bill.getId() && billService.existsById( bill.getId() ) ) {
                return new ResponseEntity(
                        errorResponse( "Office visit with the id " + bill.getId() + " already exists" ),
                        HttpStatus.CONFLICT );
            }
            billService.save( bill );
            return new ResponseEntity( bill, HttpStatus.OK );

        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not validate or save the Bill provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Creates and saves a new bill from the RequestBody provided.
     *
     * @param id
     *            ID of the bill to update
     * @param visitForm
     *            The bill to be validated and saved
     * @return response
     */
    @PutMapping ( BASE_PATH + "/bills/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP') or hasAnyRole('ROLE_BILLING_STAFF')" )
    public ResponseEntity updateBill ( @PathVariable final Long id, @RequestBody final OfficeVisitForm ovf ) {
        try {
            final Bill bill = billService.build( ovf );

            if ( null == bill.getId() || !billService.existsById( bill.getId() ) ) {
                return new ResponseEntity( errorResponse( "Bill with the id " + bill.getId() + " doesn't exist" ),
                        HttpStatus.NOT_FOUND );
            }
            billService.save( bill );

            return new ResponseEntity( bill, HttpStatus.OK );

        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not validate or save the Bill provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

}
