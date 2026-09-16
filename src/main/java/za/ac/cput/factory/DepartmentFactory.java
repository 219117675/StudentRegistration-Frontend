package za.ac.cput.factory;


import za.ac.cput.domain.Department;
import za.ac.cput.util.Helper;

public class DepartmentFactory {
    public static Department createDepartment(int departmentId, String departmentCode, String departmentName){
        if (!Helper.isValidId(departmentId)|| Helper.isNullOrEmpty(departmentCode)
        || Helper.isNullOrEmpty(departmentName)){
            throw new IllegalArgumentException("Entered wrong information");
        }

       return new Department.Builder()
               .setDepartmentId(departmentId)
               .setDepartmentCode(departmentCode)
               .setDepartmentName(departmentName)
               .build();
    }
}
