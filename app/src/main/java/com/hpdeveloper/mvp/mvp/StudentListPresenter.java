package com.hpdeveloper.mvp.mvp;


import com.hpdeveloper.mvp.entities.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hirenpatel on 26/09/17.
 */

public class StudentListPresenter implements StudentListContract.Presenter {

    private StudentListContract.View view;

    public StudentListPresenter(StudentListContract.View view) {
        this.view = view;
    }


    @Override
    public void getStudents() {
        List<Student> studentList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            studentList.add(new Student("Student " + i, "androidstudent" + i + "@gmail.com"));
        }
        view.showStudents(studentList);
    }
}
