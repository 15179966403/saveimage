package com.hrc.administrator.test.savebrowseimage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;

import com.hrc.administrator.test.R;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener{
    private List<Integer> imageResIdList=new ArrayList<Integer>();
    private Gallery gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Field[] fields=R.drawable.class.getDeclaredFields();
        for(Field field:fields){
            if(field.getName().startsWith("image")){
                try {
                    imageResIdList.add(field.getInt(R.drawable.class));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        gallery=(Gallery)findViewById(R.id.gallery);
        ImageAdapter imageAdapter=new ImageAdapter(this);
        gallery.setAdapter(imageAdapter);
        Button btnSaveImage=(Button)findViewById(R.id.btnSaveImage);
        Button btnBrowserImage=(Button)findViewById(R.id.btnBrowserImage);
        btnSaveImage.setOnClickListener(this);
        btnBrowserImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try{
            switch (v.getId()){
                //将gallery组件中的图像保存到sd卡的根目录
                case R.id.btnSaveImage:
                    String sdcard= Environment.getExternalStorageDirectory().toString();
                    FileOutputStream fos=new FileOutputStream(sdcard+"/image"+gallery.getSelectedItemPosition()+".jpg");
                    ((BitmapDrawable)getResources().getDrawable(imageResIdList.get(gallery.getSelectedItemPosition()))).getBitmap().compress(Bitmap.CompressFormat.JPEG,50,fos);
                    fos.close();
                    break;
                //显示图像浏览器
                case R.id.btnBrowserImage:
                    Intent intent=new Intent(this,ImageBrowser.class);
                    startActivity(intent);
                    break;
            }
        }catch (Exception e){

        }
    }

    public class ImageAdapter extends BaseAdapter{
        int mGalleryItemBackground;
        private Context mContext;

        public ImageAdapter(Context context){
            mContext=context;
            TypedArray typedArray=obtainStyledAttributes(R.styleable.Gallery);
            mGalleryItemBackground=typedArray.getResourceId(R.styleable.Gallery_android_galleryItemBackground,0);
        }

        @Override
        public int getCount() {
            return imageResIdList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView=new ImageView(mContext);
            imageView.setImageResource(imageResIdList.get(position));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT,Gallery.LayoutParams.WRAP_CONTENT));
            imageView.setBackgroundResource(mGalleryItemBackground);
            return imageView;
        }
    }
}