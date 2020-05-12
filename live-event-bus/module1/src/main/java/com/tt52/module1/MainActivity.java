package com.tt52.module1;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.tt52.module1_export.event.HelloWorldEvent;
import com.tt52.module1_export.event.MySmartEventBus;

public class MainActivity extends AppCompatActivity {
    private Observer<String> observer = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySmartEventBus.event1().post(new HelloWorldEvent("我是谁？",null));
            }
        });
        Button sendacrossapp = findViewById(R.id.sendacrossapp);
        sendacrossapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySmartEventBus.event1().postAcrossApp(new HelloWorldEvent("给隔壁app发消息",null));
            }
        });

        Button key_test_broadcast = findViewById(R.id.key_test_broadcast);
        key_test_broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveEventBus
                        .get("key_test_broadcast")
                        .postAcrossApp("broadcast msg");
            }
        });
    }

}
