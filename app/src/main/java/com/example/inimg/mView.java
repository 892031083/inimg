package com.example.inimg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class mView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    int vw,vh;
    int spac;//间距
    int imgW;
    Bitmap testbit;
    SurfaceHolder surfaceHolder;
    Paint paint;
    String fileName;
    public mView(Context context) {
        super(context);
        init(context);
    }

    public mView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public mView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    TextPaint textPaint;
    private void init(Context context) {
        Log.i("===========","pppppppppp");

        testbit= BitmapFactory.decodeResource(context.getResources(),R.drawable.test);
        surfaceHolder=getHolder();
        surfaceHolder.addCallback(this);
        paint=new Paint();
        paint.setStrokeWidth(2);

       // paint.setTextSize(30);

         textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(20);
        textPaint.setAntiAlias(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        vw=getMeasuredWidth();
       // vh=getMeasuredHeight();
        spac=vw/12;
        imgW=(vw-spac*5)/4;


        new Thread(this).start();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    int x,y;
    @Override
    public void run() {
        List<String> list=getFilesAllName( Environment.getExternalStorageDirectory().getPath() + "/商品图/1-24/1-24");
        if (list==null) return;


        for (int j=0;j<list.size()/26;j++){
            Bitmap bitmap=Bitmap.createBitmap(vw,(imgW+spac)*12+100, Bitmap.Config.ARGB_8888);
            Canvas canvas=new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            x=spac;
            y=spac;
            int a=0;
            for (int i=0;i<26;i++){
                Log.i("aaaa",""+i+"==="+list.get(i));
                int index=j*26+i;
                testbit=BitmapFactory.decodeFile(list.get(index));
                Bitmap bitmap1 =getRatioBitmap(testbit,(float)imgW/testbit.getWidth(),(float)imgW/testbit.getHeight());
                canvas.drawBitmap( bitmap1,x,y,null);
                String text=list.get(index);

                text=text.substring(text.lastIndexOf("/")+1,text.lastIndexOf("."));
                StaticLayout layoutopen = new StaticLayout(text, textPaint, (int) imgW , Layout.Alignment.ALIGN_NORMAL,
                        1.0F, 0.0F, true);
                canvas.save();
                canvas.translate(x,y+imgW+10);
                layoutopen.draw(canvas);
                canvas.restore();
               // canvas.drawText(text,x,y+imgW+30,paint);
                x=x+imgW+spac;

                if (a%4==0&&a!=0){
                    x=spac;
                    y=y+imgW+spac+130;
                }  else  if(i==3){
                    x=spac;
                    a=0;
                    y=y+imgW+spac+130;
                }
                a++;
            }

            if (imgLintener!=null){
                imgLintener.ok(bitmap);
            }
        }

    }
    ImgLintener imgLintener;

    public void setImgLintener(ImgLintener imgLintener) {
        this.imgLintener = imgLintener;
    }
    public Bitmap getRatioBitmap(Bitmap bitmap,float dx,float dy){//获取 dx dy缩放比例后的bitmap
        return Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*dx),(int)(bitmap.getHeight()*dy),true);
    }
    interface ImgLintener{
        void ok(Bitmap bitmap);
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
}
