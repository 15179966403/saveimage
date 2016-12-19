package com.hrc.administrator.test.contacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hrc.administrator.test.R;
import com.hrc.administrator.test.savebrowseimage.filebrowse.FileBrowser;
import com.hrc.administrator.test.savebrowseimage.filebrowse.OnFileBrowserListener;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Administrator on 2016/12/19.
 */

public class AddContact extends Activity implements View.OnClickListener,OnFileBrowserListener,MenuItem.OnMenuItemClickListener{
    private FileBrowser fileBrowser;
    private View fileBrowserView;
    private AlertDialog alertDialog;
    private EditText etName;
    private EditText etTelephone;
    private EditText etEmail;
    private ImageView ivPhoto;
    private String photoFilename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_addcontacts);
        Button btnSelectPhoto=(Button)findViewById(R.id.btnSelectPhoto);
        btnSelectPhoto.setOnClickListener(this);
        etName=(EditText)findViewById(R.id.etName);
        etTelephone=(EditText)findViewById(R.id.etTelphone);
        etEmail=(EditText)findViewById(R.id.etEmail);
        ivPhoto=(ImageView) findViewById(R.id.ivPhoto);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("保存").setOnMenuItemClickListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String sql="insert into t_contacts(name,telephone,email,photo) values(?,?,?,?)";
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ((BitmapDrawable)ivPhoto.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG,50,baos);
        Object[] args=new Object[]{etName.getText(),etTelephone.getText(),etEmail.getText(),baos.toByteArray()};

        return false;
    }

    @Override
    public void onClick(View v) {
        fileBrowserView=getLayoutInflater().inflate(R.layout.filebrowser,null);
        fileBrowser=(FileBrowser)fileBrowserView.findViewById(R.id.filebrowser);
        fileBrowser.setOnFileBrowserListener(this);
        alertDialog=new AlertDialog.Builder(this).setTitle("选择联系人头像")
                .setIcon(R.drawable.image10).setView(fileBrowserView)
                .setPositiveButton("关闭",null).create();
        alertDialog.show();
    }

    @Override
    public void onFileItemClick(String filename) {
        if(filename.toLowerCase().endsWith("jpg")||filename.toLowerCase().endsWith("jpeg")){
            try{
                FileInputStream fis=new FileInputStream(filename);
                ivPhoto.setImageDrawable(Drawable.createFromStream(fis,filename));
                alertDialog.dismiss();
                photoFilename=filename;
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDirItemClick(String path) {

    }
}
