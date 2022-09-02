package in.forpay.util;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileOperations {

    private Activity mActivity;
	public FileOperations(Activity activity) {
		 mActivity=activity;
    }
	
	public Boolean write(String fname, String fcontent){
        try {
        

            String fpath = Utility.getDeviceStorage(mActivity)+"/"+fname+".txt";
            //Log.d("path", fpath);
            File file = new File(fpath);

            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();

                //Log.d("file created","file created");
            }

            
			/*
            FileWriter fw = new FileWriter(file.getAbsoluteFile() );
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fcontent);
            bw.close();
            
            */
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());
            try {
               
                FileWriter fw = new FileWriter(fpath, true);
                fw.write(fcontent + "-----"+currentDateandTime+"\n\n");
                fw.close();
            } catch (Exception ioe) {
            	Log.d("Exception","error "+ioe);
            }
         

            //Log.d("Suceess","Sucess");


            if(file.length()>5024){
            file.delete();
            }
            
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

 }

 public String read(String fname){

     BufferedReader br = null;
     String response = null;

        try {

            StringBuffer output = new StringBuffer();
            String fpath = Utility.getDeviceStorage(mActivity)+"/"+fname+".txt";
            //String fpath = Environment.getExternalStorageDirectory().getPath()+"/"+fname+".txt";

            br = new BufferedReader(new FileReader(fpath));
            String line = "";
            while ((line = br.readLine()) != null) {
                output.append(line +"n");
            }
            response = output.toString();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
        return response;

 }
}
