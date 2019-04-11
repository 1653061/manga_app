package com.tdusnewbie.root.hakoln.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tdusnewbie.root.hakoln.Model.UserInfo;
import com.tdusnewbie.root.hakoln.R;

import java.util.ArrayList;

public class Signin extends AppCompatActivity {

    Button btnSignin, btnSignup;
    EditText editUsername, editPass;
    String logTag = "Demo";
    Intent intent;
    UserInfo user;
    static ArrayList<UserInfo> listUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mapping();

        listUser = new ArrayList<>();
        UserInfo anonymous = new UserInfo();


        anonymous.setName("Empty");
        anonymous.setEmail("Empty");
        anonymous.setBirth("");
        anonymous.setUsername("anonymous");
        anonymous.setPass("");

        listUser.add(anonymous);


        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editUsername.getText().toString().equals("") || editPass.getText().equals(""))
                    Toast.makeText(Signin.this, "Username or Password cannot be empty !!!!", Toast.LENGTH_SHORT).show();

                else {
                    for (int i = 0; i < listUser.size(); i++) {
                        if (editUsername.getText().toString().equals(listUser.get(i).getUsername())
                                && editPass.getText().toString().equals(listUser.get(i).getPass())) {
                            Log.d(logTag, "Password and User's Name is Right");
                            intent = new Intent(Signin.this, Home.class);
                            intent.putExtra("UserInfoMess",listUser.get(i));

                            startActivity(intent);
                            return;
                        }
                    }
                    Toast.makeText(Signin.this, "Wrong Password or Cannot find Username", Toast.LENGTH_SHORT).show();
                    editPass.setText("");
                    editUsername.setText("");
                }
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(logTag,"User want to create a new Account");
                intent = new Intent(Signin.this,Signup.class);
                startActivityForResult(intent,1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            user = new UserInfo();
            user = (UserInfo) data.getSerializableExtra("UserInfoMess");

            listUser.add(user);

            editUsername.setText(user.getUsername());
            editPass.setText(user.getPass());

        }
    }

    private void Mapping()
    {
        btnSignin = (Button) findViewById(R.id.btnSignin);
        btnSignup = (Button) findViewById(R.id.btnSignup);

        editUsername = (EditText) findViewById(R.id.editUsername);
        editPass = (EditText) findViewById(R.id.editPass);
    }


}
