package za.ac.cput.factory;

import za.ac.cput.domain.Applicant;
import za.ac.cput.domain.Application;
import za.ac.cput.domain.Course;
import za.ac.cput.util.Helper;

public class ApplicationFactory {
    public static Application createApplication(int applicationId, Applicant applicant, Course course){
        if (!Helper.isValidId(applicationId)|| Helper.isNull(applicant)|| Helper.isNull(course)){
            throw new IllegalArgumentException("Enter valid details");
        }

        return new Application.Builder()
                .setApplicationId(applicationId)
                .setApplicant(applicant)
                .setCourse(course)
                .build();
    }
}
