package za.ac.cput.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "applications")
public class Application {

    // ============================================================
    // STATUS
    // ============================================================

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }


    // ============================================================
    // FIELDS
    // ============================================================

    @Id
    @Column(name = "application_id")
    private int applicationId;


    @OneToOne
    @JoinColumn(
            name = "applicant_person_id",
            nullable = false,
            unique = true
    )
    private Applicant applicant;


    @ManyToOne
    @JoinColumn(
            name = "course_id",
            nullable = false
    )
    private Course course;


    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;


    // ============================================================
    // CONSTRUCTORS
    // ============================================================

    protected Application() {
    }


    private Application(Builder builder) {

        this.applicationId =
                builder.applicationId;

        this.applicant =
                builder.applicant;

        this.course =
                builder.course;

        this.status =
                builder.status != null
                        ? builder.status
                        : Status.PENDING;
    }


    // ============================================================
    // GETTERS
    // ============================================================

    public int getApplicationId() {

        return applicationId;
    }


    public Applicant getApplicant() {

        return applicant;
    }


    public Course getCourse() {

        return course;
    }


    public Status getStatus() {

        return status;
    }


    // ============================================================
    // SETTERS
    // ============================================================

    public void setStatus(Status status) {

        this.status = status;
    }


    // ============================================================
    // BUILDER
    // ============================================================

    public static class Builder {

        private int applicationId;

        private Applicant applicant;

        private Course course;

        private Status status;


        public Builder setApplicationId(
                int applicationId
        ) {

            this.applicationId =
                    applicationId;

            return this;
        }


        public Builder setApplicant(
                Applicant applicant
        ) {

            this.applicant =
                    applicant;

            return this;
        }


        public Builder setCourse(
                Course course
        ) {

            this.course =
                    course;

            return this;
        }


        public Builder setStatus(
                Status status
        ) {

            this.status =
                    status;

            return this;
        }


        public Application build() {

            return new Application(this);
        }
    }


    // ============================================================
    // TO STRING
    // ============================================================

    @Override
    public String toString() {

        return "Application{" +
                "applicationId=" +
                applicationId +
                ", applicant=" +
                (
                        applicant == null
                                ? null
                                : applicant.getApplicantId()
                ) +
                ", course=" +
                (
                        course == null
                                ? null
                                : course.getCourseId()
                ) +
                ", status=" +
                status +
                '}';
    }
}