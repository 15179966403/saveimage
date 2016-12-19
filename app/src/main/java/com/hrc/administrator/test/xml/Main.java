package com.hrc.administrator.test.xml;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;

import com.hrc.administrator.test.R;
import com.hrc.administrator.test.savebrowseimage.filebrowse.FileBrowser;
import com.hrc.administrator.test.savebrowseimage.filebrowse.OnFileBrowserListener;

import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/12/18.
 */

public class Main extends Activity implements OnFileBrowserListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filebrowser);
        FileBrowser fileBrowser=(FileBrowser)findViewById(R.id.filebrowser);
        fileBrowser.setOnFileBrowserListener(this);
    }

    @Override
    public void onFileItemClick(String filename) {
        try{
            if(!filename.toLowerCase().endsWith(".xml")){
                return;
            }
            Log.d("onFileItemClick","点击的文件路径："+filename);
            FileInputStream fis=new FileInputStream(filename);
            XML2Product xml2Product=new XML2Product();
            android.util.Xml.parse(fis, Xml.Encoding.UTF_8,xml2Product);
            List<Product> products=xml2Product.getProducts();
            String msg="共"+products.size()+"个产品\n";
            for (Product product:products){
                msg+="id:"+product.getId()+" 产品名："+product.getName()+" 价格："+product.getPrice()
                        +"\n";
            }
            new AlertDialog.Builder(this).setTitle("产品信息").setMessage(msg).setPositiveButton("关闭",null).show();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirItemClick(String path) {
        setTitle("当前目录："+path);
    }
}
