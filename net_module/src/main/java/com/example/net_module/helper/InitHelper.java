package com.example.net_module.helper;


import android.util.Log;

import com.example.common_module.db.mode.SingleShootDataModel;
import com.example.net_module.callback.NetCallBack;
import com.example.net_module.mode.InitModeData;
import com.example.net_module.mode.TopUser;
import com.example.net_module.net.Network;
import com.example.net_module.net.RemoteService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InitHelper {

    public static void init(final NetCallBack<InitModeData> callBack) {
        RemoteService service = Network.remote();
        Call<InitModeData> call = service.init(android.os.Build.SERIAL);
        call.enqueue(new Callback<InitModeData>() {
            @Override
            public void onResponse(Call<InitModeData> call, Response<InitModeData> response) {
                callBack.onResponseData(response.body());
            }

            @Override
            public void onFailure(Call<InitModeData> call, Throwable t) {
                callBack.onError(t.getMessage());

            }
        });

    }

    public static void postShootData(SingleShootDataModel dataModel) {
        RemoteService service = Network.remote();
        Call<String> call = service.postSingleData(dataModel);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("Net", "upload success");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Net", "upload error " + t.getMessage());

            }
        });
    }

    public static void getTopTypeList(final NetCallBack<TopUser> callBack) {
        RemoteService service = Network.remote();
        Call<TopUser> call = service.getTopTypeList();
        call.enqueue(new Callback<TopUser>() {
            @Override
            public void onResponse(Call<TopUser> call, Response<TopUser> response) {
                callBack.onResponseData(response.body());
            }

            @Override
            public void onFailure(Call<TopUser> call, Throwable t) {
            Log.e("Net", "getTopTypeList error " + t.getMessage());
            }
        });
    }
}
