package com.example.sqlitedemo.service;

import android.content.Context;

import com.example.sqlitedemo.dao.StudentDao;
import com.example.sqlitedemo.dao.StudentDaoImpl;
import com.example.sqlitedemo.entity.Student;

import java.util.List;

public class StudentServiceImpl implements StudentService{
    private StudentDao studentDao;

    public  StudentServiceImpl(Context context){
        studentDao=new StudentDaoImpl(context);
    }
    @Override
    public List<Student> getAllStudents() {
        return studentDao.selectAllStudents();
    }

    @Override
    public void insert(Student student) {
        studentDao.insert(student);
    }

    @Override
    public void update(Student student) {
        studentDao.update(student);
    }

    @Override
    public void delete(String StudentName) {
        studentDao.delete(StudentName);
    }
}
