/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.timtable.migration;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class CourseInfoExtractor {

    private String ID;
    private String name;
    private String section;
    private String department;

    public void updateContent(String content) {

        int indexID = content.indexOf("-");
        ID = content.substring(0, indexID);

        int nameIndex = content.indexOf("-", indexID + 1);
        if (content.indexOf(" - ", indexID) != -1) {
            nameIndex = content.indexOf("-", nameIndex + 1);
        }

        name = content.substring(indexID + 1, nameIndex);
        int deptIndex = content.indexOf("-", nameIndex + 1);
        if (deptIndex != -1) {
            department = content.substring(nameIndex + 1, deptIndex);
            section = content.substring(deptIndex + 1, content.length());
        } else {
            department = content.substring(nameIndex + 1, content.length());
            section = "A";
        }

        if(department.equals("MBA")){
            department = "BBA";
        } else if(department.equals("BA")){
            department = "BBA";
        } else if(department.equals("EL")){
            department = "EE";
        } else if(department.equals("MS")){
            department = ID.substring(0, 2);
        }

        if(name.equals("Advanced Computer Architecture")){
            department = "CS";
        }
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getSection() {
        return section;
    }

    public String getDepartment() {
        return department;
    }
}
