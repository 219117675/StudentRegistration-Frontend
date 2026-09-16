package za.ac.cput.factory;

import za.ac.cput.domain.Course;
import za.ac.cput.domain.Department;
import za.ac.cput.util.Helper;

public class CourseFactory {
    public static Course createCourse(int courseId, String courseCode, String courseName, Department department){
        if(!Helper.isValidId(courseId) || Helper.isNullOrEmpty(courseCode)
         || Helper.isNullOrEmpty(courseName) || Helper.isNull(department)){
            throw new IllegalArgumentException("Enter valid course details");
        }

        return new Course.Builder()
                .setCourseId(courseId)
                .setCourseCode(courseCode)
                .setCourseName(courseName)
                .setDepartment(department)
                .build();
    }
}
