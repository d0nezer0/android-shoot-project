package org.ggxz.shoot.mvp.view.activity;

import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common_module.base.mvp.BaseMvpActivity;
import com.example.common_module.db.mode.TopModel;

import org.ggxz.shoot.R;
import org.ggxz.shoot.adapter.TopAdapter;
import org.ggxz.shoot.mvp.presenter.impl.TopPresenterImpl;
import org.ggxz.shoot.mvp.view.activity_view.TopView;

import butterknife.BindView;

public class TopActivity extends BaseMvpActivity<TopPresenterImpl> implements TopView<TopModel> {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.backHomeTv)
    TextView backHomeTv;

    private TopAdapter adapter;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_top);
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new TopAdapter(this);
        recyclerView.setAdapter(adapter);

        /*====click start===*/
        backHomeTv.setOnClickListener(v -> finish());
        /*====click edn===*/
    }

    @Override
    protected void initData() {

    }

    @Override
    protected TopPresenterImpl initInjector() {
        return new TopPresenterImpl();
    }

    @Override
    public void onTopDataSuccess(TopModel topModel) {

    }

    @Override
    public void onTopDataError() {

    }
}
