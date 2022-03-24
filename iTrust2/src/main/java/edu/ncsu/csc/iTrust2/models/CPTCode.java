package edu.ncsu.csc.iTrust2.models;

import java.util.Objects;
import java.util.Scanner;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Class for CPT codes. These codes themselves are stored as a String, along
 * with a description and an ID.
 *
 * @author rdudhat
 *
 */
@Entity
public class CPTCode extends DomainObject {

    /**
     * ID of this CPTCode
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long    id;

    /**
     * The CPT Code string
     */
    private String  code;
    /**
     * Description of the diagnosis
     */
    private String  description;

    /**
     * Time Range of the CPT Code
     */
    private String  timeRange;

    /**
     * Cost of the CPT code
     */
    private Integer cost;

    /**
     * Empty constructor for Hibernate
     */
    public CPTCode () {

    }

    /**
     * Construct from a form
     *
     * @param form
     *            The form that validates and sanitizes input
     */
    public CPTCode ( final CPTCodeForm form ) {
        setCode( form.getCode() );
        setDescription( form.getDescription() );
        setId( form.getId() );
        setCost( form.getCost() );
        setTimeRange( form.getTimeFrame() );

    }

    /**
     * Sets the ID of the Code
     *
     * @param id
     *            The new ID of the Code. For Hibernate.
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Sets the time range of the Code
     *
     * @param timeFrame
     *            for the code
     *
     */
    public void setTimeRange ( final String timeFrame ) {
        // scans the time string to check if the starting t1 > t2 or if its
        // negative. Should be in format (int-int minutes)
        final Scanner s = new Scanner( timeFrame );
        final String time = s.next();
        final String timeArr[] = time.split( "-", 2 );
        try {
            final int t1 = Integer.parseInt( timeArr[0] );
            final int t2 = Integer.parseInt( timeArr[1] );
            if ( ( t1 > t2 ) || t1 <= 0 || t2 <= 0 ) {
                s.close();
                throw new IllegalArgumentException( "Time format should be in format (int-int minutes)" );
            }

        }
        catch ( final Exception e ) {
            s.close();
            throw new IllegalArgumentException( "Time range must contain integer for time" );
        }
        s.close();
        this.timeRange = timeFrame;
    }

    /**
     * Returns the String representation of the code
     *
     * @return The code
     */
    public String getCode () {
        return code;
    }

    /**
     * Sets the String representation of the code
     *
     * @param code
     *            The new code
     */
    public void setCode ( final String code ) {
        // code XYY where X is a capital alphabetic letter and Y is a number
        // from 0-9. Code can also contain up to three decimal places
        final char[] c = code.toCharArray();
        if ( c.length < 5 ) {
            throw new IllegalArgumentException( "Code must be at least five characters and all integers: " + code );
        }
        for ( int i = 0; i < c.length; i++ ) {
            if ( Character.isLetter( c[i] ) ) {
                throw new IllegalArgumentException( "Code must contain all integers and must be of length 5: " + code );
            }
        }
        this.code = code;
    }

    /**
     * Returns the description of the code
     *
     * @return The description
     */
    public String getDescription () {
        return this.description;
    }

    /**
     * Sets the description of this code
     *
     * @param d
     *            The new description
     */
    public void setDescription ( final String d ) {
        // validate
        if ( d.length() > 250 ) {
            throw new IllegalArgumentException( "Description too long (250 characters max): " + description );
        }
        description = d;
    }

    /**
     * Returns the ICDCode's isOphthalmology
     *
     * @return the isOphthalmology
     */
    public Integer getCost () {
        return this.cost;
    }

    /**
     * Sets the ICDCode's isOphthalmology
     *
     * @param isOphthalmology
     *            the isOphthalmology to set
     */
    public void setCost ( final Integer cost ) {
        if ( cost < 0 ) {
            throw new IllegalArgumentException( "CPT Code Cost cannot be negative" );
        }
        this.cost = cost;
    }

    /**
     * Returns the ICDCode's id
     *
     * @return the id
     */
    @Override
    public Long getId () {
        return id;
    }

    @Override
    public int hashCode () {
        return Objects.hash( code, description, id, cost );
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final CPTCode other = (CPTCode) obj;
        return Objects.equals( code, other.code ) && Objects.equals( description, other.description )
                && Objects.equals( id, other.id ) && Objects.equals( cost, other.cost );
    }

}
