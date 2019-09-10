package com.example.sqlitedemo.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sqlitedemo.entity.Student;
import com.example.sqlitedemo.utils.DBUtil;

import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {

    private DBUtil dbUtil;
    private SQLiteDatabase db;

    public StudentDaoImpl(Context context) {
        dbUtil = new DBUtil(context);
    }

    @Override
    public List<Student> selectAllStudents() {
        List<Student> students = null;
        db = dbUtil.getReadableDatabase();
        String sql = "select * from goto_student";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            students = new ArrayList<>();
            while (cursor.moveToNext()) {
                Student student1 = new Student();
                student1.setGoto_id(cursor.getInt(cursor.getColumnIndex("goto_id")));
                student1.setName(cursor.getString(cursor.getColumnIndex("name")));
                student1.setClassmate(cursor.getString(cursor.getColumnIndex("classmate")));
                student1.setAge(cursor.getInt(cursor.getColumnIndex("age")));

                students.add(student1);
            }
            cursor.close();
        }
        return students;
    }

    @Override
    public void insert(Student student) {
        db = dbUtil.getWritableDatabase();
        String sql = "insert into goto_student values(null,?,?,?)";
        db.execSQL(sql, new Object[]{
                student.getName(),
                student.getClassmate(),
                student.getAge()});
    }

    @Override
    public void update(Student student) {
        db = dbUtil.getWritableDatabase();
        String sql = "update goto_student set name=? where classmate=?";
        db.execSQL(sql, new Object[]{
                student.getName(),
                student.getClassmate(),
        });
    }

    @Override
    public void delete(String studentName) {
        db = dbUtil.getWritableDatabase();
        String sql = "delete from goto_student where name=?";
        db.execSQL(sql, new Object[]{studentName});
    }
}

