package com.cisco.imagefollow;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ImageFollowActivity extends Activity {
    private ViewPager mViewPager;
    private TextView mTitle;
    private TextView mContent;
    private ImageButton mBack;
    private Button mMessage;
    private Button mSave;
    private Button mShare;
    private Button mStore;
    private MyAdapter mViewAdapter;
    private ArrayList<ImageView>images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagefollow);
        initAllMembers();
        initMEvents();
    }
    
    private void initAllMembers()
    {
        mViewPager = (ViewPager)findViewById(R.id.viewpaper);
        mTitle = (TextView)findViewById(R.id.txt_title);
        mContent = (TextView)findViewById(R.id.txt_content);
        mBack = (ImageButton)findViewById(R.id.btn_back);
        mSave = (Button)findViewById(R.id.btn_save);
        mShare = (Button)findViewById(R.id.btn_share);
        mStore = (Button)findViewById(R.id.btn_store);
        mMessage = (Button)findViewById(R.id.btn_message);
        mViewAdapter = new MyAdapter();
        images = new ArrayList<ImageView>();
    }
    private void initMEvents()
    {
        mBack.setOnClickListener(btnClickListen);
        mMessage.setOnClickListener(btnClickListen);
        mSave.setOnClickListener(btnClickListen);
        mStore.setOnClickListener(btnClickListen);
        mShare.setOnClickListener(btnClickListen);
        mViewPager.setAdapter(mViewAdapter);
        mContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        for(int i = 0; i < ImageOperation.fileNameList.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setBackgroundColor(Color.BLACK);
            imageView.setImageBitmap(ImageOperation.convertToBitmap(ImageOperation.fileNameList.get(i).toString(), 200, 200));
            images.add(imageView);
        }
    }
    
    View.OnClickListener btnClickListen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch(v.getId())
            {
                case R.id.btn_back:
                    finish();
                    break;
                case R.id.btn_message:
                    break;
                case R.id.btn_save:
                    break;
                case R.id.btn_share:
                    break;
                case R.id.btn_store:
                    break;
                default:
                    break;
            }
        }
    };
    public  class MyAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;
        }
        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(images.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            view.addView(images.get(position));
            return images.get(position);
        }
    }
}
