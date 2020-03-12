package com.example.myapplication;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.myapplication.AppData;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PathPermission;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.PatternMatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import kotlin.Unit;

public class ApkInfo extends Activity {
    PackageManager packageManager;
    TextView appLabel, packageName, version, features,result;
    TextView permissions, andVersion, installed, path,int_spoof;
    PackageInfo packageInfo;
    Button uninstall,sp,enc,leak,intent,clipboard,scan;
    String apName, packName, andVer, ins, finalVulResult,finalDetailsResult, pth, spoofVul="No",exported_activity="No activities are exposed";
    Boolean vulnerable;


    private static Socket s;

    private static ObjectOutputStream os;
    private static String ip="192.168.0.105";

    private int spoof_count=0;
    private int progress=0;
    private static int total_act;
    String result_vul,result_details;
    int vul_count=0;
    String code;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apkinfo);

        //circularProgressBar();

        //final ProgressBar progressBar = (ProgressBar) findViewById(R.id.circle_progress_bar);
        packageInfo = AppData.getPackageInfo();

        apName = getPackageManager().getApplicationLabel(packageInfo.applicationInfo).toString();
        packName = packageInfo.packageName;

        scan =  (Button)findViewById(R.id.scan);
        uninstall =  (Button)findViewById(R.id.uninstall);
        andVer = Integer.toString(packageInfo.applicationInfo.targetSdkVersion);
        pth = packageInfo.applicationInfo.sourceDir;
        result=findViewById(R.id.result);
        int_spoof=findViewById(R.id.int_spoof);





        //ACTIVITIES INFO
        try {
            ActivityInfo[] list = getPackageManager().getPackageInfo(packName,PackageManager.GET_ACTIVITIES).activities;
            for(ActivityInfo a:list) {

                System.out.println(a.toString());
                System.out.println(a.permission);
                System.out.println(a.exported);
                Log.v("permissions",list.toString());
                Log.v("permissions_exp",a.exported+"");
                if(a.exported==true)

                {
                    exported_activity="The exposed activities are"+"\n";
                    exported_activity+=a.toString()+"\n";
                    spoofVul = "Yes";
                    spoof_count++;

                }



                total_act=list.length;
                float percent=(((float)spoof_count/(float)total_act));
                if(percent>0)
                {
                     exported_activity+="Vulnerable activities : "+spoof_count+"\n Total activities :"+total_act+"\n Percentage of vulnerable activities :"+percent*100+"%";
                }


            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        //SERVICES INFO
        try {
            ServiceInfo[] services = getPackageManager().getPackageInfo(packName, PackageManager.GET_SERVICES).services;
            if(services!=null) {
                Log.v("serv", services.toString());
                for (ServiceInfo s : services) {
                    System.out.println(s.toString());
                    System.out.println(s.permission);
                    System.out.println(s.exported);

                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }





        //CONTENT PROVIDERS
        for (PackageInfo pack : getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS)) {
            if(pack.packageName.equals(packName) )
            {ProviderInfo[] providers = pack.providers;
                if (providers != null ) {
                    for (ProviderInfo provider : providers) {
                        System.out.println("Content providers");
                        System.out.println(provider.name);
                        System.out.println(provider.processName);
                                  System.out.println(provider.describeContents());




                       // System.out.println(provider.pathPermissions.toString());

                      //  System.out.println(provider.uriPermissionPatterns.toString());
                        System.out.println("Example" + "provider: " + provider.authority);
                    }
                }   }
        }





        //BROADCAST RECEVIERS
        try {
            final ActivityInfo[] receivers = getPackageManager().getPackageInfo(packName, PackageManager.GET_RECEIVERS).receivers;
            if(receivers!=null) {
                for (ActivityInfo r : receivers) {
                    System.out.println(r.toString());
                    System.out.println(r.permission);
                    System.out.println(r.exported);

                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        ins = setDateFormat(this.packageInfo.firstInstallTime);









        sp=findViewById(R.id.sp);



        scan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {


                sendPackageNames();
                System.out.println(finalVulResult);
                System.out.println(finalDetailsResult);









            }
        });



        sp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(getApplicationContext(), MainActivity.class);


                intent.putExtra("pack",packName);
                intent.putExtra("vulresult", finalVulResult);
                intent.putExtra("detailsresult", finalDetailsResult);

                startActivity(intent);







            }

        });





        findViewsById();
        setValues();


    }




    public void sendPackageNames()
    {

        myTask mt=new myTask();
        mt.execute();
    }




    class myTask extends AsyncTask<Void,Void,Void>
    {

        protected Void doInBackground(Void... params)
        {
            try
            {


                s=new Socket(ip,5011);
                os=new ObjectOutputStream(s.getOutputStream());
                List<String> values=new ArrayList<String>();
                values.add(packName);
                values.add(code);
                Log.v("values",values+"");
                os.writeObject(values);
                os.flush();

                Thread.sleep(1000);


                ObjectInputStream is = new ObjectInputStream(s.getInputStream());
                List<String> result = new ArrayList<String>();
                result = (List<String>) is.readObject();
                result_vul = result.get(0);
                result_details= result.get(1);
                finalVulResult = result_vul+"\n"+spoofVul;
                finalDetailsResult=result_details+"\n\n"+exported_activity;

                Log.v("Server Vul Response",finalVulResult);
                Log.v("Server Details Response",finalDetailsResult);






            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            result.setText(finalVulResult);

            String scan_result="";
            String details_result="" ;
            String vul[] = finalVulResult.split("\n");
String details[] = finalDetailsResult.split("\n\n");
System.out.println("details from server"+details.toString());

            if(vul[0].equalsIgnoreCase("yes"))
                scan_result = scan_result+"Shared Preferences - Vulnerable"+"\n";
            else
                scan_result = scan_result+"Shared Preferences - Not Vulnerable"+"\n";
            if(vul[1].equalsIgnoreCase("yes"))
                scan_result = scan_result+"Local Database Storage - Vulnerable"+"\n";
            else
                scan_result = scan_result+"Local Database Storage - Not Vulnerable"+"\n";
            if(vul[2].equalsIgnoreCase("yes"))
                scan_result = scan_result+"Data leakage through log - Vulnerable"+"\n";
            else
                scan_result = scan_result+"Data leakage through log - Not Vulnerable"+"\n";
            if(vul[3].equalsIgnoreCase("yes"))
                scan_result = scan_result+"Clipboard Data - Vulnerable"+"\n";
            else
                scan_result = scan_result+"Clipboard Data - Not Vulnerable"+"\n";

            if(vul[4].equalsIgnoreCase("yes"))
                scan_result = scan_result+"Intent Spoofing - Vulnerable"+"\n";
            else
                scan_result = scan_result+"Intent Spoofing - Not Vulnerable"+"\n";



            result.setText(scan_result+"\n"+details_result);

            for(String v:vul)
            if(v.equalsIgnoreCase("yes"))
            {
                uninstall.setVisibility(View.VISIBLE);
            }

        }


    }


    private String getLocalIpAddress() throws UnknownHostException {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiManager != null;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipInt = wifiInfo.getIpAddress();
        return InetAddress.getByAddress(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()).getHostAddress();
    }


    private void findViewsById() {

        appLabel = (TextView) findViewById(R.id.applabel);
        packageName = (TextView) findViewById(R.id.package_name);
        version = (TextView) findViewById(R.id.version_name);
        features = (TextView) findViewById(R.id.req_permission);
        permissions = (TextView) findViewById(R.id.req_permission);
        andVersion = (TextView) findViewById(R.id.andversion);
        path = (TextView) findViewById(R.id.path);
        installed = (TextView) findViewById(R.id.insdate);

    }

    private void setValues() {
        packageInfo = AppData.getPackageInfo();
        // APP name
        appLabel.setText(apName);

        // package name
        packageName.setText(packName);


        // target version
        andVersion.setText(andVer);

        //path
        path.setText(pth);

        // first installation
        installed.setText(ins);



        // features
        if (packageInfo.reqFeatures != null)
            features.setText(getFeatures(packageInfo.reqFeatures));
        else
            features.setText("-");

        // uses-permission
        if (packageInfo.requestedPermissions != null)
            permissions
                    .setText(getPermissions(packageInfo.requestedPermissions));
        else
            permissions.setText("-");
    }

    @SuppressLint("SimpleDateFormat")
    private String setDateFormat(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strDate = formatter.format(date);
        return strDate;
    }

    // Convert string array to comma separated string
    private String getPermissions(String[] requestedPermissions) {
        String permission = "";
        for (int i = 0; i < requestedPermissions.length; i++) {
            permission = permission + requestedPermissions[i] + ",\n";
        }
        return permission;
    }

    // Convert string array to comma separated string
    private String getFeatures(FeatureInfo[] reqFeatures) {
        String features = "";
        for (int i = 0; i < reqFeatures.length; i++) {
            features = features + reqFeatures[i] + ",\n";
        }
        return features;
    }






}