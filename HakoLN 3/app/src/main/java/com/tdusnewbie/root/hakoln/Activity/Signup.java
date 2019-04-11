package com.tdusnewbie.root.hakoln.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tdusnewbie.root.hakoln.Model.UserInfo;
import com.tdusnewbie.root.hakoln.R;

import java.util.Calendar;

public class Signup extends AppCompatActivity {

    EditText editName, editEmail, editNewuser, editNewpass,editRetype;
    Button btnChooseDate, btnDoSignup, btnCancel;
    TextView txtBirth;
    Intent intent;
    DatePickerDialog.OnDateSetListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Mapping();



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editNewpass.getText().toString().equals(editRetype.getText().toString()))
                {
                   intent = new Intent(Signup.this,Signin.class);
                   UserInfo user = new UserInfo();

                   user.setName(editName.getText().toString());
                   user.setEmail(editEmail.getText().toString());
                   user.setBirth(txtBirth.getText().toString());
                   user.setUsername(editNewuser.getText().toString());
                   user.setPass(editNewpass.getText().toString());

                   intent.putExtra("UserInfoMess",user);
                   setResult(Activity.RESULT_OK,intent);
                   finish();
                }
                else
                    Toast.makeText(Signup.this, "Retype is not Correct !! Please do it Again !!",Toast.LENGTH_SHORT).show();

            }
        });

        btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Signup.this,
                        android.R.style.Theme_DeviceDefault_Dialog_Alert,
                        listener,
                        year,month,day
                );
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
//                Calendar c = Calendar.getInstance();
//                c.set(Calendar.YEAR,year);
//                c.set(Calendar.MONTH,month);
//                c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String date = String.format("%02d",dayOfMonth) + "/" + String.format("%02d",month) + "/" + year;
                //String date = DateFormat.getDateInstance().format(c.getTime());
                txtBirth.setText(date);
            }
        };


    }


    private void Mapping()
    {
        // Information of User
        editName        = (EditText) findViewById(R.id.editName);
        editEmail       = (EditText) findViewById(R.id.editEmail);
        txtBirth        = (TextView) findViewById(R.id.txtBirth);

        // Account of User
        editNewuser     = (EditText) findViewById(R.id.editNewuser);
        editNewpass     = (EditText) findViewById(R.id.editNewpass);
        editRetype      = (EditText) findViewById(R.id.editRetype);

        btnChooseDate   = (Button) findViewById(R.id.btnChooseDate);
        btnDoSignup     = (Button) findViewById(R.id.btnDoSignup);
        btnCancel       = (Button) findViewById(R.id.btnCancel);

    }

}
