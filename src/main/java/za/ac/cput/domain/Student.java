package za.ac.cput.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "person_id")
public class Student extends Person {

    @Column(name = "student_id", nullable = false, unique = true)
    private int studentId;

    @Column(name = "student_number", nullable = false, unique = true)
    private String studentNumber;

    @Column(name = "student_email", nullable = false, unique = true)
    private String studentEmail;

    @OneToOne
    @JoinColumn(
            name = "applicant_person_id",
            unique = true
    )
    private Applicant applicant;

    protected Student() {
        super();
    }

    private Student(Builder builder) {

        super(
                builder.firstName,
                builder.lastName,
                builder.dateOfBirth,
                builder.address,
                builder.contactDetails,
                builder.gender,
                builder.race
        );

        this.studentId = builder.studentId;
        this.studentNumber = builder.studentNumber;
        this.studentEmail = builder.studentEmail;
        this.applicant = builder.applicant;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    @Override
    public String toString() {

        return "Student{" +
                "personId=" + getPersonId() +
                ", studentId=" + studentId +
                ", studentNumber='" + studentNumber + '\'' +
                ", studentEmail='" + studentEmail + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                '}';
    }

    /**
     * Creates a new Student Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int studentId;
        private String studentNumber;
        private String studentEmail;
        private Applicant applicant;

        private String firstName;
        private String lastName;
        private java.time.LocalDate dateOfBirth;
        private Address address;
        private ContactDetails contactDetails;
        private Gender gender;
        private Race race;

        public Builder setStudentId(int studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder setStudentNumber(String studentNumber) {
            this.studentNumber = studentNumber;
            return this;
        }

        public Builder setStudentEmail(String studentEmail) {
            this.studentEmail = studentEmail;
            return this;
        }

        public Builder setApplicant(Applicant applicant) {
            this.applicant = applicant;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setDateOfBirth(
                java.time.LocalDate dateOfBirth) {

            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public Builder setAddress(Address address) {
            this.address = address;
            return this;
        }

        public Builder setContactDetails(
                ContactDetails contactDetails) {

            this.contactDetails = contactDetails;
            return this;
        }

        public Builder setGender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder setRace(Race race) {
            this.race = race;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }
}