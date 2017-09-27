package com.hpdeveloper.mvp.mvp;


import com.hpdeveloper.mvp.entities.Student;

import java.util.List;

/**
 * Created by hirenpatel on 26/09/17.
 */

public interface StudentListContract {

    interface View {

        void showStudents(List<Student> studentList);

        void showPlaceHolder();
    }

    interface Presenter {

        void getStudents();

    }
}
