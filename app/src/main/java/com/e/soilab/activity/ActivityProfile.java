package com.e.soilab.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.e.soilab.R;
import com.e.soilab.model.AvaliableModel;
import com.e.soilab.network.Api;
import com.e.soilab.network.ApiClient;
import com.e.soilab.network.NetworkUtil;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.txusballesteros.widgets.FitChart;
import com.txusballesteros.widgets.FitChartValue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityProfile extends AppCompatActivity {
    PieChart pieChart;
   // CircleImageView image_Person;
    FitChart chartssss;
    private List<AvaliableModel> list = new ArrayList<>();
    LinkedHashMap<String,String> start_time_list = new LinkedHashMap<>();
    LinkedHashMap<String,String> end_time_list = new LinkedHashMap<>();
    LinkedHashMap<String,String> time_list = new LinkedHashMap<>();
    ArrayList<Float> hours = new ArrayList<>();
    LinkedHashMap<String, String> hours_status = new LinkedHashMap<>();
    ArrayList<Float> endtime = new ArrayList<>();
    ArrayList<Float> starttime = new ArrayList<>();
    private static final String TAG = "ProfileActivity";

    ArrayList<String> machinestatus = new ArrayList<>();
    ImageView imageview;
    Legend legend;
    ProgressBar progressBar;
    ImageView img_Report;
    String mem_id = "1020";
    String machin_ID = "5";
    ArrayList<String> xvalues;
    TextView txt_Date,txt_Time,txt_Hour,txt_Min,txt_Second,txt_PersonName,txt_PersonId;

    List<PieEntry> value = new ArrayList<>();
    float f1,f2,f3,f4;
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private Runnable runnable;
    private String mach_sch_time_to,mach_sch_time_from;
    HorizontalBarChart stockChart;
    Timer mTimer;
    List<FitChartValue> values = new ArrayList<>();
    int counter = 0;
    ProgressBar circularProgressbar;
    int pStatus = 0;
    private Handler handler = new Handler();
    TextView tv;
    long start_timemili;
    String start_time;
    CountDownTimer mCountDownTimer;
    int i=0;
    ProgressBar mProgress;
    long min;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_time);
        try
        {
            initView();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        profileAPI(mem_id);
        machineAPI(machin_ID);

        img_Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ActivityProfile.this, ActivitySelectProblem.class);
                startActivity(intent);
            }
        });
    }

    public void initView() throws ParseException {
        pieChart = findViewById(R.id.pieChart);
        img_Report = findViewById(R.id.img_report);
        txt_Date = findViewById(R.id.txt_Date);
        txt_Time = findViewById(R.id.txt_Time);
        txt_Hour = findViewById(R.id.txt_Num_Hrs);
        txt_Min = findViewById(R.id.txt_Num_Min);
        txt_Second = findViewById(R.id.txt_Num_Sec);
        txt_PersonName = findViewById(R.id.txt_PersonName);
        txt_PersonId = findViewById(R.id.txt_PersonID);
        imageview = findViewById(R.id.imageView1);
        tv = findViewById(R.id.tv);

        String currentDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        txt_Time.setText(currentTime);
        txt_Date.setText(currentDate + ", " + dayOfTheWeek);

        Date date = new Date();
        final long timeMilli = date.getTime();

        start_timemili = timeMilli;
        start_time = currentTime;

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular);
        ProgressBar mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
        mProgress.setProgress(0);
        mProgress.setSecondaryProgress(100);
        mProgress.setMax(100);
        mProgress.setProgressDrawable(drawable);
        stockChart = (HorizontalBarChart) findViewById(R.id.barchart);

        String rem_time = "02.34";

        SimpleDateFormat format = new SimpleDateFormat("HH.mm");
        Date rem_timenew = format.parse(rem_time);

        String hournew = rem_time.substring(0, 2);
        Log.d("timinghours", "initView: " + hournew);


//        Log.d("timinghours", "initView: "+minnew);
//
//        int hour_in_sec = Integer.parseInt(hournew) * 3600;
//        int min_in_sec = Integer.parseInt(minnew) * 1200;

        //    final int time_remaining = hour_in_sec + min_in_sec;
        //   Toast.makeText(this, "hours -"+hour_in_sec+" mins -"+min_in_sec+" "+time_remaining, Toast.LENGTH_LONG).show();

        String minnew = rem_time.substring(3, 5);

        int minute = Integer.parseInt(minnew);
        min = minute * 60 * 1000;
        counter(min);

        mProgress.setProgress(i);
//        mCountDownTimer = new CountDownTimer(time_remaining,1000) {
//            @Override
//            public void onTick(long millisUntilFinished)
//            {
//                i++;           // Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
//                mProgress.setProgress((int)i*100/(time_remaining/1000));
//              //  tv.setText(millisUntilFinished+" seconds remaining ");
//                int seconds = (int) (millisUntilFinished / 1000) % 60;
//                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
//                int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
//                tv.setText(String.format("%d:%d:%d", hours, minutes, seconds));
//
//            }
//            @Override
//            public void onFinish() {
//                i++;
//                mProgress.setProgress(100);
//            }
//           };
//        mCountDownTimer.start();
//    }
    }

    private void counter(final long min) {
        CountDownTimer timer = new CountDownTimer(min, 1000) {
            public void onTick(long millisUntilFinished) {
                i++;           // Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                mProgress.setProgress((int) ((int)i*100/(min/1000)));

                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                tv.setText(String.format("%d:%d:%d", hours, minutes, seconds));
            }
            public void onFinish() {
                i++;
                mProgress.setProgress(100);
            }
        };
        timer.start();
    }

    public void machineAPI(String machin_ID)
    {
        if (NetworkUtil.isNetworkAvailable(this)) {
            Api api = ApiClient.getClient().create(Api.class);
            Call<ResponseBody> call = api.getMachineDetails(machin_ID);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String JSON_STRING = response.body().string();
                        JSONArray jsonArray = new JSONArray(JSON_STRING);
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject = jsonArray.optJSONObject(j);
                            Log.e("JSON_OBJECT ", " " + jsonObject);
                            int machine_id = jsonObject.optInt("machine_id");
                            int sch_id = jsonObject.optInt("sch_id");
                            String mach_sch_date = jsonObject.getString("mach_sch_date");
                            int class_sch_day_id = jsonObject.optInt("class_sch_day_id");
                            mach_sch_time_from = jsonObject.getString("mach_sch_time_from");
                            mach_sch_time_to = jsonObject.getString("mach_sch_time_to");
                            String mach_status = jsonObject.getString("mach_status");
                            int mach_sch_isactive = jsonObject.optInt("mach_sch_isactive");
                            int mach_sch_iscreatedon = jsonObject.optInt("mach_sch_iscreatedon");
                            int mach_sch_iscreatedby = jsonObject.optInt("mach_sch_iscreatedby");
                            int mach_sch_isupdatedon = jsonObject.optInt("mach_sch_isupdatedon");
                            int emp_id = jsonObject.optInt("emp_id");
                            int machine_region_id = jsonObject.optInt("machine_region_id");
                            String status = jsonObject.getString("status");
                            int message = jsonObject.optInt("message");
                            int mach_green_uri = jsonObject.optInt("mach_green_uri");
                            int mach_purple_uri = jsonObject.optInt("mach_purple_uri");
                            String start_time_user = jsonObject.optString("start_time");
                            String end_time_user = jsonObject.optString("end_time");

                            machinestatus.add(mach_status);
                            String currentDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
                            String currentTime = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());
                            Log.e("1111111eeeeee", currentTime + " " + currentDate);

                            list.add(new AvaliableModel(mach_sch_time_from, mach_status));
                            start_time_list.put(mach_sch_time_from, mach_status);
                            end_time_list.put(mach_sch_time_to, mach_status);
                            endtime.add(Float.parseFloat(mach_sch_time_to));
                            starttime.add(Float.parseFloat(mach_sch_time_from));
                            time_list.put(mach_sch_time_from + " - " + mach_sch_time_to, mach_status);

                            String time1 = mach_sch_time_from + "0";
                            String time2 = mach_sch_time_to + "0";
                            String extension = time1.substring(time1.lastIndexOf(".") + 1);
                            String extension2 = time2.substring(time2.lastIndexOf(".") + 1);
                            if (extension.length() > 2)
                            {
                                time1 = mach_sch_time_from;
                            }
                            else
                            {
                                time1 = mach_sch_time_from + "0";
                            }

                            if (extension2.length() > 2)
                            {
                                time2 = mach_sch_time_to;
                            }
                            else
                            {
                                time2 = mach_sch_time_to + "0";
                            }
                            SimpleDateFormat format = new SimpleDateFormat("HH.mm");
                            Date date1 = format.parse(time1);
                            Date date2 = format.parse(time2);
                            long minutess = timeDiff(date1, date2, TimeUnit.MINUTES);
                            float hoursss = (float) (minutess / 60.0);
                            String newhours = hoursss+"-"+Math.random();
                            Log.d(TAG, "onResponse: " + time1 + "--" + time2 + "--" + hoursss);
                            hours.add(hoursss);
                            hours_status.put(newhours, mach_status);

                            Log.d(TAG, "onResponse: " + time_list );
                            Log.d(TAG,  "-hours -" + hours );
                            Log.d(TAG, "onResponse: " + time_list + "-hours -" + hours + "-hourstatus -" + hours_status);

                            Set<String> floats = hours_status.keySet();
                            float total = 0, unavailbleTotal = 0;
                            LinkedHashMap<String, String> updatedMap = new LinkedHashMap<>();

                        int i = 0;
                        for (String key : floats)
                        {
                            String text = hours_status.get(key);
                            if (i == floats.size() - 1)
                            {
                                if (text.equalsIgnoreCase("NOT AVAILABLE"))
                                {
                                    if (total > 0)
                                    {
                                        updatedMap.put(total + "-" + Math.random(), "AVAILABLE");
                                    }
                                    String separator = "-";
                                    int sepPos = key.lastIndexOf(separator);
                                    String newkey = key.substring(0, sepPos);

                                    unavailbleTotal += Float.parseFloat(newkey);
                                    updatedMap.put(unavailbleTotal + "-" + Math.random(), text);
                                    total = 0;
                                }
                                else
                                {
                                    if (unavailbleTotal > 0)
                                    {
                                        updatedMap.put(unavailbleTotal + "-" + Math.random(), "NOT AVAILABLE");
                                    }
                                    String separator = "-";
                                    int sepPos = key.lastIndexOf(separator);
                                    String newkey = key.substring(0, sepPos);

                                    total += Float.parseFloat(newkey);
                                    updatedMap.put(total + "-" + Math.random(), "AVAILABLE");
                                    unavailbleTotal = 0;
                                }
                            }
                            else if (text.equalsIgnoreCase("AVAILABLE"))
                            {
                                String separator = "-";
                                int sepPos = key.lastIndexOf(separator);
                                String newkey = key.substring(0, sepPos);

                                total += Float.parseFloat(newkey);
                                Log.d(TAG, "onResponse: " + total + "--" + key);
                                if (unavailbleTotal > 0)
                                {
                                    updatedMap.put(unavailbleTotal + "-" + Math.random(), "NOT AVAILABLE");
                                }
                                unavailbleTotal = 0;
                            }
                            else
                            {
                                if (total > 0)
                                {
                                    updatedMap.put(total + "-" + Math.random(), "AVAILABLE");
                                }
                                String separator = "-";
                                int sepPos = key.lastIndexOf(separator);
                                String newkey = key.substring(0, sepPos);

                                unavailbleTotal += Float.parseFloat(newkey);
                                total = 0f;
                            }
                            i++;
                        }
                        Log.d(TAG, "onResponse:updatedmap- " + updatedMap);

                        Set<String> keys = updatedMap.keySet();
                        ArrayList<Float> graphvalues = new ArrayList<>();

                        for (String str : keys)
                        {
                            String separator = "-";
                            int sepPos = str.lastIndexOf(separator);
                            String valuesforgraph = str.substring(0, sepPos);
                            graphvalues.add(Float.parseFloat(valuesforgraph));
                        }

                        final float[] arr = new float[graphvalues.size()];
                        int index = 0;
                        for (final Float value : graphvalues)
                        {
                            arr[index++] = value;
                        }

                        int StockColors[] = new int[0];

                        for(int k = 0; k<1;k++)
                        {
                            String first_status = machinestatus.get(k);
                            if (first_status.equals("AVAILABLE"))
                            {
                                 StockColors = new int[]{Color.parseColor("#24E224"), Color.parseColor("#A9A9A9")};
                            }
                            else
                            {
                                StockColors = new int[]{Color.parseColor("#A9A9A9"), Color.parseColor("#24E224")};
                            }
                        }

                        ArrayList<BarEntry> entries = new ArrayList<>();
                        entries.add(new BarEntry(0f, arr));
                        BarDataSet bardataset = new BarDataSet(entries, "");
                        bardataset.setColors(StockColors);
                        bardataset.setDrawValues(false);
                        stockChart.getAxisRight().setCenterAxisLabels(true);
                        BarData data = new BarData(bardataset);
                        data.setBarWidth(0.3f);
                        stockChart.invalidate();
                        stockChart.getLegend().setEnabled(false);
                        stockChart.getAxisRight().setDrawGridLines(false);
                        stockChart.getAxisRight().setDrawAxisLine(false);
                        stockChart.getAxisRight().setGranularity(1f);

                        stockChart.setViewPortOffsets(0f, 0f, 0f, 0f);
                        stockChart.setExtraOffsets(0f, 0f, 0f, 0f);

                        stockChart.getAxisLeft().setEnabled(false); //show y-axis at left
                        stockChart.getAxisRight().setEnabled(true); //hide y-axis at right

                        stockChart.setScaleEnabled(false);
                        stockChart.getAxisRight().setEnabled(true);
                        stockChart.getXAxis().setEnabled(false);
                        stockChart.getXAxis().setDrawAxisLine(false);

                        stockChart.setData(data);
                        stockChart.getAxisRight().setTextColor(Color.WHITE);
                        stockChart.getXAxis().setTextColor(Color.WHITE);
                        stockChart.getLegend().setTextColor(Color.WHITE);

                        stockChart.getDescription().setEnabled(false);
                        stockChart.setFitBars(false);
                        stockChart.setTouchEnabled(false);

                        stockChart.setDrawGridBackground(false);
                        stockChart.setDrawBarShadow(false);
                        stockChart.setDrawValueAboveBar(false);

                        xvalues = new ArrayList<>();
                        xvalues.add("9.00");
                        xvalues.add("10.00");
                        xvalues.add("11.00");
                        xvalues.add("12.00");
                        xvalues.add("13.00");
                        xvalues.add("14.00");
                        xvalues.add("15.00");
                        xvalues.add("16.00");
                        xvalues.add("17.00");
                        xvalues.add("18.00");
                        xvalues.add("19.00");
                        xvalues.add("20.00");
                        xvalues.add("21.00");

                        stockChart.getAxisRight().setLabelCount(xvalues.size(), true);
                        stockChart.getAxisRight().setDrawLabels(true);
                        stockChart.getAxisRight().setValueFormatter(new newBarChartXaxisFormatter());
                        stockChart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {

                            }
                        });
                        }
                    }
                    catch (Exception e)
                    {
                        Log.e("Available Activity", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t)
                {
                    Log.e("facing failure error", t.getMessage());
                }
            });
        } else
        {

        }
    }

    public static class newBarChartXaxisFormatter implements IAxisValueFormatter
    {
        @SuppressLint("StringFormatInvalid")
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            int a = (int) (9f + value);

            return String.valueOf(a);
        }
    }

    private static long timeDiff(Date date, Date date2, TimeUnit unit)
    {
        long milliDiff=date2.getTime()-date.getTime();
        long unitDiff = unit.convert(milliDiff, TimeUnit.MILLISECONDS);
        return unitDiff;
    }

    public void profileAPI(String mem_id)
    {
        if (NetworkUtil.isNetworkAvailable(this)) {
            Api api = ApiClient.getClient().create(Api.class);
            Call<ResponseBody> call = api.getProfile(mem_id);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String JSON_STRING = response.body().string();
                        Log.d("hellooutput", "onResponse:1- "+response);
                        JSONObject jsonObject = new JSONObject(JSON_STRING);

                        Log.d("hellooutput", "onResponse: "+JSON_STRING);
                            Log.e("JSON_OBJECT ", " " + jsonObject);
                            String mem_name = jsonObject.getString("mem_name");
                            String mem_emiratesid = jsonObject.getString("mem_emiratesid");
                            String mem_pic_uri = jsonObject.getString("mem_pic_uri");
                            String message = jsonObject.getString("message");
                            String status = jsonObject.getString("status");

                            Log.e("In API ", " " + mem_name+" "+mem_emiratesid+" "+""+mem_pic_uri+""+""+status+""+message+"");

                            txt_PersonName.setText(mem_name);
                            txt_PersonId.setText(mem_emiratesid);

                        Glide.with(getApplicationContext())
                                .load(mem_pic_uri)
                                .apply(new RequestOptions().placeholder(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)).into(imageview);

                    }
                    catch (Exception e) {
                        Log.e("Profile Activity", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("facing failure error", t.getMessage());
                }

            });
        } else {

        }
    }
}

