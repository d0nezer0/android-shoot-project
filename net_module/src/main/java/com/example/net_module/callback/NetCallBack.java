package com.example.net_module.callback;

public interface NetCallBack<T> {
    void onResponseData(T t);

    void onError(String msg);
}
