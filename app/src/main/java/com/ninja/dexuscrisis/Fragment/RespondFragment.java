package com.ninja.dexuscrisis.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
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

public class RespondFragment extends Fragment {


    public RespondFragment() {
        // Required empty public constructor
    }

    LayoutInflater mInflater;
    View mView;
    MainActivity mContext;
    LinearLayout lyMain;
    JSONArray jsonArrayRespond;
    private int icons[] = { R.drawable.ic_tab_1, R.drawable.ic_tab_2, R.drawable.ic_tab_3};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_respond, container, false);
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
                mContext.launchFragment(0);
            }
            public void onSwipeLeft() {
                mContext.launchFragment(2);
            }
        });
    }

    void initData() {
        InputStream is = mContext.getResources().openRawResource(R.raw.respond_json);
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
            jsonArrayRespond = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void presentData() {
        lyMain.removeAllViews();


        try {
            // first part
            JSONObject jsonObject = jsonArrayRespond.getJSONObject(0);
            String strTitle = jsonObject.getString(Constant.JSON_TITLE);
            String strDescription = jsonObject.getString(Constant.JSON_DESCRIPTION);

            View cellContent = mInflater.inflate(R.layout.cell_identify, null);

            ((TextView)cellContent.findViewById(R.id.tvTitle)).setText(strTitle);
            ((ImageView)cellContent.findViewById(R.id.imgvTab)).setImageResource(icons[0]);

            LinearLayout lyContentList = (LinearLayout)cellContent.findViewById(R.id.lyList);

            View cellDescription = mInflater.inflate(R.layout.cell_paragraph_description, null);
            ((TextView)cellDescription.findViewById(R.id.tvText)).setText(strDescription);
            lyContentList.addView(cellDescription);

            JSONArray jsonArrayItems = jsonObject.getJSONArray(Constant.JSON_ITEMS);

            for(int i = 0; i < jsonArrayItems.length(); i ++) {
                JSONObject jsonObjectItem = jsonArrayItems.getJSONObject(i);
                String strSubTitle = jsonObjectItem.getString(Constant.JSON_SUBTITLE);
                String strContent = jsonObjectItem.getString(Constant.JSON_CONTENT);

                View cellItem = mInflater.inflate(R.layout.cell_paragraph_check, null);
                String strHtml = "<b>" + strSubTitle + ":</b>" + strContent;

                ((TextView)cellItem.findViewById(R.id.tvText)).setText(Html.fromHtml(strHtml));

                lyContentList.addView(cellItem);
            }

            lyMain.addView(cellContent);

            //second part
            jsonObject = jsonArrayRespond.getJSONObject(1);
            strTitle = jsonObject.getString(Constant.JSON_TITLE);

            cellContent = mInflater.inflate(R.layout.cell_identify, null);

            ((TextView)cellContent.findViewById(R.id.tvTitle)).setText(strTitle);
            ((ImageView)cellContent.findViewById(R.id.imgvTab)).setImageResource(icons[1]);

            lyContentList = (LinearLayout)cellContent.findViewById(R.id.lyList);

            jsonArrayItems = jsonObject.getJSONArray(Constant.JSON_CONTENT);

            for(int i = 0; i < jsonArrayItems.length(); i ++) {
                JSONObject jsonObjectItem = jsonArrayItems.getJSONObject(i);
                strDescription = jsonObjectItem.getString(Constant.JSON_DESCRIPTION);

                cellDescription = mInflater.inflate(R.layout.cell_paragraph_description, null);
                ((TextView)cellDescription.findViewById(R.id.tvText)).setText(strDescription);
                lyContentList.addView(cellDescription);

                JSONArray jsonArraySubItems = jsonObjectItem.getJSONArray(Constant.JSON_ITEMS);

                for(int j = 0; j < jsonArraySubItems.length(); j ++) {
                    String strText = jsonArraySubItems.getString(j);
                    View cellItem = mInflater.inflate(R.layout.cell_paragraph_dot, null);
                    ((TextView)cellItem.findViewById(R.id.tvText)).setText(strText);
                    lyContentList.addView(cellItem);
                }
            }

            lyMain.addView(cellContent);

            //3rd part
            jsonObject = jsonArrayRespond.getJSONObject(2);
            strTitle = jsonObject.getString(Constant.JSON_TITLE);

            cellContent = mInflater.inflate(R.layout.cell_identify, null);

            ((TextView)cellContent.findViewById(R.id.tvTitle)).setText(strTitle);
            ((ImageView)cellContent.findViewById(R.id.imgvTab)).setImageResource(icons[2]);

            lyContentList = (LinearLayout)cellContent.findViewById(R.id.lyList);

            jsonArrayItems = jsonObject.getJSONArray(Constant.JSON_CONTENT);

            for(int i = 0; i < jsonArrayItems.length(); i ++) {
                JSONObject jsonObjectItem = jsonArrayItems.getJSONObject(i);
                String strText = jsonObjectItem.getString(Constant.JSON_TEXT);

                View cellItem = mInflater.inflate(R.layout.cell_paragraph_dot, null);
                ((TextView)cellItem.findViewById(R.id.tvText)).setText(strText);
                lyContentList.addView(cellItem);

                if(jsonObjectItem.has(Constant.JSON_SUBITEMS)) {
                    JSONArray jsonArraySubItems = jsonObjectItem.getJSONArray(Constant.JSON_SUBITEMS);

                    for(int j = 0; j < jsonArraySubItems.length(); j ++) {
                        String strSubItem = jsonArraySubItems.getString(j);
                        View cellSubItem = mInflater.inflate(R.layout.cell_paragraph_circle, null);
                        ((TextView)cellSubItem.findViewById(R.id.tvText)).setText(strSubItem);
                        lyContentList.addView(cellSubItem);
                    }
                }
            }

            lyMain.addView(cellContent);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



}
