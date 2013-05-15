package com.cisco.imagefollow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ImageOperation {
    public static ArrayList<File> fileNameList;
    public static String path="/sdcard/photo"; 
    public  static Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int)scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }
    

  // 读取文件列表,并设置listview
  public static void getImageFiles() { 
          File[] files = new File(path).listFiles();  
          fileNameList = new ArrayList<File>();
          for (File file : files) {
                  if (isValidFileOrDir(file)) {
                          fileNameList.add(file);
                  }
          }
          Log.e("ImageOperation", "fileNameList =  " + "" +fileNameList.size());
  }
  
  /*检查是否为合法的文件名，或者是否为路径*/
  public static boolean isValidFileOrDir(File file)
  {
          if (file.isDirectory()) {
                  return true;
          }
          else {
                  String fileName = file.getName().toLowerCase();
                  if (fileName.endsWith(".png")||fileName.endsWith(".jpg")) {
                          return true;
                  }
          }
          return false;
  }
}
