package org.ggxz.shoot.mvp.presenter;

import com.example.common_module.base.mvp.BasePresenter;

import org.ggxz.shoot.mvp.view.activity_view.MainView;

public interface MainPresenter extends BasePresenter<MainView> {

    /*榜单数据*/
    void getTopAllData();


    /*用户信息*/
    void getUserInformationData();


    /*胸环靶设计点数据*/
    void getShootPointData();


    /*射击数据*/
    void getShootRoundData();


    /*射击统计数据*/
    void getShootStatistcsData();


    /*折线图*/
    void getShootChartData();


}
