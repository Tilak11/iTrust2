package edu.ncsu.csc.iTrust2.services;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.forms.CPTCodeForm;
import edu.ncsu.csc.iTrust2.forms.OfficeVisitForm;
import edu.ncsu.csc.iTrust2.models.Bill;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.repositories.BillRepository;

/**
 * Service class for interacting with OfficeVisit model, performing CRUD tasks
 * with database and building a persistence object from a Form.
 *
 * @author Shruti Jadhav
 *
 */
@Component
@Transactional
public class BillService extends Service<Bill, Long> {

    /**
     * Repository for CRUD operations
     */
    @Autowired
    private BillRepository     repository;

    /**
     * User service
     */
    @Autowired
    private UserService<User>  userService;

    /**
     * cpt service
     */
    @Autowired
    private CPTCodeService     cptService;

    /**
     * office visit service
     */
    @Autowired
    private OfficeVisitService officevisitService;

    @Override
    protected JpaRepository<Bill, Long> getRepository () {
        return repository;
    }

    /**
     * Finds all Bills for the specified Patient
     *
     * @param patient
     *            Patient to search for
     * @return Matching Bills
     */
    public List<Bill> findByPatient ( final User patient ) {
        return repository.findByPatient( patient );
    }

    /**
     * Builds an OfficeVisit based on the deserialised BillForm
     *
     * @param ovf
     *            Form to build from
     * @return Constructed Bill
     */
    public Bill build ( final Long id, final OfficeVisitForm ovf ) {
        final Bill b = new Bill();
        System.out.println( "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" );
        b.setPatient( (Patient) userService.findByName( ovf.getPatient() ) );
        b.setAttendingHCP( userService.findByName( ovf.getHcp() ) );

       // if ( ovf.getId() != null ) {
            b.setId( id );
        //}

        final ZonedDateTime visitDate = ZonedDateTime.parse( ovf.getDate() );
        b.setDate( visitDate );

        final List<CPTCodeForm> cpt = ovf.getCodes();
        if ( cpt != null ) {
            b.setCPTCodes( cpt.stream().map( cptService::build ).collect( Collectors.toList() ) );
        }

        b.setBillStatus( ovf.getBillStatus() );
        b.setTotal( ovf.getTotal() );
        b.setPaymentMethod( ovf.getPaymentType() );

        final Patient p = b.getPatient();
        if ( p == null || p.getDateOfBirth() == null ) {
            return b; // we're done, patient can't be tested against
        }
        final LocalDate dob = p.getDateOfBirth();
        int age = b.getDate().getYear() - dob.getYear();
        // Remove the -1 when changing the dob to OffsetDateTime
        if ( b.getDate().getMonthValue() < dob.getMonthValue() ) {
            age -= 1;
        }
        else if ( b.getDate().getMonthValue() == dob.getMonthValue() ) {
            if ( b.getDate().getDayOfMonth() < dob.getDayOfMonth() ) {
                age -= 1;
            }
        }

        return b;
    }

}
