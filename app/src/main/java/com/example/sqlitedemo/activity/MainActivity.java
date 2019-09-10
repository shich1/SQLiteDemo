package com.example.sqlitedemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.sqlitedemo.R;
import com.example.sqlitedemo.adapter.StudentAdapter;
import com.example.sqlitedemo.entity.Student;
import com.example.sqlitedemo.service.StudentService;
import com.example.sqlitedemo.service.StudentServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_REQUEST=100;
    private static final int MODIFY_REQUEST=101;
    private ListView lv_list;
    private StudentAdapter studentAdapter;
    private List<Student> students;
    private int selectedPos;
    private Student selectedStudent;
    private Button add,modify,delete;
    private StudentService studentService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initView() {
        add=findViewById(R.id.btn_add);
        modify=findViewById(R.id.btn_modify);
        delete=findViewById(R.id.btn_delete);
        lv_list=findViewById(R.id.lv_list);

        studentAdapter=new StudentAdapter(students);
        lv_list.setAdapter(studentAdapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,InsertActivity.class);
                intent.putExtra("flag","添加");
                startActivityForResult(intent,ADD_REQUEST);
            }
        });
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                selectedPos=position;
                selectedStudent=(Student) parent.getItemAtPosition(position);

                modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(MainActivity.this,InsertActivity.class);
                        intent.putExtra("flag","修改");

                        Bundle bundle=new Bundle();
                        bundle.putSerializable("student_obj",selectedStudent);
                        intent.putExtras(bundle);

                        startActivityForResult(intent,MODIFY_REQUEST);
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        studentService.delete(selectedStudent.getName());
                        students.remove(position);
                        studentAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    // 从SQLite数据库获取数据
    private void initData() {
        // 从SQLite数据库获取宿舍列表
        studentService = new StudentServiceImpl(this);
        students = studentService.getAllStudents();
        // 若数据库中没数据，则初始化数据列表，防止ListView报错
        if (students == null) {
            students = new ArrayList<>();
        }
    }
    // 接收InsertActivity的返回的添加或修改后的flag对象，更新flags，刷新列表
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }
            // 更新列表
            selectedStudent = (Student) bundle.get("student_obj");
            if (requestCode == MODIFY_REQUEST) {
                students.set(selectedPos, selectedStudent);
            } else if (requestCode == ADD_REQUEST) {
                students.add(selectedStudent);
            }
            // 刷新ListView
            studentAdapter.notifyDataSetChanged();
        }

    }
}
