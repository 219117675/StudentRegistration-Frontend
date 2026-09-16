package za.ac.cput.factory;

import za.ac.cput.domain.Class;
import za.ac.cput.domain.Registration;
import za.ac.cput.domain.Student;
import za.ac.cput.util.Helper;

public class RegistrationFactory {
    public static Registration createRegistration(int registrationId, Student student, Class class1){
        if (!Helper.isValidId(registrationId) || Helper.isNull(student)
        || Helper.isNull(class1)){
            throw new IllegalArgumentException("Enter valid deatails");
        }

        return new Registration.Builder()
                .setRegistrationId(registrationId)
                .setStudent(student)
                .setCourseClass(class1)
                .build();
    }
}
