package edu.ncsu.csc.iTrust2.forms;

import java.util.Scanner;

import edu.ncsu.csc.iTrust2.models.CPTCode;

/**
 * Intermediate form for adding or editing ICDCodes. Used to create and
 * serialize ICDCodes.
 *
 * @author Thomas
 * @author Kai Presler-Marshall
 *
 */
public class CPTCodeForm {

    /** The code of the Diagnosis */
    private String code;
    /** The description of the diagnosis */
    private String description;
    /** ID of the ICDCode */
    private Long   id;
    /**
     * If ICD code is ophtalmology-related
     */
    private Double cost;

    /**
     * Time frame for the CPT code
     */
    private String timeFrame;
    
    private String deleted;

    

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

    /**
     * Empty constructor for GSON
     */
    public CPTCodeForm () {

    }

    /**
     * Construct a form off an existing ICDCode object
     *
     * @param code
     *            The object to fill this form with
     */
    public CPTCodeForm ( final CPTCode code ) {
        setCode( code.getCode() );
        setDescription( code.getDescription() );
        setId( code.getId() );
        setCost( code.getCost() );
        setTimeFrame( code.getTimeFrame() );
        setDeleted(code.getDeleted());
    }

    /**
     * Sets the time range of the Code
     *
     * @param timeFrame
     *            for the code
     *
     */
    public void setTimeFrame ( final String timeFrame ) {
        // scans the time string to check if the starting t1 > t2 or if its
        // negative. Should be in format (int-int minutes)
        final Scanner s = new Scanner( timeFrame );
        final String time = s.next();
        final String timeArr[] = time.split( "-", 2 );
        final String timeUnit = s.next();
        final int t1;
        final int t2;
        try {
            t1 = Integer.parseInt( timeArr[0] );
            t2 = Integer.parseInt( timeArr[1] );

        }
        catch ( final Exception e ) {
            s.close();
            throw new IllegalArgumentException( "Time range must contain integer for time" );
        }
        if ( t1 > t2 || t1 <= 0 || t2 <= 0 || ( !timeUnit.equals( "hours" ) && !timeUnit.equals( "minutes" ) ) ) {
            s.close();
            throw new IllegalArgumentException(
                    "Time format should be in format [int-int minutes] OR [int-int hours]" );
        }
        s.close();
        this.timeFrame = timeFrame;
    }

    /**
     * Return the time frame of the CPT code
     *
     * @return time frame as String for CPT code
     */
    public String getTimeFrame () {
        return this.timeFrame;
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
        if ( c.length != 5 ) {
            throw new IllegalArgumentException( "Code must be at least five characters and all integers: " + code );
        }
        for ( int i = 0; i < c.length; i++ ) {
            if ( !Character.isDigit( c[i] ) ) {
                throw new IllegalArgumentException( "Code must contain all integers and must be of length 5: " + code );
            }
        }
        this.code = code;
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
     * Sets the description of this code
     *
     * @param d
     *            The new description
     */
    public void setDescription ( final String d ) {
        // validate
        if ( d.length() > 250 ) {
            throw new IllegalArgumentException( "Description too long (250 characters max)" );
        }
        description = d;
    }

    /**
     * Returns the description of the code
     *
     * @return The description
     */
    public String getDescription () {
        return description;
    }

    /**
     * Sets the ID of the Code
     *
     * @param l
     *            The new ID of the Code. For Hibernate.
     */
    public void setId ( final Long l ) {
        id = l;
    }

    /**
     * Returns the ID of the Diagnosis
     *
     * @return The diagnosis ID
     */
    public Long getId () {
        return id;
    }

    /**
     * Returns the ICDCodeForm's isOphthalmology
     *
     * @return the isOphthalmology
     */
    public Double getCost () {
        return this.cost;
    }

    /**
     * Sets the CPTCodeForm's cost
     *
     * @param cost
     *            the cost to set
     */
    public void setCost ( final Double cost ) {
        if ( cost < 0 ) {
            throw new IllegalArgumentException( "CPT Code Cost cannot be negative" );
        }
        this.cost = cost;
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( code == null ) ? 0 : code.hashCode() );
        result = prime * result + ( ( cost == null ) ? 0 : cost.hashCode() );
        result = prime * result + ( ( description == null ) ? 0 : description.hashCode() );
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        result = prime * result + ( ( timeFrame == null ) ? 0 : timeFrame.hashCode() );
        return result;
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
        final CPTCodeForm other = (CPTCodeForm) obj;
        if ( code == null ) {
            if ( other.code != null ) {
                return false;
            }
        }
        else if ( !code.equals( other.code ) ) {
            return false;
        }
        if ( cost == null ) {
            if ( other.cost != null ) {
                return false;
            }
        }
        else if ( !cost.equals( other.cost ) ) {
            return false;
        }
        if ( description == null ) {
            if ( other.description != null ) {
                return false;
            }
        }
        else if ( !description.equals( other.description ) ) {
            return false;
        }
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        }
        else if ( !id.equals( other.id ) ) {
            return false;
        }
        if ( timeFrame == null ) {
            if ( other.timeFrame != null ) {
                return false;
            }
        }
        else if ( !timeFrame.equals( other.timeFrame ) ) {
            return false;
        }
        return true;
    }

    // @Override
    // public int hashCode () {
    // final int prime = 31;
    // int result = 1;
    // result = prime * result + ( ( code == null ) ? 0 : code.hashCode() );
    // result = prime * result + ( ( description == null ) ? 0 :
    // description.hashCode() );
    // result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
    // result = prime * result + ( ( cost == null ) ? 0 : cost.hashCode() );
    // return result;
    // }

}
