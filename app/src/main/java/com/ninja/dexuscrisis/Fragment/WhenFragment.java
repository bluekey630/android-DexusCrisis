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

public class WhenFragment extends Fragment {


    public WhenFragment() {
        // Required empty public constructor
    }

    LayoutInflater mInflater;
    View mView;
    MainActivity mContext;
    LinearLayout lyMain;
    JSONArray jsonArrayWhen;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_when, container, false);
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
                mContext.launchFragment(2);
            }
            public void onSwipeLeft() {
                mContext.launchFragment(4);
            }
        });
    }

    void initData() {
        InputStream is = mContext.getResources().openRawResource(R.raw.when_json);
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
            jsonArrayWhen = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void presentData() {
        lyMain.removeAllViews();

        for(int i = 0; i < jsonArrayWhen.length(); i ++) {
            try {
                JSONObject jsonObject = jsonArrayWhen.getJSONObject(i);
                String strDescriptor = jsonObject.getString(Constant.JSON_DESCRIPTOR);
                String strImpact = jsonObject.getString(Constant.JSON_IMPACT);
                JSONArray jsonArrayItems = jsonObject.getJSONArray(Constant.JSON_EXAMPLE);

                View cellContent = mInflater.inflate(R.layout.cell_when, null);

                ((TextView)cellContent.findViewById(R.id.tvDescriptor)).setText(strDescriptor);
                ((TextView)cellContent.findViewById(R.id.tvImpact)).setText(strImpact);
                LinearLayout lyList = (LinearLayout)cellContent.findViewById(R.id.lySubList);

                for(int j = 0; j < jsonArrayItems.length(); j ++) {
                    String strItem = jsonArrayItems.getString(j);
                    View cellItem = mInflater.inflate(R.layout.cell_paragraph_dot, null);
                    ((TextView)cellItem.findViewById(R.id.tvText)).setText(strItem);

                    lyList.addView(cellItem);
                }

                lyMain.addView(cellContent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
