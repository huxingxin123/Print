package com.example.print3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.posapi.PosApi;
import android.posapi.PrintQueue;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.print3.util.BitmapTools;
import com.example.print3.util.PowerUtil;
import com.example.print3.util.TextToBitMap;

import java.io.ByteArrayOutputStream;

public class TestActivity extends AppCompatActivity implements PrintQueue.OnPrintListener, AdapterView.OnItemSelectedListener {
    Button tbutton;
    PosApi mposApi;
    private PrintQueue printQueue;
    private PrintQueue printQueue1;
    private PrintQueue printQueue2;
    private String path = "/dev/ttyS2";
    public static String PARAM_ALIGN,PARAM_TEXTSIZE;
    private ImageView iv;
    Bitmap mBitmap;

    @Override
    protected void onResume() {
        super.onResume();
        PowerUtil.power("1");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initview();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mposApi != null) {
            mposApi.closeDev();
        }
        // Power off
        PowerUtil.power("0");
    }

    private void initview() {
        tbutton=(Button) findViewById(R.id.tbtn);
        mBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.ma);
        mposApi=PosApi.getInstance(this);
        mposApi.initDeviceEx(path);
        printQueue = new PrintQueue(this, mposApi);
        printQueue.init();
        tbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMachine();
            }
        });
    }
    private void initMachine(){
        mposApi.setOnComEventListener(new PosApi.OnCommEventListener() {
            @Override
            public void onCommState(int i, int i1, byte[] bytes, int i2) {

            }
        });

        initPrint();
    }

    private void initPrint() {
        int concentration = 35;

        //标题大写字符
        PrintQueue.TextData textData = printQueue.new TextData();
        StringBuilder sb = new StringBuilder();
        sb.append("铜梁路内停车\n");
        textData.addParam(PrintQueue.PARAM_TEXTSIZE_2X);
        textData.addParam(PrintQueue.PARAM_ALIGN_MIDDLE);
        textData.addText(sb.toString());
        printQueue.addText(concentration, textData);


        //内容小写字体
        PrintQueue.TextData textData1=printQueue.new TextData();
        StringBuilder sb1=new StringBuilder();
        sb1.append("车牌号：无车牌\n");
        sb1.append("车位号：0452\n");
        sb1.append("停车时间：2019-10-21     22:35\n");
        sb1.append("已缴纳金额：0元\n");
        sb1.append("收费员：cs101\n");
        sb1.append("-------------------------------\n");
        sb1.append("\n");
        sb1.append("收费时间:08:00-21:00\n");
        sb1.append("\n");
        sb1.append("计费规则：重要商圈：每小时2元/车位，全天单位停车封顶20元，非重要商圈：每小时1元/车位，全天单位停车封顶10元\n");
        sb1.append("-------------------------------\n");
        sb1.append("\n");
        textData1.addText(sb1.toString());
        textData1.addParam(PrintQueue.PARAM_TEXTSIZE_24);
        textData1.addParam(PrintQueue.PARAM_ALIGN_LEFT);
        printQueue.addText(concentration,textData1);

        //第三个内容大写
        PrintQueue.TextData textData2=printQueue.new TextData();
        StringBuilder sb2=new StringBuilder();
        sb2.append("请仔细核对小票信息、车牌号码后再使用微信扫码支付\n");

        textData2.addText(sb2.toString());
        textData2.addParam(PrintQueue.PARAM_TEXTSIZE_2X);
        textData2.addParam(PrintQueue.PARAM_ALIGN_LEFT);
        printQueue.addText(concentration,textData2);

        //第四个图片打印



        Log.d ("printData", "img3: " + "image info" + (mBitmap.getByteCount () / 1024 / 1024) + "Width:" + mBitmap.getWidth () + "Height:" + mBitmap.getHeight () + "size");
        byte[] printData=BitmapTools.bitmap2PrinterBytes (mBitmap);
        printQueue.addBmp (concentration, 10, mBitmap.getWidth (), mBitmap.getHeight (), printData);

        //第五个字符的打印
        PrintQueue.TextData textData3=printQueue.new TextData();
        StringBuilder sb3=new StringBuilder();
        sb3.append("\n");
        sb3.append("请关好车窗，保管好财物\n");
        sb3.append("城投物业公司\n");
        sb3.append("\n");
        sb3.append("\n");
        textData3.addText(sb3.toString());
        textData3.addParam(PrintQueue.PARAM_TEXTSIZE_24);
        textData3.addParam(PrintQueue.PARAM_ALIGN_MIDDLE);
        printQueue.addText(concentration,textData3);

        //启动打印
        printQueue.printStart();
    }



    @Override
    public void onFailed(int i) {

        Toast.makeText(TestActivity.this, "打印失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish() {
        Toast.makeText(TestActivity.this, "打印成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetState(int i) {

    }

    @Override
    public void onPrinterSetting(int i) {

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
