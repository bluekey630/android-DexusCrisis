package com.ninja.dexuscrisis.Fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class WhoFragment extends Fragment {


    public WhoFragment() {
        // Required empty public constructor
    }

    LayoutInflater mInflater;
    View mView;
    MainActivity mContext;
    LinearLayout lyMain;
    JSONArray jsonArrayWho;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_who, container, false);
        mContext = (MainActivity) getActivity();
        mInflater = inflater;

        setUI();
        initData();
        presentData();
        return mView;
    }

    void setUI() {
        lyMain = (LinearLayout) mView.findViewById(R.id.lyMain);
        mView.findViewById(R.id.btnMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.toggleMenu();
            }
        });

        lyMain.setOnTouchListener(new OnSwipeTouchListener(mContext) {
            public void onSwipeRight() {
                mContext.launchFragment(3);
            }

            public void onSwipeLeft() {
                mContext.launchFragment(5);
            }
        });
    }

    void initData() {
        InputStream is = mContext.getResources().openRawResource(R.raw.who_json);
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
            jsonArrayWho = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void presentData() {
        lyMain.removeAllViews();

        for (int i = 0; i < jsonArrayWho.length(); i++) {
            try {
                JSONObject jsonObject = jsonArrayWho.getJSONObject(i);
                String strTitle = jsonObject.getString(Constant.JSON_TITLES);
                JSONArray jsonArrayItems = jsonObject.getJSONArray(Constant.JSON_CONTENTS);

                View cellContent = mInflater.inflate(R.layout.cell_who, null);

                if (strTitle.length() == 0) {
                    ((TextView) cellContent.findViewById(R.id.tvSubTitle)).setVisibility(View.GONE);
                } else {
                    ((TextView) cellContent.findViewById(R.id.tvSubTitle)).setText(strTitle);
                }

                LinearLayout lyList = (LinearLayout) cellContent.findViewById(R.id.lySubList);

                for (int j = 0; j < jsonArrayItems.length(); j++) {
                    JSONObject jsonObjectItem = jsonArrayItems.getJSONObject(j);
                    String strRole = jsonObjectItem.getString(Constant.JSON_ROLE);
                    String strName = jsonObjectItem.getString(Constant.JSON_NAME);
                    final String strPhone = jsonObjectItem.getString(Constant.JSON_PHONE);

                    View cellItem = mInflater.inflate(R.layout.cell_who_item, null);
                    ((TextView) cellItem.findViewById(R.id.tvRole)).setText(strRole);
                    ((TextView) cellItem.findViewById(R.id.tvName)).setText(strName);

                    if (strPhone.length() == 0) {
                        cellItem.findViewById(R.id.tvPhone).setVisibility(View.GONE);
                    } else {
                        ((TextView) cellItem.findViewById(R.id.tvPhone)).setText(strPhone);

                    }

                    cellItem.findViewById(R.id.tvPhone).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (strPhone.length() > 0) {
                                new AlertDialog.Builder(mContext)
                                        .setTitle("Phone Call")
                                        .setMessage("Are you sure you want to make a call with this phone number?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                String strPhoneCall = "tel:" + getCorrectNumberFrom(strPhone);
                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse(strPhoneCall));
                                                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    // TODO: Consider calling
                                                    //    ActivityCompat#requestPermissions
                                                    // here to request the missing permissions, and then overriding
                                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                    //                                          int[] grantResults)
                                                    // to handle the case where the user grants the permission. See the documentation
                                                    // for ActivityCompat#requestPermissions for more details.

                                                    Toast.makeText(mContext, "Phone Call permission is not allowed", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                mContext.startActivity(callIntent);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        }
                    });

                    lyList.addView(cellItem);
                }

                lyMain.addView(cellContent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    String getCorrectNumberFrom(String strPhone) {
        String strAns = strPhone.replaceAll("\\D+","");



        return strAns;
    }

}
