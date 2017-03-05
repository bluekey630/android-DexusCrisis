package com.ninja.dexuscrisis.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ninja.dexuscrisis.MainActivity;
import com.ninja.dexuscrisis.R;
import com.ninja.dexuscrisis.SwipeGestureListener.OnSwipeTouchListener;
import com.ninja.dexuscrisis.Utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class DevelopFragment extends Fragment {


    public DevelopFragment() {
        // Required empty public constructor
    }

    LayoutInflater mInflater;
    View mView;
    MainActivity mContext;
    LinearLayout lyMain;
    JSONArray jsonArrayDevelop;
    private int icons[] = { R.drawable.ic_tab_1, R.drawable.ic_tab_2, R.drawable.ic_tab_3};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_develop, container, false);
        mContext = (MainActivity)getActivity();
        mInflater = inflater;

        setUI();
        initData();
        presentData();

        return mView;
    }

    void setUI() {
        lyMain = (LinearLayout)mView.findViewById(R.id.lyMain);

        mView.findViewById(R.id.btnMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.toggleMenu();
            }
        });

        lyMain.setOnTouchListener(new OnSwipeTouchListener(mContext) {
            public void onSwipeRight() {
                mContext.launchFragment(1);
            }
            public void onSwipeLeft() {
                mContext.launchFragment(3);
            }
        });
    }

    void initData() {
        InputStream is = mContext.getResources().openRawResource(R.raw.develop_json);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();

        try {
            jsonArrayDevelop = new JSONArray(jsonString);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void presentData() {
        lyMain.removeAllViews();


        try {
            // first part
            JSONObject jsonObject = jsonArrayDevelop.getJSONObject(0);
            String strTitle = jsonObject.getString(Constant.JSON_TITLE);

            View cellContent = mInflater.inflate(R.layout.cell_identify, null);

            ((TextView)cellContent.findViewById(R.id.tvTitle)).setText(strTitle);
            ((ImageView)cellContent.findViewById(R.id.imgvTab)).setImageResource(icons[0]);

            LinearLayout lyContentList = (LinearLayout)cellContent.findViewById(R.id.lyList);

            JSONArray jsonArrayItems = jsonObject.getJSONArray(Constant.JSON_ITEMS);

            for(int i = 0; i < jsonArrayItems.length(); i ++) {
                String strItem = jsonArrayItems.getString(i);

                View cellItem = mInflater.inflate(R.layout.cell_paragraph_dot, null);
                ((TextView)cellItem.findViewById(R.id.tvText)).setText(strItem);

                lyContentList.addView(cellItem);
            }

            lyMain.addView(cellContent);

            //second part
            jsonObject = jsonArrayDevelop.getJSONObject(1);
            strTitle = jsonObject.getString(Constant.JSON_TITLE);
            String strDescription = jsonObject.getString(Constant.JSON_DESCRIPTION);

            cellContent = mInflater.inflate(R.layout.cell_identify, null);

            ((TextView)cellContent.findViewById(R.id.tvTitle)).setText(strTitle);
            ((ImageView)cellContent.findViewById(R.id.imgvTab)).setImageResource(icons[1]);

            lyContentList = (LinearLayout)cellContent.findViewById(R.id.lyList);

            View cellDescription = mInflater.inflate(R.layout.cell_paragraph_dot, null);
            ((TextView)cellDescription.findViewById(R.id.tvText)).setText(strDescription);

            lyContentList.addView(cellDescription);

            jsonArrayItems = jsonObject.getJSONArray(Constant.JSON_ITEMS);

            for(int i = 0; i < jsonArrayItems.length(); i ++) {
                String strItem = jsonArrayItems.getString(i);

                View cellItem = mInflater.inflate(R.layout.cell_paragraph_circle, null);
                ((TextView)cellItem.findViewById(R.id.tvText)).setText(strItem);

                lyContentList.addView(cellItem);
            }

            lyMain.addView(cellContent);

            //3rd part
            jsonObject = jsonArrayDevelop.getJSONObject(2);
            strTitle = jsonObject.getString(Constant.JSON_TITLE);
            String strContent = jsonObject.getString(Constant.JSON_CONTENT);
            strDescription = jsonObject.getString(Constant.JSON_DESCRIPTION);
            cellContent = mInflater.inflate(R.layout.cell_identify, null);

            ((TextView)cellContent.findViewById(R.id.tvTitle)).setText(strTitle);
            ((ImageView)cellContent.findViewById(R.id.imgvTab)).setImageResource(icons[2]);

            lyContentList = (LinearLayout)cellContent.findViewById(R.id.lyList);

            View cellDescriptionOther = mInflater.inflate(R.layout.cell_paragraph_description, null);
            ((TextView)cellDescriptionOther.findViewById(R.id.tvText)).setText(strContent);
            lyContentList.addView(cellDescriptionOther);

            cellDescription = mInflater.inflate(R.layout.cell_paragraph_description, null);
            ((TextView)cellDescription.findViewById(R.id.tvText)).setText(strDescription);
            lyContentList.addView(cellDescription);

            jsonArrayItems = jsonObject.getJSONArray(Constant.JSON_ITEMS);

            for(int i = 0; i < jsonArrayItems.length(); i ++) {
                String strItem = jsonArrayItems.getString(i);

                View cellItem = mInflater.inflate(R.layout.cell_paragraph_check, null);
                ((TextView)cellItem.findViewById(R.id.tvText)).setText(strItem);
                lyContentList.addView(cellItem);
            }

            lyMain.addView(cellContent);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



}
