package com.github.android.flux;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.github.android.flux.actions.ActionsCreator;
import com.github.android.flux.dispatcher.Dispatcher;
import com.github.android.flux.stores.SingerStore;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Dispatcher dispatcher;
    private ActionsCreator actionsCreator;
    private SingerStore singerStore;

    private Button btnAdd;
    private RecyclerView recyclerView;
    private SingerRecyclerAdapter adapter;
    private DividerItemDecoration mDividerItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDependencies();
        setUpView();
    }

    private void initDependencies() {
        dispatcher = Dispatcher.get(new Bus());
        actionsCreator = ActionsCreator.get(dispatcher);
        singerStore = SingerStore.get(dispatcher);
        dispatcher.register(this);
        dispatcher.register(singerStore);
    }

    private void setUpView() {
        recyclerView = (RecyclerView) findViewById(R.id.singer_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);
        adapter = new SingerRecyclerAdapter(actionsCreator);
        recyclerView.setAdapter(adapter);
        btnAdd = (Button) findViewById(R.id.add);
        btnAdd.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispatcher.unregister(this);
        dispatcher.unregister(singerStore);
    }

    @Override
    public void onClick(View v) {
        if (v == btnAdd) {
            startActivity(new Intent(this, FillInfoActivity.class));
        }
    }

    @Subscribe
    public void onSingerStoreChange(SingerStore.SingerStoreChangeEvent event) {
        updateUI();
    }

    private void updateUI() {
        adapter.setItems(singerStore.getSingers());
    }
}
