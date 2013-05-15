
package com.cisco.imagefollow;

import com.cisco.imagefollow.AsyncBitmapLoader.ImageCallBack;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
    
public class MainActivity extends Activity implements View.OnClickListener {

    private ListView mList;
    private ListAdapter mAdapter;
    private View mView;
    private FrameLayout anim_content;
    private ImageView mapview;
    private Button mBtn_right;
    private Button mBtn_left;
    private final String LIST_ITEM_CLICK_INFO = "listitem";
    
    private int[] imageIDs;
    private String[] titles;
    private ArrayList<ImageView> images;
    private ArrayList<View> dots;
    private TextView title;
    private ViewPager viewPager;
    private MyAdapter madapter;
    private int currrentItem = 0; //记录当前显示页面的位置
    private boolean mapstatus = false;
    private ScheduledExecutorService scheduledExecutorService;
    private int  mViewPagerTotalNum = 0;
    private int mListImageWidth = 0;
    private int mListImageHeight = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAllMembers();
        initMEvents();
    }

    private void initAllMembers()
    {
        ImageOperation.getImageFiles();
        images = new ArrayList<ImageView>();
        mListImageWidth = getResources().getDimensionPixelSize(R.dimen.listview_image_width);
        mListImageHeight = getResources().getDimensionPixelSize(R.dimen.listview_image_height);
        mAdapter = new ListAdapter(this);
        anim_content = (FrameLayout)findViewById(R.id.anim_content);
        mapview = (ImageView)findViewById(R.id.mapview);
        mBtn_right = (Button)findViewById(R.id.title_right);
        mBtn_left = (Button)findViewById(R.id.title_left);

        mList = (ListView)findViewById(R.id.listview);
        LayoutInflater mInflater = LayoutInflater.from(this);
        mView = mInflater.inflate(R.layout.list_viewpage,null);
        mView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.listview_header_height)));
        titles = new String[]{
                "这是第一张",
                "这是第二张",
                "这是第三张",
                "这是第四张",
                "这是第五张",
        };
        //用来显示的图片
        for(int i = 0; i < ImageOperation.fileNameList.size(); i++){
            mViewPagerTotalNum = i;
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setBackgroundColor(Color.BLACK);
            imageView.setImageBitmap(ImageOperation.convertToBitmap(ImageOperation.fileNameList.get(i).toString(), 200, 200));
            images.add(imageView);
            if(i == 4)
                break;
        }
        
        dots = new ArrayList<View>();
        dots.add(mView.findViewById(R.id.dot_0));
        dots.add(mView.findViewById(R.id.dot_1));
        dots.add(mView.findViewById(R.id.dot_2));
        dots.add(mView.findViewById(R.id.dot_3));
        dots.add(mView.findViewById(R.id.dot_4));
        
        title = (TextView) mView.findViewById(R.id.title);
        title.setText(titles[0]);
        
        viewPager = (ViewPager) mView.findViewById(R.id.viewpaper);
        madapter = new MyAdapter();
    }
    private void initMEvents()
    {
        viewPager.setAdapter(madapter);
        mList.addHeaderView(mView);
        mList.setAdapter(mAdapter);

        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            int oldPosition = 0; 
            @Override
            public void onPageSelected(int position) {
                title.setText(titles[position]);
                dots.get(position).setBackgroundResource(R.drawable.dot_focused);
                dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
                oldPosition = position;
                currrentItem = position;
            }
            @Override
            public void onPageScrolled(int position, float arg1, int arg2) {
            }
            @Override 
            public void onPageScrollStateChanged(int position) {
            }
        });
        
        mBtn_right.setOnClickListener(onclicolisten);
        mBtn_left.setOnClickListener(onclicolisten);
        mList.setOnItemClickListener(listOnItemClick);
    }

    View.OnClickListener onclicolisten = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch(v.getId())
            {
                case R.id.title_left:
                    
                    break;
                case R.id.title_right:
                	if(!mapstatus)
                    applyRotation(1, 0, 90, mapview ,mList,anim_content);
                	else
                	applyRotation(-1, 0, -90, mList ,mapview,anim_content);
                    break;
                 default:
                        break;
            }
        }
    };
    
    AdapterView.OnItemClickListener listOnItemClick = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            // TODO Auto-generated method stub
            Intent intent = new Intent();
            intent.putExtra(LIST_ITEM_CLICK_INFO, arg2);
            intent.setClass(MainActivity.this, ImageFollowActivity.class);
            startActivity(intent);
        }
    };
    public class ListAdapter extends BaseAdapter{
        private LayoutInflater mInflater;
        private AsyncBitmapLoader asyncLoader = null;
        public ListAdapter(Context context)
        {
            this.mInflater = LayoutInflater.from(context);
            this.asyncLoader = new AsyncBitmapLoader();  
        }
        
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return ImageOperation.fileNameList.size();
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
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder)convertView.getTag();
            }
            /*
            Log.e("getView", "position = " + "" + position);
            String filename = ImageOperation.fileNameList.get(position).toString();
            Log.e("getView", "filename = " + "" + filename);
            Bitmap bm = ImageOperation.convertToBitmap(filename,55,55);
            holder.itemImageviw.setImageBitmap(bm);
            */
            String  filename = ImageOperation.fileNameList.get(position).toString();
            Bitmap bitmap = asyncLoader.loadBitmap(holder.itemImageviw,
                    filename,
                    mListImageWidth ,
                    mListImageHeight,
                    new ImageCallBack()  
                    {  
                        @Override  
                        public void imageLoad(ImageView imageView, Bitmap bitmap)  
                        {  
                            // TODO Auto-generated method stub  
                            holder.itemImageviw.setImageBitmap(bitmap);  
                        }  
                    });  
            if(bitmap == null)  
            {  
                holder.itemImageviw.setImageResource(R.drawable.list_item_image_def);  
            }  
            else  
            {  
                holder.itemImageviw.setImageBitmap(bitmap);  
            }  
            return convertView;
        }
        
        public final class ViewHolder{
            public ImageView itemImageviw;
            public TextView itemTextView1;
            public TextView itemTextView2;
        }
      }
    
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
    @Override
    protected void onStart() {
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ViewpagerTask(),10, 5, TimeUnit.SECONDS);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        scheduledExecutorService.shutdown();
    }
    class ViewpagerTask implements Runnable{

        @Override
        public void run() {
            currrentItem = (currrentItem + 1)%mViewPagerTotalNum;
            handler.sendEmptyMessage(0);
        }
    }
    private Handler handler = new Handler(){
        
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(currrentItem);
        }
        
    };
    
    private void applyRotation(int position, float start, float end, View first,View second,View content){  
        // 计算中心点  
        final float centerX = content.getWidth() / 2.0f;  
        final float centerY = content.getHeight() / 2.0f;  
    
        final TranslationAnimation rotation =  
                new TranslationAnimation(start, end, centerX, centerY, 310.0f, true);  
        rotation.setDuration(500);  
        rotation.setFillAfter(true);  
        rotation.setInterpolator(new AccelerateInterpolator());  
        //设置监听  
        rotation.setAnimationListener(new DisplayNextView(position,first,second,content));  
        content.startAnimation(rotation);  
        } 
       private final class DisplayNextView implements Animation.AnimationListener {  
           private final int mPosition;  
           private View mFirst;
           private View mSecond;
           private View mContent;
           private DisplayNextView(int position,View first,View second,View content) {  
               mPosition = position; 
               mFirst = first;
               mSecond = second;
               mContent = content;
           }  

           public void onAnimationStart(Animation animation) {  
               
           }  
           //动画结束  
           public void onAnimationEnd(Animation animation) {  
               mapview.post(new SwapViews(mPosition,mFirst,mSecond,mContent));  
           }  

           public void onAnimationRepeat(Animation animation) {  
               
           }  
        }  
       private final class SwapViews implements Runnable {  
       private final int mPosition;  
       private View mFirst;
       private View mSecond;
       private View mContent;
       public SwapViews(int position,View first,View second,View content) {  
           mPosition = position;  
           mFirst = first;
           mSecond = second;
           mContent = content;
       }  

       public void run() {  
           final float centerX = mContent.getWidth() / 2.0f;  
           final float centerY = mContent.getHeight() / 2.0f;  
           TranslationAnimation rotation;  
             
           if (mPosition > -1) {  
               //显示ImageView  
        	   mapstatus = true;
               mFirst.setVisibility(View.VISIBLE);  
               mSecond.setVisibility(View.GONE);  
               mFirst.requestFocus();  
               rotation = new TranslationAnimation(-90, 0, centerX, centerY, 310.0f, false);  
           } else {  
               //返回listview  
        	   mapstatus = false;
               mFirst.setVisibility(View.VISIBLE);  
               mSecond.setVisibility(View.GONE);  
               mSecond.requestFocus();  
       
               rotation = new TranslationAnimation(90, 0, centerX, centerY, 310.0f, false);  
           }  
       
           rotation.setDuration(500);  
           rotation.setFillAfter(true);  
           rotation.setInterpolator(new DecelerateInterpolator());  
           //开始动画  
           mContent.startAnimation(rotation);  
       } 
  }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        
    }
}
