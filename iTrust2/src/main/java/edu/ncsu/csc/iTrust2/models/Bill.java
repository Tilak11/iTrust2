package edu.ncsu.csc.iTrust2.models;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.gson.annotations.JsonAdapter;

import edu.ncsu.csc.iTrust2.adapters.ZonedDateTimeAdapter;
import edu.ncsu.csc.iTrust2.adapters.ZonedDateTimeAttributeConverter;

/**
 * Represents a patient bill.
 *
 * @author Shruti Jadhav
 */
@Entity
public class Bill extends DomainObject {

    /** ID of this Bill */
    @Id
    private Long          id;

    /** Amount of bill */
    @Min ( 0 )
    private double        amount;

    /** Date of the bill is to start at */
    @NotNull
    @Basic
    // Allows the field to show up nicely in the database
    @Convert ( converter = ZonedDateTimeAttributeConverter.class )
    @JsonAdapter ( ZonedDateTimeAdapter.class )
    private ZonedDateTime date;

    /** How the bill is paid, cash, check, credit card, or insurance */
    private String        paymentMethod;

    /** Status of the bill payment, could be paid, unpaid, partially paid */
    private String        status;

    /** CPTCodes as string */
    private String        codesString;

    /** Patient this Bill is for */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "patient_id", columnDefinition = "varchar(100)" )
    private Patient       patient;

    /** Attending HCP during the appointment */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "hcp_id", columnDefinition = "varchar(100)" )
    private User          attendingHCP;

    /** List of CPT codes associated with the visit */
    @OneToMany ( cascade = CascadeType.ALL )
    @JsonManagedReference
    private List<CPTCode> CPTCodes;

    /** For Hibernate/Thymeleaf _must_ be an empty constructor */
    public Bill () {

    }

    /**
     * Bill Constructor
     *
     * @param amount
     * @param date
     * @param paymentMethod
     * @param status
     */
    public Bill ( final double amount, final ZonedDateTime date, final String paymentMethod, final String status,
            final List<CPTCode> codes ) {
        setTotal( amount );
        setDate( date );
        this.setPaymentMethod( paymentMethod );
        this.setBillStatus( status );
        this.setCPTCodes( codes );
        this.codesString = getCPTCodesString();
    }

    /**
     * Sets the Bill's unique id.
     *
     * @param id
     *            the bill id
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the id associated with the Bill.
     *
     * @return the bill id
     */
    @Override
    public final Long getId () {
        return id;
    }

    /**
     * Set new amount of bill
     *
     * @param amount
     */
    public void setTotal ( final double amount ) {
        this.amount = amount;
    }

    /**
     * Return amount of bill
     *
     * @return the amount
     */
    public double getTotal () {
        return amount;
    }

    /**
     * Get the date of this bill
     *
     * @return the date of this bill
     */
    public ZonedDateTime getDate () {
        return date;
    }

    /**
     * Set the date of this bill
     *
     * @param date
     *            the date to set this bill to
     */
    public void setDate ( final ZonedDateTime date ) {
        this.date = date;
    }

    /**
     * Set bill payment method
     *
     * @param paymentMethod
     */
    public void setPaymentMethod ( final String paymentMethod ) {
       // if ( paymentMethod.equals( "cash" ) || paymentMethod.equals( "check" ) || paymentMethod.equals( "credit card" )
       //         || paymentMethod.equals( "insurance" ) ) {
            this.paymentMethod = paymentMethod;
        //}
    }

    /**
     * Return bill payment method
     *
     * @return paymentMethod
     */
    public String getPaymentMethod () {
        return paymentMethod;
    }

    /**
     * Set status of bill
     *
     * @param bill
     *            status
     */
    public void setBillStatus ( final String status ) {
        if ( status.equals( "paid" ) || status.equals( "partially paid" ) || status.equals( "unpaid" ) ) {
            this.status = status;
        }
    }

    /**
     * Return amount of bill
     *
     * @return status of bill
     */
    public String getBillStatus () {
        return status;
    }

    /**
     * Returns the user associated with this bill.
     *
     * @return the patient
     */
    public Patient getPatient () {
        return patient;
    }

    /**
     * Sets the bill's patient to the given user
     *
     * @param user
     *            the user
     */
    public void setPatient ( final Patient user ) {
        this.patient = user;
    }

    /**
     * Set name of HCP
     *
     * @param HCP
     */
    public void setAttendingHCP ( final User hcp ) {
        this.attendingHCP = hcp;
    }

    /**
     * Return attending HCP
     *
     * @return name of attending HCP
     */
    public User getAttendingHCP () {
        return attendingHCP;
    }

    /**
     * Return list of CPTCodes
     *
     * @return list of CPT codes
     */
    public List<CPTCode> getCPTCodes () {
        return CPTCodes;
    }

    /**
     * Return list of CPTCodes
     *
     * @return list of CPT codes
     */
    public String getCPTCodesString () {
        final StringBuilder codes = new StringBuilder();
        for ( int i = 0; i < this.CPTCodes.size(); i++ ) {
            final String code = CPTCodes.get( i ).toString();
            codes.append( code );
            codes.append( "\n" );
        }
        return codes.toString();
    }

    /**
     * Add cpt code to list
     *
     * @return true if code is added
     */
    public void setCPTCodes ( final List<CPTCode> codes ) {
        this.CPTCodes = codes;
    }

}
