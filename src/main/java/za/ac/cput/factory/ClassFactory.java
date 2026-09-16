package za.ac.cput.factory;

import za.ac.cput.domain.Class;
import za.ac.cput.domain.Course;
import za.ac.cput.util.Helper;

public class ClassFactory {

    public static Class createClass(
            int classId,
            String classCode,
            String className,
            Course course) {

        if (!Helper.isValidId(classId)
                || Helper.isNullOrEmpty(classCode)
                || Helper.isNullOrEmpty(className)
                || Helper.isNull(course)) {

            throw new IllegalArgumentException("Enter valid details");
        }

        return new Class.Builder()
                .setClassId(classId)
                .setClassCode(classCode)
                .setClassName(className)
                .setCourse(course)
                .build();
    }
}