package edu.ncsu.csc.iTrust2.models;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.gson.annotations.JsonAdapter;

import edu.ncsu.csc.iTrust2.adapters.ZonedDateTimeAdapter;
import edu.ncsu.csc.iTrust2.adapters.ZonedDateTimeAttributeConverter;
import edu.ncsu.csc.iTrust2.models.enums.AppointmentType;
import edu.ncsu.csc.iTrust2.utils.ValidationUtil;

/**
 * This is the validated database-persisted office visit representation
 *
 * @author Kai Presler-Marshall
 *
 */
@Entity
public class OfficeVisit extends DomainObject {

    /**
     * List of active CPT codes
     */
    @OneToMany ( cascade = CascadeType.ALL )
    @JsonManagedReference
    private List<CPTCode>        codes;

    /**
     * The patient of this office visit
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "patient_id", columnDefinition = "varchar(100)" )
    private User                 patient;

    /**
     * The hcp of this office visit
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "hcp_id", columnDefinition = "varchar(100)" )
    private User                 hcp;

    /**
     * The basic health metric data associated with this office visit.
     */
    @OneToOne ( cascade = CascadeType.ALL )
    @JoinColumn ( name = "basichealthmetrics_id" )
    private BasicHealthMetrics   basicHealthMetrics;

    /**
     * The Satisfaction Survey associated with this office visit.
     */
    @OneToOne ( cascade = CascadeType.ALL )
    @JoinColumn ( name = "satisfactionsurvey_id" )
    private SatisfactionSurvey   satisfactionSurvey;

    /**
     * The date of this office visit
     */
    @NotNull
    @Basic
    // Allows the field to show up nicely in the database
    @Convert ( converter = ZonedDateTimeAttributeConverter.class )
    @JsonAdapter ( ZonedDateTimeAdapter.class )
    private ZonedDateTime        date;

    /**
     * The id of this office visit
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long                 id;

    /**
     * The type of this office visit
     */
    @NotNull
    @Enumerated ( EnumType.STRING )
    private AppointmentType      type;

    /**
     * The hospital of this office visit
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "hospital_id", columnDefinition = "varchar(100)" )
    private Hospital             hospital;

    /**
     * The set of diagnoses associated with this visits Marked transient so not
     * serialized or saved in DB If removed, serializer gets into an infinite
     * loop
     */
    @OneToMany ( cascade = CascadeType.ALL )
    @JsonManagedReference
    private List<Diagnosis>      diagnoses;

    /**
     * The notes of this office visit
     */
    private String               notes;

    /**
     * The appointment of this office visit
     */
    @OneToOne
    @JoinColumn ( name = "appointment_id" )
    private AppointmentRequest   appointment;

    /**
     * Prescriptions associated with this OfficeVisit
     */
    @OneToMany ( cascade = CascadeType.ALL )
    @JsonManagedReference
    private List<Prescription>   prescriptions;
    
    
    public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	private String billStatus;
    
    private String paymentDate;
    
    private double total;
    
    private String paymentType;


	/**
     * The ophthalmology metric data associated with this office visit.
     */
    @OneToOne ( cascade = CascadeType.ALL )
    @JoinColumn ( name = "ophthalmologymetrics_id" )
    private OphthalmologyMetrics ophthalmologyMetrics;

    /** For Hibernate/Thymeleaf _must_ be an empty constructor */
    public OfficeVisit () {
    }

    /**
     * Validates the diagnoses attached to this visit. If there are none, no
     * validation is performed. Otherwise, makes sure each Diagnosis is legal
     * for the type of visit.
     */
    public void validateDiagnoses () {
        if ( diagnoses == null ) {
            return;
        }
        for ( final Diagnosis d : diagnoses ) {
            if ( d.getNote().length() > 500 ) {
                throw new IllegalArgumentException( "Diagnosis note too long (500 character max) : " + d.getNote() );
            }
            if ( d.getCode() == null ) {
                throw new IllegalArgumentException( "Diagnosis Code missing!" );
            }

            // if the visit is for ophtalmology
            if ( type == AppointmentType.GENERAL_OPHTHALMOLOGY || type == AppointmentType.OPHTHALMOLOGY_SURGERY ) {

                if ( d.getCode() != null
                        && ( d.getCode().getIsOphthalmology() == null || !d.getCode().getIsOphthalmology() ) ) {
                    throw new IllegalArgumentException(
                            "The diagnosis " + d.getCode().getId() + " is not for an ophtalmologic ICD Code." );
                }

            }
        }
    }

    /**
     * Execute proper validations for the ophthalmology metrics
     */
    public void validateOphthalmology () {
        if ( ophthalmologyMetrics == null ) {
            return;
        }

        if ( type == AppointmentType.GENERAL_CHECKUP ) {
            throw new IllegalArgumentException( "Ophthalmology metrics can't be entered in a general checkup." );
        }

        ValidationUtil.validate( ophthalmologyMetrics );
    }

    /**
     * Validates an office visit form for containing correct fields for patients
     * 12 and over. Expects the basic health metrics to already be set by the
     * OfficeVisit constructor.
     */
    public void validate12AndOver () {
        // should already be set in office visit constructor
        final BasicHealthMetrics bhm = getBasicHealthMetrics();
        if ( bhm.getDiastolic() == null || bhm.getHdl() == null || bhm.getHeight() == null
                || bhm.getHouseSmokingStatus() == null || bhm.getLdl() == null || bhm.getPatientSmokingStatus() == null
                || bhm.getSystolic() == null || bhm.getTri() == null || bhm.getWeight() == null ) {
            throw new IllegalArgumentException( "Not all necessary fields for basic health metrics were submitted." );
        }
    }

    /**
     * Validates an office visit form for containing correct fields for patients
     * 12 and under.
     */
    public void validateUnder12 () {
        // should already be set in office visit constructor
        final BasicHealthMetrics bhm = getBasicHealthMetrics();
        if ( bhm.getDiastolic() == null || bhm.getHeight() == null || bhm.getHouseSmokingStatus() == null
                || bhm.getSystolic() == null || bhm.getWeight() == null ) {
            throw new IllegalArgumentException( "Not all necessary fields for basic health metrics were submitted." );
        }
    }

    /**
     * Validates an office visit form for containing correct fields for patients
     * 3 and under.
     */
    public void validateUnder3 () {
        // should already be set in office visit constructor
        final BasicHealthMetrics bhm = getBasicHealthMetrics();
        if ( bhm.getHeight() == null || bhm.getHeadCircumference() == null || bhm.getHouseSmokingStatus() == null
                || bhm.getWeight() == null ) {
            throw new IllegalArgumentException( "Not all necessary fields for basic health metrics were submitted." );
        }
    }

    /**
     * Get the patient of this office visit
     *
     * @return the patient of this office visit
     */
    public User getPatient () {
        return patient;
    }

    /**
     * Set the patient of this office visit
     *
     * @param patient
     *            the patient to set this office visit to
     */
    public void setPatient ( final User patient ) {
        this.patient = patient;
    }

    /**
     * Get the hcp of this office visit
     *
     * @return the hcp of this office visit
     */
    public User getHcp () {
        return hcp;
    }

    /**
     * Set the hcp of this office visit
     *
     * @param hcp
     *            the hcp to set this office visit to
     */
    public void setHcp ( final User hcp ) {
        this.hcp = hcp;
    }

    /**
     * Get the date of this office visit
     *
     * @return the date of this office visit
     */
    public ZonedDateTime getDate () {
        return date;
    }

    /**
     * Set the date of this office visit
     *
     * @param date
     *            the date to set this office visit to
     */
    public void setDate ( final ZonedDateTime date ) {
        this.date = date;
    }

    /**
     * Get the id of this office visit
     *
     * @return the id of this office visit
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the id of this office visit
     *
     * @param id
     *            the id to set this office visit to
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Get the type of this office visit
     *
     * @return the type of this office visit
     */
    public AppointmentType getType () {
        return type;
    }

    /**
     * Set the type of this office visit
     *
     * @param type
     *            the type to set this office visit to
     */
    public void setType ( final AppointmentType type ) {
        this.type = type;
    }

    /**
     * Get the hospital of this office visit
     *
     * @return the hospital of this office visit
     */
    public Hospital getHospital () {
        return hospital;
    }

    /**
     * Set the hospital of this office visit
     *
     * @param hospital
     *            the hospital to set this office visit to
     */
    public void setHospital ( final Hospital hospital ) {
        this.hospital = hospital;
    }

    /**
     * Get the notes of this office visit
     *
     * @return the notes of this office visit
     */
    public String getNotes () {
        return notes;
    }

    /**
     * Set the notes of this office visit
     *
     * @param notes
     *            the notes to set this office visit to
     */
    public void setNotes ( final String notes ) {
        this.notes = notes;
    }

    /**
     * Get the appointment of this office visit
     *
     * @return the appointment of this office visit
     */
    public AppointmentRequest getAppointment () {
        return appointment;
    }

    /**
     * Set the appointment of this office visit
     *
     * @param appointment
     *            the appointment to set this office visit to
     */
    public void setAppointment ( final AppointmentRequest appointment ) {
        this.appointment = appointment;
    }

    /**
     * Gets the basic health metrics for this office visit.
     *
     * @return the basicHealthMetrics
     */
    public BasicHealthMetrics getBasicHealthMetrics () {
        return basicHealthMetrics;
    }

    /**
     * Sets the basic health metrics for this office visit.
     *
     * @param basicHealthMetrics
     *            the basicHealthMetrics to set
     */
    public void setBasicHealthMetrics ( final BasicHealthMetrics basicHealthMetrics ) {
        this.basicHealthMetrics = basicHealthMetrics;
    }

    /**
     * Returns the OfficeVisit's ophthalmologyMetrics
     *
     * @return the ophthalmologyMetrics
     */
    public OphthalmologyMetrics getOphthalmologyMetrics () {
        return ophthalmologyMetrics;
    }

    /**
     * Sets the OfficeVisit's ophthalmologyMetrics
     *
     * @param ophthalmologyMetrics
     *            the ophthalmologyMetrics to set
     */
    public void setOphthalmologyMetrics ( final OphthalmologyMetrics ophthalmologyMetrics ) {
        this.ophthalmologyMetrics = ophthalmologyMetrics;
    }

    /**
     * Sets the list of Diagnoses associated with this visit
     *
     * @param list
     *            The List of Diagnoses
     */
    public void setDiagnoses ( final List<Diagnosis> list ) {
        diagnoses = list;
    }

    /**
     * Returns the list of diagnoses for this visit
     *
     * @return The list of diagnoses
     */
    public List<Diagnosis> getDiagnoses () {
        return diagnoses;
    }

    /**
     * Sets the list of prescriptions associated with this visit
     *
     * @param prescriptions
     *            The list of prescriptions
     */
    public void setPrescriptions ( final List<Prescription> prescriptions ) {
        this.prescriptions = prescriptions;
    }

    /**
     * Returns the list of prescriptions for this visit
     *
     * @return The list of prescriptions
     */
    public List<Prescription> getPrescriptions () {
        return prescriptions;
    }

    /**
     * Sets the list of CPT Code associated with this visit
     *
     * @param codes
     *            The list of CPT Code
     */
    public void setCodes ( final List<CPTCode> codes ) {
        this.codes = codes;
    }

    /**
     * Returns the list of CPT Code for this visit
     *
     * @return The list of CPT Codes
     */
    public List<CPTCode> getCodes () {
        return this.codes;
    }

    /**
     * Returns the Satisfaction Survey for this visit
     *
     * @return the Satisfaction Survey POJO
     */
    public SatisfactionSurvey getSatisfactionSurvey () {
        return satisfactionSurvey;
    }

    /**
     * Sets the Satisfaction Survey for this visit to the given value
     *
     * @param satisfactionSurvey
     *            The value the satisfaction survey is to be set as
     */
    public void setSatisfactionSurvey ( final SatisfactionSurvey satisfactionSurvey ) {
        this.satisfactionSurvey = satisfactionSurvey;
    }

}
