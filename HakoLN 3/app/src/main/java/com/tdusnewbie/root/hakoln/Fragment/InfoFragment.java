package com.tdusnewbie.root.hakoln.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tdusnewbie.root.hakoln.Model.UserInfo;
import com.tdusnewbie.root.hakoln.R;

public class InfoFragment extends Fragment {

    View view;

    TextView txtUserName_info, txtName_info, txtBirth_info, txtEmail_info;
    ImageButton btnEditName_info, btnEditUser_info, btnEditBirth_info;
    UserInfo user;


    public InfoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.info_fragment,container,false);
        return view;
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        Mapping();
//    }
//
//    public void setUserInfoPage(UserInfo user)
//    {
//        this.user = user;
//
//    }
//
//
//    private void Mapping()
//    {
//        txtName_info = (TextView) view.findViewById(R.id.txtName_info);
//        txtBirth_info = (TextView) view.findViewById(R.id.txtBirth_info);
//        txtUserName_info = (TextView) view.findViewById(R.id.txtUserName_info);
//        txtEmail_info = (TextView) view.findViewById(R.id.txtEmail_info);
//
//        btnEditName_info = (ImageButton) view.findViewById(R.id.btnEditName_info);
//        btnEditBirth_info = (ImageButton) view.findViewById(R.id.btnEditBirth_info);
//        btnEditUser_info = (ImageButton) view.findViewById(R.id.btnEditUser_info);
//
//        txtName_info.setText(user.getName());
//        txtEmail_info.setText(user.getEmail());
//        txtUserName_info.setText(user.getUsername());
//        txtBirth_info.setText(user.getBirth());
//
//    }
}
