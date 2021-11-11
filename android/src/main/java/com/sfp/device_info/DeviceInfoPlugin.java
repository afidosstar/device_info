package com.sfp.device_info;
import androidx.annotation.NonNull;

import android.os.Build;
import java.lang.reflect.Method;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import android.provider.Settings;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


/** DeviceInfoPlugin */
@SuppressWarnings("deprecation")
public class DeviceInfoPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  private Context context;

  private PackageInfo info;





  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "device_info");
    context = flutterPluginBinding.getApplicationContext();
    getPackageInfo();
    channel.setMethodCallHandler(this);
  }

  private void getPackageInfo(){
      try {
            PackageManager pm = context.getPackageManager();
            info = pm.getPackageInfo(context.getPackageName(), 0);
      } catch (PackageManager.NameNotFoundException ex) {
      }
   }




  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getSerialNumber")) {
      result.success(getSerialNumber());
    } else if(call.method.equals("getVersion"))  {
      result.success(getVersion());
    } else if(call.method.equals("getBuildNumber")) {
      result.success(getBuildNumber());
    } else if(call.method.equals("getAndroidID")) {
      result.success(getAndroidId());
    } else  {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }


  private String getSerialNumber() {
     String serialNumber;
       try {
           Class<?> c = Class.forName("android.os.SystemProperties");
           Method get = c.getMethod("get", String.class);


           // (?) Lenovo Tab (https://stackoverflow.com/a/34819027/1276306)
           serialNumber = (String) get.invoke(c, "gsm.sn1");

           if (serialNumber.equals(""))
               // Samsung Galaxy S5 (SM-G900F) : 6.0.1
               // Samsung Galaxy S6 (SM-G920F) : 7.0
               // Samsung Galaxy Tab 4 (SM-T530) : 5.0.2
               // (?) Samsung Galaxy Tab 2 (https://gist.github.com/jgold6/f46b1c049a1ee94fdb52)
               serialNumber = (String) get.invoke(c, "ril.serialnumber");

           if (serialNumber.equals(""))
               // Archos 133 Oxygen : 6.0.1
               // Google Nexus 5 : 6.0.1
               // Hannspree HANNSPAD 13.3" TITAN 2 (HSG1351) : 5.1.1
               // Honor 5C (NEM-L51) : 7.0
               // Honor 5X (KIW-L21) : 6.0.1
               // Huawei M2 (M2-801w) : 5.1.1
               // (?) HTC Nexus One : 2.3.4 (https://gist.github.com/tetsu-koba/992373)
               serialNumber = (String) get.invoke(c, "ro.serialno");

           if (serialNumber.equals(""))
               // (?) Samsung Galaxy Tab 3 (https://stackoverflow.com/a/27274950/1276306)
               serialNumber = (String) get.invoke(c, "sys.serialnumber");




           if (serialNumber.equals(""))
               // Archos 133 Oxygen : 6.0.1
               // Hannspree HANNSPAD 13.3" TITAN 2 (HSG1351) : 5.1.1
               // Honor 9 Lite (LLD-L31) : 8.0
               // Xiaomi Mi 8 (M1803E1A) : 8.1.0
               serialNumber = Build.SERIAL;

           if(serialNumber.equals(Build.UNKNOWN))
               //This field was deprecated in API level 26.
               serialNumber = Build.getSerial();



           // If none of the methods above worked
           if (serialNumber.equals(Build.UNKNOWN))
               serialNumber = null;
       } catch (Exception e) {
           e.printStackTrace();
           serialNumber = null;
       }

       return serialNumber;
  }

  private String getAndroidId() {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.CUPCAKE) {
       return Settings.Secure.getString(context.getContentResolver(),
                       Settings.Secure.ANDROID_ID);
    }
    return null;
  }

  String getVersion(){
    if(info == null ) return null;
    return info.versionName;
  }

  String getBuildNumber(){
       if(info == null ) return null;
      return String.valueOf(getLongVersionCode(info));
  }

  @SuppressWarnings("deprecation")
    private static long getLongVersionCode(PackageInfo info) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        return info.getLongVersionCode();
      }
      return info.versionCode;
    }
}
