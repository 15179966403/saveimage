package com.hrc.administrator.test.savebrowseimage;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hrc.administrator.test.R;
import com.hrc.administrator.test.savebrowseimage.filebrowse.FileBrowser;
import com.hrc.administrator.test.savebrowseimage.filebrowse.OnFileBrowserListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Administrator on 2016/12/17.
 */

public class ImageBrowser extends Activity implements OnFileBrowserListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filebrowser);
        FileBrowser fileBrowser=(FileBrowser)findViewById(R.id.filebrowser);
        fileBrowser.setOnFileBrowserListener(this);
    }

    @Override
    public void onFileItemClick(String filename) {
        if(!filename.toLowerCase().endsWith(".jpg")){
            return;
        }
        View view=getLayoutInflater().inflate(R.layout.activity_imagebrowser,null);
        ImageView imageView=(ImageView)view.findViewById(R.id.imageview);
        try{
            FileInputStream fis=new FileInputStream(filename);
            imageView.setImageDrawable(Drawable.createFromStream(fis,filename));
            new AlertDialog.Builder(this).setTitle("浏览图像").setView(view).setPositiveButton("关闭",null).show();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirItemClick(String path) {
        setTitle("当前目录："+path);
    }
}
