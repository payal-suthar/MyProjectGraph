package com.e.soilab.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.e.soilab.R;
import com.e.soilab.network.Api;
import com.e.soilab.network.ApiClient;
import com.e.soilab.network.NetworkUtil;
import com.github.mikephil.charting.charts.HorizontalBarChart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

public class AvailableActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    HorizontalBarChart stockChart;
    ArrayList<String> xvalues;
    Timer timer;
    LinkedHashMap<String, String> hours_status = new LinkedHashMap<>();
    TextView txt_VerticalBandSaw, txt_Status, txt_Date, txt_Time;
    ImageView img_info, img_report, img_Touch;
    RecyclerView recyclerView;
    String machin_ID = "5";
    Date date = new Date();
    Context context;
    LinkedHashMap<String,String> start_time_list = new LinkedHashMap<>();
    LinkedHashMap<String,String> end_time_list = new LinkedHashMap<>();
    LinkedHashMap<String,String> time_list = new LinkedHashMap<>();

    ArrayList<Float> endtime = new ArrayList<>();
    ArrayList<Float> starttime = new ArrayList<>();
    private static final String TAG = "AvaliableActivity";
    ArrayList<Float> hours = new ArrayList<>();
    ArrayList<String> machinestatus = new ArrayList<>();
    String statusnew = "";
    Handler mHandler;
    Timer mTimer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_avilable);
        txt_Status = findViewById(R.id.txt_Status);
        initView();
        machineAPI(machin_ID);

        mHandler = new Handler();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // run main thread code
                        SimpleDateFormat sdfd = new SimpleDateFormat("HH.mm");
                        String str = sdfd.format(new Date());
                        Log.d(TAG, "run:first - "+starttime+str);
                        for (int i = 0; i < starttime.size(); i++)
                        {
                            if(i ==starttime.size()-1)
                            {
                                if (Float.parseFloat(str) >= starttime.get(i)){
                                    float aa = starttime.get(i);
                                    String s = Float. toString(aa);
                                    statusnew =  start_time_list.get(s);
                                    if(statusnew.equals("AVAILABLE"))
                                    {
                                        txt_Status.setText(statusnew);
                                        txt_Status.setBackgroundColor(getResources().getColor(R.color.green));
                                    }
                                    else
                                    {
                                        txt_Status.setText(statusnew);
                                        txt_Status.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                                    }
                                }
                            }
                            else
                            {
                                if (Float.parseFloat(str) >= starttime.get(i) && (Float.parseFloat(str) <= starttime.get(i+1)))
                                {
                                    float aa = starttime.get(i);
                                    String s = Float. toString(aa);
                                    statusnew =  start_time_list.get(s);
                                    if(statusnew.equals("AVAILABLE"))
                                    {
                                        txt_Status.setText(statusnew);
                                        txt_Status.setBackgroundColor(getResources().getColor(R.color.green));
                                    }
                                    else
                                    {
                                        txt_Status.setText(statusnew);
                                        txt_Status.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }, 1000, 1000);

        String currentDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        txt_Time.setText(currentTime);
        txt_Date.setText(currentDate + ", " + dayOfTheWeek);
        Log.e("eeeeee", currentTime + " " + currentDate);


    }

    public void initView() {
        txt_VerticalBandSaw = findViewById(R.id.txt_VerticalBand);
        img_info = findViewById(R.id.img_info);
        img_report = findViewById(R.id.img_problem);
        img_Touch = findViewById(R.id.img_Touch);
        txt_Time = findViewById(R.id.txt_Time);
        txt_Date = findViewById(R.id.txt_Day);


        txt_Status.setText(statusnew);
        Calendar now = Calendar.getInstance();
        int cur_hour = now.get(Calendar.HOUR_OF_DAY);
        int cur_minute = now.get(Calendar.MINUTE);

        stockChart = (HorizontalBarChart) findViewById(R.id.barchart);
    }

    public class CategoryBarChartXaxisFormatter implements IAxisValueFormatter
    {
        ArrayList<String> mValues;
        public CategoryBarChartXaxisFormatter(ArrayList<String> values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis)
        {
            int val = (int) value;
            String label = "";

            if (val >= 0 && val < mValues.size())
            {
                label = mValues.get(val);
            }
            else
            {
                label = "";
            }
            return label;
        }
    }

    public  class newBarChartXaxisFormatter implements IAxisValueFormatter
    {
        @SuppressLint({"StringFormatInvalid", "StringFormatMatches"})
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
              int a = (int) (10f + value);
              return String.valueOf(a);
        }
    }


    public void machineAPI(String machin_ID) {
        if (NetworkUtil.isNetworkAvailable(this)) {
            Api api = ApiClient.getClient().create(Api.class);
            Log.d("responseforsthis", "onResponse:1 " + machin_ID);
            Call<ResponseBody> call = api.getMachineDetails(machin_ID);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String JSON_STRING = response.body().string();
                        Log.d("responseforsthis", "onResponse:2 " + response);
                        JSONArray jsonArray = new JSONArray(JSON_STRING);

                     //   Log.d("responseforsthis", "onResponse: " + JSON_STRING);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.optJSONObject(i);
                            Log.e("JSON_OBJECT ", " " + jsonObject);

                            int machine_id = jsonObject.optInt("machine_id");
                            int sch_id = jsonObject.optInt("sch_id");
                            String mach_sch_date = jsonObject.getString("mach_sch_date");
                            int class_sch_day_id = jsonObject.optInt("class_sch_day_id");
                            String mach_sch_time_from = jsonObject.getString("mach_sch_time_from");
                            String mach_sch_time_to = jsonObject.getString("mach_sch_time_to");
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

                            start_time_list.put(mach_sch_time_from, mach_status);
                            end_time_list.put(mach_sch_time_to, mach_status);
                            endtime.add(Float.parseFloat(mach_sch_time_to));
                            starttime.add(Float.parseFloat(mach_sch_time_from));
                            machinestatus.add(mach_status);
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
                            } else
                            {
                                time2 = mach_sch_time_to + "0";
                            }

                            Log.d(TAG, "onResponse: extension - " + time1 + "---" + time2);

                            SimpleDateFormat format = new SimpleDateFormat("HH.mm");
                            Date date1 = format.parse(time1);
                            Date date2 = format.parse(time2);
                            long minutess = timeDiff(date1, date2, TimeUnit.MINUTES);
                            long hoursss = (long) (minutess / 60.00);
                            Log.d(TAG, "onResponse:minutes -date1  "+date1+"-date2 -"+date2);
                            Log.d(TAG, "onResponse:minutes -  "+minutess);
                            String newhours = hoursss+"-"+Math.random();
                            hours.add((float) hoursss);

                            hours_status.put(newhours, mach_status);
                        }

                        Set<String> floats = hours_status.keySet();

                        float total = 0, unavailbleTotal = 0;
                        LinkedHashMap<String, String> updatedMap = new LinkedHashMap<>();

                        int i = 0;
                        for (String key : floats) {
                            String text = hours_status.get(key);
                            if (i == floats.size() - 1) {
                                if (text.equalsIgnoreCase("NOT AVAILABLE")) {
                                    if (total > 0) {
                                        updatedMap.put(total + "-" + Math.random(), "AVAILABLE");
                                    }

                                    String separator = "-";
                                    int sepPos = key.lastIndexOf(separator);
                                    String newkey = key.substring(0, sepPos);

                                    unavailbleTotal +=  Float.parseFloat(newkey);
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
                            else if (text.equalsIgnoreCase("AVAILABLE")) {
                                String separator = "-";
                                int sepPos = key.lastIndexOf(separator);
                                String newkey = key.substring(0, sepPos);

                                total +=  Float.parseFloat(newkey);
                                Log.d(TAG, "onResponse:total - " + total + "-key -" + key);
                                if (unavailbleTotal > 0)
                                {
                                    updatedMap.put(unavailbleTotal + "-" + Math.random(), "NOT AVAILABLE");
                                }
                                unavailbleTotal = 0;
                            } else {
                                if (total > 0)
                                {
                                    updatedMap.put(total + "-" + Math.random(), "AVAILABLE");
                                }
                                String separator = "-";
                                int sepPos = key.lastIndexOf(separator);
                                String newkey = key.substring(0, sepPos);

                                unavailbleTotal +=  Float.parseFloat(newkey);
                                total = 0f;
                            }
                            i++;
                        }

                        Log.d(TAG, "onResponse:updatedmap- " + updatedMap);
                        Set<String> keys = updatedMap.keySet();
                        Log.d(TAG, "onResponse: keys -" + keys);
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
                        for (final Float value : graphvalues) {
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
                        data.setBarWidth(1f);
                        stockChart.invalidate();

                        Legend legend = stockChart.getLegend();
                        LegendEntry legendentry1 = new LegendEntry();
                        legendentry1.label = "Available Slot";
                        legendentry1.formColor = Color.GREEN;

                        LegendEntry legendentry2 = new LegendEntry();
                        legendentry2.label = "UnAvailable Slot";
                        legendentry2.formColor = Color.GRAY;

                        legend.setCustom(Arrays.asList(legendentry1, legendentry2));
                        stockChart.setExtraBottomOffset(10f);
                        stockChart.getLegend().setXEntrySpace(10f);
                        stockChart.getLegend().setYEntrySpace(10f);
                        stockChart.getAxisRight().setDrawGridLines(false);
                        stockChart.getAxisRight().setDrawAxisLine(false);
                        stockChart.getAxisRight().setGranularity(1f);

                        stockChart.setViewPortOffsets(-30f, -30f, -30f, -30f);
                        stockChart.setExtraOffsets(0f, 0f, 0f, 0f);
                        stockChart.getAxisLeft().setEnabled(false); //show y-axis at left
                        stockChart.getAxisRight().setEnabled(true); //hide y-axis at right

                        stockChart.getLegend().setEnabled(false);
                        stockChart.setScaleEnabled(false);
                        stockChart.getAxisRight().setEnabled(true);
                        stockChart.getXAxis().setEnabled(false);
                        stockChart.getXAxis().setDrawAxisLine(false);

                        stockChart.setData(data);
                        stockChart.getAxisRight().setTextColor(Color.WHITE);
                        stockChart.getXAxis().setTextColor(Color.WHITE);
                        stockChart.getLegend().setTextColor(Color.WHITE);

                        stockChart.getDescription().setEnabled(false);
                        stockChart.setFitBars(true);
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

                        stockChart.getAxisRight().setLabelCount(13, true);
                        stockChart.getAxisRight().setDrawLabels(true);

                    stockChart.getAxisRight().setValueFormatter(new newBarChartXaxisFormatter());

//                        CategoryBarChartXaxisFormatter xaxisFormatter = new CategoryBarChartXaxisFormatter(xvalues);
//                        stockChart.getAxisRight().setValueFormatter(xaxisFormatter);

                        stockChart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    } catch (Exception e) {
                        Log.e("Available Activity", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("facing failure error", t.getMessage());
                }
            });
        }
    }

    private static long timeDiff(Date date, Date date2, TimeUnit unit)
    {
        long milliDiff=date2.getTime()-date.getTime();
        Log.d(TAG, "timeDiff: "+milliDiff);
        long unitDiff = unit.convert(milliDiff, TimeUnit.MILLISECONDS);
        return unitDiff;
    }

}



