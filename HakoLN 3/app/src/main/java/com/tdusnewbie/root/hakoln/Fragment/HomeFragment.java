package com.tdusnewbie.root.hakoln.Fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.tdusnewbie.root.hakoln.Activity.MangaView;
import com.tdusnewbie.root.hakoln.Activity.ChapterNovelView;
import com.tdusnewbie.root.hakoln.Model.BaseInfoModel;
import com.tdusnewbie.root.hakoln.R;
import com.tdusnewbie.root.hakoln.UpdatedListAdaper;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    private View view;
    private ListView listUpdate;
    private Button btnTruyenTranh;
    private Button btnTruyenChu;
    private ArrayList<BaseInfoModel> baseInfoModels;
    private ArrayList<BaseInfoModel> baseInfoNovelModels;
    private static boolean flag =true;

    UpdatedListAdaper updatedListAdaper;



    public HomeFragment(){
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment,container,false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listUpdate = view.findViewById(R.id.listUpdate);
        btnTruyenTranh = view.findViewById(R.id.btntruyentranh);
        btnTruyenChu = view.findViewById(R.id.btntruyenChu);

        btnTruyenTranh.setEnabled(false);
        btnTruyenChu.setEnabled(true);


        btnTruyenTranh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReloadManga();
            }
        });

        btnTruyenChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReloadNovel();
            }
        });
        updatedListAdaper = new UpdatedListAdaper(getContext(), R.layout.row_list_update, baseInfoModels);
        listUpdate.setAdapter(updatedListAdaper);
        listUpdate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(flag) {
                    BaseInfoModel baseInfoModel = (BaseInfoModel) parent.getItemAtPosition(position);
                    Intent intent = new Intent(getContext(), MangaView.class);
                    intent.putExtra("MangaInfo", baseInfoModel);
                    startActivity(intent);
                }
                else
                {
                    BaseInfoModel baseInfoNovelModel = (BaseInfoModel) parent.getItemAtPosition(position);
                    Intent intent = new Intent(getContext(), ChapterNovelView.class);
                    intent.putExtra("VolumeInfo", baseInfoNovelModel);
                    startActivity(intent);

                }
            }
        });

    }
    public ArrayList<BaseInfoModel> getBaseInfoModels() {
        return baseInfoModels;
    }

    public void setBaseInfoModels(ArrayList<BaseInfoModel> baseInfoModels) {
        this.baseInfoModels = baseInfoModels;
    }

    private void ReloadManga() {
        updatedListAdaper = new UpdatedListAdaper(getContext(), R.layout.row_list_update, baseInfoModels);
        listUpdate.setAdapter(updatedListAdaper);
        flag = true;
        btnTruyenTranh.setEnabled(false);
        btnTruyenChu.setEnabled(true);

    }

    private void ReloadNovel()
    {
        updatedListAdaper = new UpdatedListAdaper(getContext(), R.layout.row_list_update, baseInfoNovelModels);
        listUpdate.setAdapter(updatedListAdaper);
        flag = false;
        btnTruyenTranh.setEnabled(true);
        btnTruyenChu.setEnabled(false);

    }

    public ArrayList<BaseInfoModel> getBaseInfoNovelModels() {
        return baseInfoNovelModels;
    }

    public void setBaseInfoNovelModels(ArrayList<BaseInfoModel> baseInfoNovelModels) {
        this.baseInfoNovelModels = baseInfoNovelModels;
    }

    

}
