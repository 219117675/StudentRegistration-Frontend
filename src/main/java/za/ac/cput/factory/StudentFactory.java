package za.ac.cput.factory;

import za.ac.cput.domain.Applicant;
import za.ac.cput.domain.Student;
import za.ac.cput.util.Helper;

public class StudentFactory {
    public static Student createStudent(int studentId, String studentNumber, String studentEmail, Applicant applicant){

        if (!Helper.isValidId(studentId) || Helper.isNullOrEmpty(studentNumber)
        || Helper.isNullOrEmpty(studentEmail) || Helper.isNull(applicant)){
            throw new IllegalArgumentException("Enter valid details");
        }
        return new Student.Builder()
                .setStudentId(studentId)
                .setStudentNumber(studentNumber)
                .setStudentEmail(studentEmail)
                .setApplicant(applicant)
                .build();
    }
}
