package edu.ncsu.csc.iTrust2.services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.models.CPTCode;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.repositories.CPTCodeRepository;
import edu.ncsu.csc.iTrust2.repositories.CPTCodeRepository;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

/**
 * 
 * @author Tilak Patel
 *
 */
@Component
@Transactional
public class CPTCodeService extends Service<CPTCode, Long> {

    /** Repository for CRUD operations */
    @Autowired
    private CPTCodeRepository repository;

    /** Service for looking up Personnel */
    @Autowired
    private PersonnelService  service;

    @Override
    protected JpaRepository<CPTCode, Long> getRepository () {
        return repository;
    }

    /**
     * Finds an CPTCode object for the given Code
     * 
     * @param code
     *            Code of the CPTCode desired
     * @return CPTCode found, if any
     */
    public CPTCode findByCode ( final String code ) {
        return repository.findByCode( code );
    }

    @Override
    public List<CPTCode> findAll() {
        final User user = service.findByName( LoggerUtil.currentUser() );
        final Collection<Role> roles = user.getRoles();

        if ( roles.contains( Role.ROLE_BILLING_STAFF )
                || ( roles.contains( Role.ROLE_OPH ) && roles.contains( Role.ROLE_HCP ) ) ) {
            return repository.findAll();
        }
        return  null;

     
    }

}
