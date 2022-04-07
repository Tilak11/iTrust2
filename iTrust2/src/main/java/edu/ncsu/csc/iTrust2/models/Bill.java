package edu.ncsu.csc.iTrust2.models;

import java.time.LocalDate;
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

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

import com.google.gson.annotations.JsonAdapter;

import edu.ncsu.csc.iTrust2.adapters.LocalDateAdapter;

/**
 * Represents a patient bill.
 *
 * @author Shruti Jadhav
 */
@Entity
public class Bill extends DomainObject {

    /** ID of this Bill */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long          id;

    /** Amount of bill */
    @Min ( 0 )
    private double        amount;

    /** Date of the bill is to start at */
    @NotNull
    @Basic
    // Allows the field to show up nicely in the database
    @Convert ( converter = LocalDateConverter.class )
    @JsonAdapter ( LocalDateAdapter.class )
    private LocalDate     date;

    /** How the bill is paid, cash, check, credit card, or insurance */
    private String        paymentMethod;

    /** Status of the bill payment, could be paid, unpaid, partially paid */
    private String        status;

    /** Patient this Bill is for */
    @NotNull
    @ManyToOne ( cascade = CascadeType.ALL )
    @JoinColumn ( name = "patient_id", columnDefinition = "varchar(100)" )
    private User          patient;

    /** Attending HCP during the appointment */
    private String        attendingHCP;

    /** List of CPT codes associated with the visit */
    @OneToMany
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
    public Bill ( final double amount, final LocalDate date, final String paymentMethod, final String status ) {
        this.amount = amount;
        this.date = date;
        this.paymentMethod = paymentMethod;
        this.status = status;
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
    public Long getId () {
        return id;
    }

    /**
     * Set new amount of bill
     *
     * @param amount
     */
    public void setAmount ( final double amount ) {
        this.amount = amount;
    }

    /**
     * Return amount of bill
     *
     * @return the amount
     */
    public double getAmount () {
        return amount;
    }

    /**
     * Set date of bill
     *
     * @param date
     */
    public void setDate ( final LocalDate date ) {
        this.date = date;
    }

    /**
     * Return date of bill
     *
     * @return date
     */
    public LocalDate getDate () {
        return date;
    }

    /**
     * Set bill payment method
     *
     * @param paymentMethod
     */
    public void setPaymentMethod ( final String paymentMethod ) {
        if ( paymentMethod.equals( "cash" ) || paymentMethod.equals( "check" ) || paymentMethod.equals( "credit card" )
                || paymentMethod.equals( "insurance" ) ) {
            this.paymentMethod = paymentMethod;
        }
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
    public void setStatus ( final String status ) {
        if ( status.equals( "paid" ) || status.equals( "partially paid" ) || status.equals( "unpaid" ) ) {
            this.status = status;
        }
    }

    /**
     * Return amount of bill
     *
     * @return status of bill
     */
    public String getStatus () {
        return status;
    }

    /**
     * Returns the user associated with this bill.
     *
     * @return the patient
     */
    public User getPatient () {
        return patient;
    }

    /**
     * Sets the bill's patient to the given user
     *
     * @param user
     *            the user
     */
    public void setPatient ( final User user ) {
        this.patient = user;
    }

    /**
     * Set name of HCP
     *
     * @param HCP
     */
    public void setAttendingHCP ( final String hcp ) {
        this.attendingHCP = hcp;
    }

    /**
     * Return attending HCP
     *
     * @return name of attending HCP
     */
    public String getAttendingHCP () {
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
     * Add cpt code to list
     *
     * @return true if code is added
     */
    public boolean addCPTCode ( final CPTCode cpt ) {
        for ( int i = 0; i < CPTCodes.size(); i++ ) {
            if ( CPTCodes.get( i ) == cpt ) {
                return false;
            }
        }
        CPTCodes.add( cpt );
        return true;
    }

}
