package com.hrc.administrator.test.contacts;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hrc.administrator.test.R;

import java.io.ByteArrayInputStream;

/**
 * Created by Administrator on 2016/12/19.
 */

public class Main extends ListActivity implements MenuItem.OnMenuItemClickListener{
    public static DBService dbService;
    public static ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbService=new DBService(this);
        String sql="select id as _id,name,telephone,photo from t_contacts order by name";
        Cursor cursor=dbService.query(sql,null);
        contactAdapter=new ContactAdapter(this,cursor,true);
        setListAdapter(contactAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem miAddContact=menu.add(1,1,1,"添加联系人");
        MenuItem miEditContact=menu.add(1,2,2,"修改联系人");
        MenuItem miDeleteContact=menu.add(1,3,3,"删除联系人");
        miAddContact.setOnMenuItemClickListener(this);
        miEditContact.setOnMenuItemClickListener(this);
        miDeleteContact.setOnMenuItemClickListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    public Main(){
        dbService=new DBService(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent=null;
        switch (item.getItemId()){
            //添加联系人
            case 1:
                intent=new Intent(this,AddContact.class);
                startActivity(intent);
                break;
            //修改联系人
            case 2:
                break;
            //删除联系人
            case 3:
                break;
        }
        return false;
    }

    public class ContactAdapter extends CursorAdapter{
        private LayoutInflater layoutInflater;

        private void setChildView(View view,Cursor cursor){
            TextView tvName=(TextView)view.findViewById(R.id.tvName);
            TextView tvTelephone=(TextView)view.findViewById(R.id.tvTelephone);
            ImageView ivPhone=(ImageView)view.findViewById(R.id.ivPhoto);
            tvName.setText(cursor.getString(cursor.getColumnIndex("name")));
            tvTelephone.setText(cursor.getString(cursor.getColumnIndex("telephone")));
            byte[] photo=cursor.getBlob(cursor.getColumnIndex("photo"));
            ByteArrayInputStream bais=new ByteArrayInputStream(photo);
            ivPhone.setImageDrawable(Drawable.createFromStream(bais,"photo"));
        }

        public ContactAdapter(Context context,Cursor c,boolean autoRequery){
            super(context,c,autoRequery);
            layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view=layoutInflater.inflate(R.layout.contact_item,null);
            setChildView(view,cursor);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            setChildView(view,cursor);
        }
    }
}
