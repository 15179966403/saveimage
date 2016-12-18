package com.hrc.administrator.test.savebrowseimage.filebrowse;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Administrator on 2016/12/14.
 */

public class FileBrowser extends ListView implements AdapterView.OnItemClickListener{
    private final String namespace="http://com.hrc.widget";
    private String sdcardDirectory;
    private List<File> fileList=new ArrayList<File>();
    private Stack<String> dirStack=new Stack<String>();
    private FileListAdapter fileListAdapter;
    private OnFileBrowserListener onFileBrowserListener;
    private int folderImageResId;
    private int otherFileImageResId;
    private Map<String,Integer> fileImageResIdMap=new HashMap<String,Integer>();
    private boolean onlyFolder=false;


    public FileBrowser(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(isInEditMode()){
            return;
        }
        //可以顺利的读取本地的文件存储路径,路径为：/storage/sdcard
        sdcardDirectory= Environment.getExternalStorageDirectory().toString();
        setOnItemClickListener(this);
        setBackgroundColor(Color.BLACK);
        folderImageResId=attrs.getAttributeResourceValue(namespace,"folderImage",0);
        otherFileImageResId=attrs.getAttributeResourceValue(namespace,"otherFileImage",0);
        onlyFolder=attrs.getAttributeBooleanValue(namespace,"onlyFolder",false);
        int index=1;
        while(true){
            String extName=attrs.getAttributeValue(namespace,"extName"+index);
            int fileImageResId=attrs.getAttributeResourceValue(namespace,"fileImage"+index,0);
            if(extName==null||"".equals(extName)||fileImageResId==0){
                break;
            }
            fileImageResIdMap.put(extName,fileImageResId);
            index++;
        }
        dirStack.push(sdcardDirectory);
        addFiles();
        fileListAdapter=new FileListAdapter(getContext());
        setAdapter(fileListAdapter);
    }

    private void addFiles(){
        fileList.clear();
        String currentPath=getCurrentPath();
        Log.d("addFiles","currentPath字符串的值："+currentPath);
        File[] files=new File(currentPath).listFiles();
        Log.d("addFiles","files是否为空:"+(files==null));
        if(dirStack.size()>1){
            fileList.add(null);
        }
        if(files!=null){
            for(File file:files){
                if(onlyFolder){
                    if(file.isDirectory()){
                        fileList.add(file);
                    }
                }else{
                    fileList.add(file);
                }
            }
        }
    }

    private String getCurrentPath(){
        String path="";
        for(String dir:dirStack){
            path+=dir+"/";
        }
        //将path的最后一个字符/斜杠去掉
        path=path.substring(0,path.length()-1);
        Log.d("getCurrentPath","path字符串内容："+path);
        return path;
    }

    private String getExtName(String filename){
        int position=filename.lastIndexOf(".");
        if(position>=0){
            return filename.substring(position+1);
        }else{
            return "";
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(fileList.get(position)==null){
            dirStack.pop();
            addFiles();
            fileListAdapter.notifyDataSetChanged();
            if(onFileBrowserListener!=null){
                onFileBrowserListener.onDirItemClick(getCurrentPath());
            }
        }else if(fileList.get(position).isDirectory()){
            dirStack.push(fileList.get(position).getName());
            addFiles();
            fileListAdapter.notifyDataSetChanged();
            if(onFileBrowserListener!=null){
                onFileBrowserListener.onDirItemClick(getCurrentPath());
            }
        }else{
            if(onFileBrowserListener!=null){
                String filename=getCurrentPath()+"/"+fileList.get(position).getName();
                onFileBrowserListener.onFileItemClick(filename);
            }
        }
    }

    public void setOnFileBrowserListener(OnFileBrowserListener listener){
        this.onFileBrowserListener=listener;
    }

    private class FileListAdapter extends BaseAdapter{
        private Context context;

        public FileListAdapter(Context context){
            this.context=context;
        }

        @Override
        public int getCount() {
            return fileList.size();
        }

        @Override
        public Object getItem(int position) {
            return fileList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout fileLayout=new LinearLayout(context);
            fileLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            fileLayout.setOrientation(LinearLayout.HORIZONTAL);
            fileLayout.setPadding(5,10,0,10);
            ImageView ivFile=new ImageView(context);
            ivFile.setLayoutParams(new ViewGroup.LayoutParams(48,48));
            TextView tvFile=new TextView(context);
            tvFile.setTextColor(Color.WHITE);
            //该方法会使字符串无法显示出来
            //tvFile.setTextAppearance(context,android.R.style.TextAppearance_Large);
            tvFile.setPadding(5,5,0,0);
            //用fileList.get(position).getName()可以获得相应文字的字符串
            if(fileList.get(position)==null){
                if(folderImageResId>0){
                    ivFile.setImageResource(folderImageResId);
                }
                tvFile.setText(". .");
                Log.d("tvFile","第一个if设置的文字:"+tvFile.getText().toString());
            }else if(fileList.get(position).isDirectory()){
                if(folderImageResId>0){
                    ivFile.setImageResource(folderImageResId);
                }
                tvFile.setText(fileList.get(position).getName());
                Log.d("tvFile","第二个if设置的文字:"+tvFile.getText().toString());
            }else{
                tvFile.setText(fileList.get(position).getName());
                Log.d("tvFile","最后设置的文字:"+tvFile.getText().toString());
                Integer resId=fileImageResIdMap.get(getExtName(fileList.get(position).getName()));
                int fileImageResId=0;
                if(resId!=null){
                    if(resId>0){
                        fileImageResId=resId;
                    }
                }
                if(fileImageResId>0){
                    ivFile.setImageResource(fileImageResId);
                }else if(otherFileImageResId>0){
                    ivFile.setImageResource(otherFileImageResId);
                }
            }
            tvFile.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            fileLayout.addView(ivFile);
            fileLayout.addView(tvFile);
            return fileLayout;
        }
    }
}
