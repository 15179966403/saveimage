package com.hrc.administrator.test.savebrowseimage.filebrowse;

/**
 * Created by Administrator on 2016/12/14.
 */

public interface OnFileBrowserListener {
    //单击文件列表时调用该方法
    public void onFileItemClick(String filename);
    //单击目录时调用该方法
    public void onDirItemClick(String path);
}
