package edu.ncsu.csc.iTrust2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.models.CPTCode;

public interface CPTCodeRepository extends JpaRepository<CPTCode, Long>  {
	/**
     * Finds an CPTCode by the provided code
     * 
     * @param code
     *            Code to search by
     * @return Matching code, if any
     */
    public CPTCode findByCode ( String code );
}
