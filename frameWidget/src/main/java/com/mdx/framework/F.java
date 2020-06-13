package com.mdx.framework;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import androidx.fragment.app.FragmentActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mdx.framework.utility.BitmapRead;
import com.mdx.framework.view.CallBackOnly;
import com.mdx.framework.view.CallBackPt;
import com.mdx.framework.view.CallBackShareJieKou;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class F {
    public static String WEIXINKEY = null, APPNAME = null, WEIXINID = null,
            WEIXINSEC = null, SINAID = null, SiNASEC = null, QQID = null,
            QQSEC = null, cookie = "", COLOR = "#0277bd";
    public static int ICON_SHARE = 0;
    //    public static String ICON_SHARE_URL = "";
    public static CallBackShareJieKou mCallBackShareJieKou;
    public static CallBackPt mCallBackPt;
    public static int isShare = 0;
    public static String ShareId = "";


    /**
     * 描述：Date类型转化为String类型.
     *
     * @param date   the date
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getStringByFormat(Date date, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        String strDate = null;
        try {
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static float dp2pxF(Context context, float dpValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static boolean ExistSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    public static String toPinYin(char hanzi) {

        HanyuPinyinOutputFormat hanyuPinyin = new HanyuPinyinOutputFormat();
        hanyuPinyin.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        hanyuPinyin.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        hanyuPinyin.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        String[] pinyinArray = null;
        try {
            // 是否在汉字范围内
            if (hanzi >= 0x4e00 && hanzi <= 0x9fa5) {
                pinyinArray = PinyinHelper.toHanyuPinyinStringArray(hanzi);
                return pinyinArray[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hanzi + "";
    }

    public static boolean isPinYin(char hanzi) {
        HanyuPinyinOutputFormat hanyuPinyin = new HanyuPinyinOutputFormat();
        hanyuPinyin.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        hanyuPinyin.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        hanyuPinyin.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        try {
            // 是否在汉字范围内
            if (hanzi >= 0x4e00 && hanzi <= 0x9fa5) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static byte[] bitmap2ByteTrue(String picpathcrop) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BitmapRead.decodeSampledBitmapFromFile(
                picpathcrop, 720, 0).compress(Bitmap.CompressFormat.JPEG, 100,
                out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }


    public static byte[] File2byte(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


    public static List<String> getData() {
        List<String> datas = new ArrayList<String>();
        for (int i = 0; i < 15; i++) {
            datas.add("11111111");
        }
        return datas;
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }


    public static byte[] bitmap2Byte(String picpathcrop) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BitmapRead.decodeSampledBitmapFromFile(
                picpathcrop, 480, 0).compress(Bitmap.CompressFormat.JPEG, 80,
                out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    public static void showCenterDialog(Context context, View view,
                                        CallBackOnly mCallBackOnly) {
        Dialog mDialog = new Dialog(context, com.framework.R.style.dialog_1);
        mDialog.setContentView(view, new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        WindowManager windowManager = ((FragmentActivity) context)
                .getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        // lp.alpha = 0.7f;
        lp.width = (display.getWidth()); // 设置宽度
        // lp.height = (int) (display.getHeight() * 0.6); // 高度设置为屏幕的0.6
        lp.gravity = Gravity.CENTER;
        mDialog.getWindow().setAttributes(lp);
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(true);
        mCallBackOnly.goReturnDo(mDialog);
    }

    public static void showCenterDialogAll(Context context, View view,
                                           CallBackOnly mCallBackOnly) {
        Dialog mDialog = new Dialog(context, com.framework.R.style.dialog_1);
        mDialog.setContentView(view, new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        WindowManager windowManager = ((FragmentActivity) context)
                .getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        // lp.alpha = 0.7f;
        lp.width = (display.getWidth()); // 设置宽度
        lp.height = (int) (display.getHeight()); // 高度设置为屏幕的0.6
        lp.gravity = Gravity.CENTER;
        mDialog.getWindow().setAttributes(lp);
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(true);
        mCallBackOnly.goReturnDo(mDialog);
    }

    public static void showCenterDialogQuanJu(Context context, View view,
                                              CallBackOnly mCallBackOnly) {
        Dialog mDialog = new Dialog(context, com.framework.R.style.dialog_1);
        mDialog.setContentView(view, new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        WindowManager windowManager = ((FragmentActivity) context)
                .getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        // lp.alpha = 0.7f;
        // lp.width = (display.getWidth()); // 设置宽度
        // lp.height = (int) (display.getHeight() * 0.6); // 高度设置为屏幕的0.6
        lp.gravity = Gravity.CENTER;
        mDialog.getWindow().setAttributes(lp);
//		mDialog.getWindow().setType(WindowManager.LayoutParams.);//将弹出框设置为全局
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(true);
        mCallBackOnly.goReturnDo(mDialog);
    }


    public static void showCenterDialog(Activity context, View view,
                                        CallBackOnly mCallBackOnly) {
        Dialog mDialog = new Dialog(context, com.framework.R.style.dialog_1);
        mDialog.setContentView(view, new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        // lp.alpha = 0.7f;
        // lp.width = (display.getWidth()); // 设置宽度
        // lp.height = (int) (display.getHeight() * 0.6); // 高度设置为屏幕的0.6
        lp.gravity = Gravity.CENTER;
        mDialog.getWindow().setAttributes(lp);
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(true);
        mCallBackOnly.goReturnDo(mDialog);
    }

    public static Dialog createLoadingDialog(Context context, String msg) throws Exception {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(com.framework.R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(com.framework.R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(com.framework.R.id.img);
        TextView tipTextView = (TextView) v.findViewById(com.framework.R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, com.framework.R.anim.load_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, com.framework.R.style.loading_dialog);// 创建自定义样式dialog

//        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        return loadingDialog;


    }

    /**
     * list转数组
     */
    public static Object[] list2Array(List list) {
        return (Object[]) list.toArray(new Object[list.size()]);
    }

    /**
     * 数组转list
     */
    public static List<Object> Array2list(Object arr) {
        return Arrays.asList(arr);
    }


    public static String getVersionName(Context mContext) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = mContext.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    mContext.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getversioncode(Context mContext) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = mContext.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    mContext.getPackageName(), 0);
            int versionCode = packInfo.versionCode;
            return versionCode;
        } catch (Exception e) {

        }
        return 0;
    }


    public static String go2Wei(Double f) {
        return String.format("%.2f", f);
    }

    public static void yShoure(Context act, String title, String content,
                               DialogInterface.OnClickListener click) {
        new AlertDialog.Builder(act).setTitle(title).setMessage(content)
                .setPositiveButton("是", click).setNegativeButton("否", null)
                .show();

    }

    public static void yShoure(Context act, String title, String content,
                               DialogInterface.OnClickListener click, DialogInterface.OnClickListener click2) {
        new AlertDialog.Builder(act).setTitle(title).setMessage(content)
                .setPositiveButton("是", click).setNegativeButton("否", click2)
                .show();

    }

    public static void yShoure(Context act, String title, String content,
                               DialogInterface.OnClickListener click, DialogInterface.OnClickListener click2, DialogInterface.OnDismissListener click3) {
        new AlertDialog.Builder(act).setTitle(title).setMessage(content)
                .setPositiveButton("是", click).setNegativeButton("否", click2).setOnDismissListener(click3)
                .show();

    }

    public String uploadFile(byte[] bytes, String fileName) throws Exception {
        InputStream is = new ByteArrayInputStream(bytes);
        String fname = null;
        try {
            fname = uploadFile(is, fileName);
        } catch (Exception e) {
            try {
                if (is != null)
                    is.close();
            } catch (Exception e2) {
            }
        }
        return fname;
    }

    public String uploadFile(InputStream is, String fileName) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("/fileUpload");
        InputStreamBody isb = new InputStreamBody(is, fileName);
        MultipartEntity multipartEntity = new MultipartEntity();
        multipartEntity.addPart("MyFile", isb);
        post.setEntity(multipartEntity);
        HttpResponse response = client.execute(post);
        System.out.println(response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            is = response.getEntity().getContent();
            String result = inStream2String(is);
            // Assert.assertEquals(result, "UPLOAD_SUCCESS");
            System.out.println(result);
            result = result.replace("\"", "");
            if (!result.equals("0"))
                return result.replace("\"", "");
        }
        return null;
    }

    // 将输入流转换成字符串
    private static String inStream2String(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = is.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        return new String(baos.toByteArray());
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void setBackgroundAlpha(Activity act, float bgAlpha) {
        WindowManager.LayoutParams lp = act.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        act.getWindow().setAttributes(lp);
    }
}
