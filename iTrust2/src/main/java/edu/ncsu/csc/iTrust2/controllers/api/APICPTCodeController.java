package edu.ncsu.csc.iTrust2.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.iTrust2.forms.CPTCodeForm;
import edu.ncsu.csc.iTrust2.models.CPTCode;
import edu.ncsu.csc.iTrust2.models.enums.TransactionType;
import edu.ncsu.csc.iTrust2.services.CPTCodeService;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;
/**
 * 
 * @author Tilak Patel
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APICPTCodeController extends APIController {
	/** LoggerUtil */
    @Autowired
    private LoggerUtil     loggerUtil;

    /** CPTCode service */
    @Autowired
    private CPTCodeService service;

    /**
     * Returns a list of Codes in the system
     *
     * @return All the codes in the system
     */
    @GetMapping ( BASE_PATH + "/cptcodes" )
    public List<CPTCode> getCodes () {
       // loggerUtil.log( TransactionType.CPT_VIEW_ALL, LoggerUtil.currentUser(), "Fetched cpt codes" );
        return service.findAll();
    }

    /**
     * Returns the code with the given ID
     *
     * @param id
     *            The ID of the code to retrieve
     * @return The requested Code
     */
    @GetMapping ( BASE_PATH + "/cptcode/{id}" )
    public ResponseEntity getCode ( @PathVariable ( "id" ) final Long id ) {
        try {
            final CPTCode code = service.findById( id );
            if ( code == null ) {
                return new ResponseEntity( errorResponse( "No code with id " + id ), HttpStatus.NOT_FOUND );
            }
         //   loggerUtil.log( TransactionType.CPT_VIEW, LoggerUtil.currentUser(), "Fetched cpt code with id " + id );
            return new ResponseEntity( code, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not retrieve CPT Code " + id + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Updates the code with the specified ID to the value supplied.
     *
     * @param id
     *            The ID of the code to edit
     * @param form
     *            The new values for the Code
     * @return The Response of the action
     */
    @PutMapping ( BASE_PATH + "/cptcode/{id}" )
    @PreAuthorize ( "hasRole('ROLE_BILLING_STAFF')" )
    public ResponseEntity updateCode ( @PathVariable ( "id" ) final Long id, @RequestBody final CPTCodeForm form ) {
        try {
            if ( !service.existsById( id ) ) {
                return new ResponseEntity( "No code with id " + id, HttpStatus.NOT_FOUND );
            }
            form.setId( id );
            final CPTCode code = new CPTCode( form );
            service.save( code );
          //  loggerUtil.log( TransactionType.CPT_EDIT, LoggerUtil.currentUser(),
             //       LoggerUtil.currentUser() + " edited an CPT Code" );

            return new ResponseEntity( code, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not update CPT Code " + id + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Adds a new code to the system
     *
     * @param form
     *            The data for the new Code
     * @return The result of the action
     */
    @PostMapping ( BASE_PATH + "/cptcodes" )
    @PreAuthorize ( "hasRole('ROLE_BILLING_STAFF')" )
    public ResponseEntity addCode ( @RequestBody final CPTCodeForm form ) {
        try {
            final CPTCode code = new CPTCode( form );
            service.save( code );
          //  loggerUtil.log( TransactionType.CPT_CREATE, LoggerUtil.currentUser(),
               //     LoggerUtil.currentUser() + " created an CPT Code" );

            return new ResponseEntity( code, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not create CPT Code " + form.getCode() + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Deletes a code from the system.
     *
     * @param id
     *            The ID of the code to delete
     * @return The result of the action.
     */
    @DeleteMapping ( BASE_PATH + "/cptcode/{id}" )
    @PreAuthorize ( "hasRole('ROLE_BILLING_STAFF')" )
    public ResponseEntity deleteCode ( @PathVariable ( "id" ) final Long id ) {
        try {
            final CPTCode code = service.findById( id );
            service.delete( code );
          //  loggerUtil.log( TransactionType.CPT_DELETE, LoggerUtil.currentUser(),
           //         LoggerUtil.currentUser() + " deleted an CPT Code" );

            return new ResponseEntity( HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not delete CPT Code " + id + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

}
