package com.example.sqlitedemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sqlitedemo.R;
import com.example.sqlitedemo.entity.Student;
import com.example.sqlitedemo.service.StudentService;
import com.example.sqlitedemo.service.StudentServiceImpl;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class InsertActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_name;
    private EditText et_age;
    private Spinner sp_classname;
    private Button ensure,remove;
    private List<String> fStudent;
    private String flag;
    private Student student;
    private StudentService studentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        studentService=new StudentServiceImpl(this);
        initData();
        initView();
    }

    private void initView() {
        et_name=findViewById(R.id.et_name);
        et_age=findViewById(R.id.et_age);
        sp_classname=findViewById(R.id.sp_class);
        ensure=findViewById(R.id.btn_ensure);
        remove=findViewById(R.id.btn_remove);

        ensure.setOnClickListener(this);
        remove.setOnClickListener(this);
        fStudent= Arrays.asList(getResources().getStringArray(R.array.fStudent));
        sp_classname.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                fStudent
        ));
    }

    private void initData() {
        Intent intent=getIntent();
        flag=intent.getStringExtra("flag");

        Bundle bundle=intent.getExtras();
        if (bundle!=null){
            student=(Student) bundle.getSerializable("student_obj");
            if (student!=null){
                et_name.setText(String.valueOf(student.getName()));
                et_name.setEnabled(false);
                sp_classname.setSelection(fStudent.indexOf(student.getClassmate()));
                et_age.setText(String.valueOf(student.getAge()));
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ensure:
                updata();
                break;
            case R.id.btn_remove:
                Intent intent=new Intent(InsertActivity.this,MainActivity.class);
                startActivity(intent);
                break;

        }
    }

    private void updata() {
        if (student==null){
            student=new Student();
        }
        student.setName(et_name.getText().toString());
        student.setClassmate((String) sp_classname.getSelectedItem());
        student.setAge(Integer.valueOf(et_age.getText().toString()));
        if ("修改".equals(flag)) {
            studentService.update(student);
        } else if ("添加".equals(flag)) {
            studentService.insert(student);
        }

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("student_obj", student);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
