package com.tdusnewbie.root.hakoln;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tdusnewbie.root.hakoln.Model.BaseInfoModel;

import java.util.ArrayList;

public class UpdatedListAdaper extends ArrayAdapter<BaseInfoModel> {

    private Context mContext;
    private int mResource;
    private Filter filter;
    private ArrayList<BaseInfoModel> arrayOriginal;
    private ArrayList<BaseInfoModel> arrayFilterItem;

    public UpdatedListAdaper(Context context, int resource, ArrayList<BaseInfoModel> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        arrayFilterItem = new ArrayList<>(objects);
        arrayOriginal = new ArrayList<>(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bitmap bitmap = getItem(position).getBitmap();
        String title = getItem(position).getTitle();
        ArrayList<String> categories = getItem(position).getCategories();

        convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);

        ImageView imageView = convertView.findViewById(R.id.imagePoster);
        TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        TextView txtCategory = convertView.findViewById(R.id.txtCategory);


        txtCategory.setText("");
        txtTitle.setText(title);

        if (bitmap == null) {
            imageView.setImageResource(R.drawable.default_image);
        } else
            imageView.setImageBitmap(bitmap);
        for (int i = 0; i < categories.size(); i++) {
            if (i + 1 == categories.size())
                txtCategory.append(categories.get(i));
            else
                txtCategory.append(categories.get(i) + ", ");
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if(filter == null)
            filter = new UpdatedListFilter();

        return filter;
    }

    private class UpdatedListFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String prefix = constraint.toString().toLowerCase();
            if(prefix == null || prefix.length() == 0 )
            {
                ArrayList<BaseInfoModel> list = new ArrayList<>(arrayOriginal);
                results.values = list;
                results.count = list.size();
            } else {
                final ArrayList<BaseInfoModel> list = new ArrayList<>(arrayOriginal);
                final ArrayList<BaseInfoModel> nlist = new ArrayList<>();
                int count = list.size();
                for(int i = 0; i < count; i++)
                {
                    Log.e("size", nlist.size() + "");
                    final BaseInfoModel baseInfoModel = list.get(i);
                    final String value = baseInfoModel.getTitle().toLowerCase();
                    Log.e("list_size", list.size() + "");
                    Log.e("size", value);

                    if(value.startsWith(prefix))
                        nlist.add(baseInfoModel);
                }

                Log.e("size", nlist.size() + "");
                results.values = nlist;
                results.count = nlist.size();
            }

            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayFilterItem = (ArrayList<BaseInfoModel>)results.values;

            clear();
            int count  = arrayFilterItem.size();
            for(int i = 0; i < count; i++)
            {
                BaseInfoModel baseInfoModel = (BaseInfoModel)arrayFilterItem.get(i);
                add(baseInfoModel);
            }
        }
    }
}
