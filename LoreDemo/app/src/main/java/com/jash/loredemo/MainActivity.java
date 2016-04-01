package com.jash.loredemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.jash.tools.network.NetworkLib;
import com.jash.tools.network.NetworkTask;
import com.jash.tools.viewutil.CommonAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NetworkTask.Callback<Response>{

    private CommonAdapter<Lore> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.main_list);
        adapter = new CommonAdapter<>(this, new ArrayList<Lore>(), Lore.class, R.layout.item);
        listView.setAdapter(adapter);
        LoreService service = NetworkLib.getService(LoreService.class);
        service.getLoreList(0, 1, 20).execute(this);
    }

    @Override
    public void onResponse(Response response) {
        adapter.addAll(response.getLores());
    }

    @Override
    public void onFailure(Exception e) {
        Toast.makeText(this, "网络问题", Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }
}
