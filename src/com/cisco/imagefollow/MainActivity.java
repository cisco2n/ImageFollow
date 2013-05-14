
package com.cisco.imagefollow;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private ListView mList;
    private ListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMyMembers();
        initMEvents();
    }

    private void initMyMembers()
    {
        mAdapter = new ListAdapter(this);
        mList = (ListView)findViewById(R.id.listview);
    }
    private void initMEvents()
    {
        mList.setAdapter(mAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public class ListAdapter extends BaseAdapter{
        private LayoutInflater mInflater;
        
        public ListAdapter(Context context)
        {
            this.mInflater = LayoutInflater.from(context);
        }
        
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 10;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final  ViewHolder holder;
            if(convertView == null)
            {
                convertView = mInflater.inflate(R.layout.list_item, null);
                holder = new ViewHolder();
                holder.itemImageviw = (ImageView) convertView.findViewById(R.id.item_image);
                holder.itemTextView1 = (TextView) convertView.findViewById(R.id.item_txt1);
                holder.itemTextView2 = (TextView) convertView.findViewById(R.id.item_txt2);
            }
            else{
                holder = (ViewHolder)convertView.getTag();
            }
            return convertView;
        }
        
        public final class ViewHolder{
            public ImageView itemImageviw;
            public TextView itemTextView1;
            public TextView itemTextView2;
        }
      }
}
