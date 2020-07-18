package com.e.soilab.network;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api
{

    @FormUrlEncoded
    @POST("API/UserData/GetMachSchListByMachID")
    Call<ResponseBody> getMachineDetails(@Field("machine_id") String machine_id);

    @FormUrlEncoded
    @POST("API/UserData/GetMemProfileAC")
    Call<ResponseBody> getProfile(@Field("mem_id") String mem_id);



}
