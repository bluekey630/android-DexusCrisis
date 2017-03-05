package com.ninja.dexuscrisis.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class HowFragment extends Fragment {


    public HowFragment() {
        // Required empty public constructor
    }

    LayoutInflater mInflater;
    View mView;
    MainActivity mContext;
    LinearLayout lyMain;
    JSONArray jsonArrayHow;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_how, container, false);
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
                mContext.launchFragment(4);
            }
            public void onSwipeLeft() {

            }
        });
    }

    void initData() {
        InputStream is = mContext.getResources().openRawResource(R.raw.how_json);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();

        try {
            jsonArrayHow = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void presentData() {
        lyMain.removeAllViews();


        try {
            // first part
            JSONObject jsonObject = jsonArrayHow.getJSONObject(0);
            String strTitle = jsonObject.getString(Constant.JSON_TITLE);

            View cellContent = mInflater.inflate(R.layout.cell_how_one, null);

            ((TextView)cellContent.findViewById(R.id.tvTitle)).setText(strTitle);
            cellContent.findViewById(R.id.lySeperator).setVisibility(View.VISIBLE);

            LinearLayout lyContentList = (LinearLayout)cellContent.findViewById(R.id.lyList);

            JSONArray jsonArrayItems = jsonObject.getJSONArray(Constant.JSON_ITEMS);

            for(int i = 0; i < jsonArrayItems.length(); i ++) {
                JSONObject jsonObjectItem = jsonArrayItems.getJSONObject(i);
                String strFirst = jsonObjectItem.getString(Constant.JSON_FIRST);
                JSONArray jsonArraySubItems = jsonObjectItem.getJSONArray(Constant.JSON_SECOND);
                String strSecond = jsonArraySubItems.getString(0);
                String strThird = jsonArraySubItems.getString(1);

                View cellItem = mInflater.inflate(R.layout.cell_twice, null);
                ((TextView)cellItem.findViewById(R.id.tvFirst)).setText(strFirst);
                ((TextView)cellItem.findViewById(R.id.tvSecond)).setText(strSecond);
                ((TextView)cellItem.findViewById(R.id.tvThird)).setText(strThird);
                cellItem.findViewById(R.id.lySeperator).setVisibility(View.VISIBLE);

                lyContentList.addView(cellItem);
            }

            lyMain.addView(cellContent);

            //second part
            jsonObject = jsonArrayHow.getJSONObject(1);
            strTitle = jsonObject.getString(Constant.JSON_TITLE);
            String strDescription = jsonObject.getString(Constant.JSON_DESCRIPTION);

            cellContent = mInflater.inflate(R.layout.cell_how_one, null);

            ((TextView)cellContent.findViewById(R.id.tvTitle)).setText(strTitle);
            cellContent.findViewById(R.id.lySeperator).setVisibility(View.INVISIBLE);

            lyContentList = (LinearLayout)cellContent.findViewById(R.id.lyList);

            View cellDescription = mInflater.inflate(R.layout.cell_paragraph_description, null);
            ((TextView)cellDescription.findViewById(R.id.tvText)).setText(strDescription);

            lyContentList.addView(cellDescription);

            jsonArrayItems = jsonObject.getJSONArray(Constant.JSON_ITEMS);

            for(int i = 0; i < jsonArrayItems.length(); i ++) {
                JSONObject jsonObjectItem = jsonArrayItems.getJSONObject(i);
                String strFirst = jsonObjectItem.getString(Constant.JSON_FIRST);
                String strSecond = jsonObjectItem.getString(Constant.JSON_SECOND);

                View cellItem = mInflater.inflate(R.layout.cell_twice, null);
                ((TextView)cellItem.findViewById(R.id.tvFirst)).setText(strFirst);
                ((TextView)cellItem.findViewById(R.id.tvSecond)).setText(strSecond);
                ((TextView)cellItem.findViewById(R.id.tvThird)).setVisibility(View.GONE);
                cellItem.findViewById(R.id.lySeperator).setVisibility(View.INVISIBLE);

                lyContentList.addView(cellItem);
            }

            lyMain.addView(cellContent);

            View cellItem5 = mInflater.inflate(R.layout.cell_twice_black, null);
            lyMain.addView(cellItem5);

            View cellItem6 = mInflater.inflate(R.layout.cell_twice_red, null);
            lyMain.addView(cellItem6);

            //3rd part
            jsonObject = jsonArrayHow.getJSONObject(2);
            cellContent = mInflater.inflate(R.layout.cell_how_two, null);
            lyContentList = (LinearLayout)cellContent.findViewById(R.id.lyList);
            jsonArrayItems = jsonObject.getJSONArray(Constant.JSON_ITEMS);

            for(int i = 0; i < jsonArrayItems.length(); i ++) {
                String strItem = jsonArrayItems.getString(i);

                View cellItem = mInflater.inflate(R.layout.cell_paragraph_dot, null);
                ((TextView)cellItem.findViewById(R.id.tvText)).setText(strItem);
                lyContentList.addView(cellItem);
            }

            lyMain.addView(cellContent);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



}
