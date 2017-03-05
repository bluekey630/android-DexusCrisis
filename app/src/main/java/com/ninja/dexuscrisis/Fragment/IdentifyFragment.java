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

public class IdentifyFragment extends Fragment {


    public IdentifyFragment() {
        // Required empty public constructor
    }

    LayoutInflater mInflater;
    View mView;
    MainActivity mContext;
    LinearLayout lyMain;
    JSONArray jsonArrayIdentify;
    private int icons[] = { R.drawable.ic_tab_1, R.drawable.ic_tab_2, R.drawable.ic_tab_3};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_identify, container, false);
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
            }
            public void onSwipeLeft() {
                mContext.launchFragment(1);
            }
        });


    }

    void initData() {
        InputStream is = mContext.getResources().openRawResource(R.raw.identify_json);
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
            jsonArrayIdentify = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void presentData() {
        lyMain.removeAllViews();

        for(int i = 0; i < jsonArrayIdentify.length(); i ++) {
            try {
                JSONObject jsonObject = jsonArrayIdentify.getJSONObject(i);

                String strTitle = jsonObject.getString(Constant.JSON_TITLE);

                View cellContent = mInflater.inflate(R.layout.cell_identify, null);

                ((TextView)cellContent.findViewById(R.id.tvTitle)).setText(strTitle);
                ((ImageView)cellContent.findViewById(R.id.imgvTab)).setImageResource(icons[i]);
                LinearLayout lyContentList = (LinearLayout)cellContent.findViewById(R.id.lyList);

                lyMain.addView(cellContent);

                JSONArray jsonArrayContent = jsonObject.getJSONArray(Constant.JSON_CONTENT);

                for(int j = 0; j < jsonArrayContent.length(); j ++) {
                    JSONObject jsonObjectContent = jsonArrayContent.getJSONObject(j);

                    String strSubTitle = jsonObjectContent.getString(Constant.JSON_SUBTITLE);
                    String strDescription = jsonObjectContent.getString(Constant.JSON_DESCRIPTION);

                    if(strSubTitle.length() > 0) {
                        View cellSubTitle = mInflater.inflate(R.layout.cell_paragraph_subtitle, null);
                        ((TextView)cellSubTitle.findViewById(R.id.tvText)).setText(strSubTitle);
                        lyContentList.addView(cellSubTitle);
                    }

                    if(strDescription.length() > 0) {
                        View cellDescription = mInflater.inflate(R.layout.cell_paragraph_description, null);
                        ((TextView)cellDescription.findViewById(R.id.tvText)).setText(strDescription);
                        lyContentList.addView(cellDescription);
                    }

                    JSONArray jsonArrayItems = jsonObjectContent.getJSONArray(Constant.JSON_ITEMS);

                    for(int k = 0; k < jsonArrayItems.length(); k ++) {
                        String strItem = jsonArrayItems.getString(k);
                        View cellItem = mInflater.inflate(R.layout.cell_paragraph_dot, null);
                        ((TextView)cellItem.findViewById(R.id.tvText)).setText(strItem);
                        lyContentList.addView(cellItem);
                    }
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }





}
