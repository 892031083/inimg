package com.example.inimg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Runnable {
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         bitmap= BitmapFactory.decodeResource(this.getResources(),R.drawable.test);
       //  new Thread(this).start();
       mView view= findViewById(R.id.views);
       view.setImgLintener(new mView.ImgLintener() {
           @Override
           public void ok(Bitmap bitmap) {
               saveImagetoTK(bitmap,"ssss");
           }
       });
        List<String> list=getFilesAllName( Environment.getExternalStorageDirectory().getPath() + "/商品图" +
                "");
        for (String str:list){
            Log.i("=====",str);
        }
    }


    public static List<String> getFilesAllName(String path) {

        File file=new File(path);
        File[] files=file.listFiles();
        if (files == null){
            Log.e("error","空目录");return null;}
        List<String> s = new ArrayList<>();
        for(int i =0;i<files.length;i++){
            s.add(files[i].getAbsolutePath());
        }
        return s;
    }
    String fileName;


    private void saveImagetoTK(Bitmap bit,String name) {
        /**
         * Environment.getExternalStorageDirectory().getPath() //获取外部存储的根目录   DCIM是图库目录
         */
        File file ;
        try {

            if (Build.BRAND.equals("Xiaomi")) { // 小米手机
                fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + name;//
            } else {  // Meizu 、Oppo
                fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + name;//保存文件的路径
            }
            file = new File(fileName);
            // imgurl=fileName;
            if (file.exists()) {
                //file.createNewFile();
                file.delete();
            }
            FileOutputStream out=new FileOutputStream(file);//文件输入流
            bit.compress(Bitmap.CompressFormat.JPEG,100,out);//亚索图片函数 第一个参数是文件格式 第二个参数是图片压缩  第三个是输出流
            out.flush();
            out.close();
            MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), name, null);//插入相册
            //发送广播 提示图册更新
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+fileName)));

            runOnUiThread(new Runnable() {
                @Override
                public void run () {
                    ((TextView)findViewById(R.id.t)).setText("ok!!");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        Bitmap bit=Bitmap.createBitmap(1080,3000, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bit);
        canvas.drawColor(Color.WHITE);
        int w=1080/10;
        int x=1080/10,y=1080/10;
        Paint paint=new Paint();
        int imgWH=(1080-4*108)/3;
        paint.setStrokeWidth(2);

        paint.setTextSize(30);
        Bitmap bit1=Bitmap.createScaledBitmap(bitmap,imgWH/bitmap.getWidth(),imgWH/bitmap.getHeight(),true);
        for (int i=0;i<50;i++){
            Matrix matrix=new Matrix();
            matrix.setScale(0.2f,0.2f  );
            matrix.postTranslate(x,y);
          canvas.drawBitmap(bitmap,matrix,null);
          canvas.drawText("孙悟空",x,y+imgWH+w,paint);
          x=x+w+imgWH;
          if (i%4==0&&i!=0){
              x=w;
              y=y+w+imgWH;
          }
        }
        saveImagetoTK(bit,"产品图test");
    }
}
