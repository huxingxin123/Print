package com.example.print3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.posapi.PosApi;
import android.posapi.PrintQueue;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.print3.util.PowerUtil;
import com.example.print3.util.SystemUtils;

public class MainActivity extends AppCompatActivity implements PrintQueue.OnPrintListener {
    private Button nextButton;
    private Button button;
    private PrintQueue mPrintQueue;
    private PosApi mPosApi;
    private String PARAM_TEXTSIZE, PARAM_ALIGN;

    private PosApi mApi = null;

    private String path = "/dev/ttyS2";


    @Override
    protected void onResume() {
        super.onResume();

        PowerUtil.power("1");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //  init Device
        tonextbutton();
//        initPrint();
//        initView();
    }

    private void tonextbutton() {
        nextButton=(Button) findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,TestActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initPrint(){
        mApi = MyApplication.getInstance().getPosApi();
        mApi.setOnComEventListener(mCommEventListener);
        mApi.initDeviceEx(path);
        mApi.setOnComEventListener(new PosApi.OnCommEventListener() {
            @Override
            public void onCommState(int i, int i1, byte[] bytes, int i2) {

            }
        });
        mPosApi=MyApplication.getInstance().getPosApi();
        mPrintQueue=new PrintQueue (this, mPosApi);
        mPrintQueue.init();
        mPrintQueue.setOnPrintListener (this);// Set up print listening

        PARAM_TEXTSIZE=PrintQueue.PARAM_TEXTSIZE_16;
        PARAM_ALIGN=PrintQueue.PARAM_ALIGN_LEFT;
    }
    private void initView(){
        button = findViewById(R.id.btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print();
            }
        });
    }

    private void print(){

        PrintQueue.TextData tData=mPrintQueue.new TextData ();
        tData.addText ("打印小票");// add print content
        tData.addParam (PARAM_TEXTSIZE);// font size set
        tData.addParam (PARAM_ALIGN);
        mPrintQueue.addText (10, tData);// add to printQueue
        StringBuilder sb=new StringBuilder ();
        sb.append ("\n");
        sb.append ("\n");
        sb.append ("\n");
        tData=mPrintQueue.new TextData ();// constructor TextData living example
        tData.addText (sb.toString ());// add print content
        tData.addParam(PARAM_TEXTSIZE);// font size set
        mPrintQueue.addText (10, tData);// add to printQueue
        mPrintQueue.printStart ();
    }

    PosApi.OnCommEventListener mCommEventListener = new PosApi.OnCommEventListener() {

        @Override
        public void onCommState(int cmdFlag, int state, byte[] resp, int respLen) {
            // TODO Auto-generated method stub
            switch (cmdFlag) {
                case PosApi.POS_INIT:
                    if (state == PosApi.COMM_STATUS_SUCCESS) {
                        Toast.makeText(getApplicationContext(), "Initialization success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to initialize", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    public void onFailed(int i) {
        Toast.makeText(this, "打印失败", Toast.LENGTH_SHORT).show();
        Log.d("ddddd","打印失败 错误代码："+i);
    }

    @Override
    public void onFinish() {
        Toast.makeText(this, "打印完成", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetState(int i) {

    }

    @Override
    public void onPrinterSetting(int i) {

    }
}
