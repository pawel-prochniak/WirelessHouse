package henny.wirelesshouse;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by Henny on 2015-05-31.
 */
public class FileHandler {
    Context context;
    public FileHandler(Context context){
        this.context = context;
    }

    public String reader(){
        try {
            FileInputStream is = context.openFileInput("json1.txt");
            byte[] buffer = new byte[is.available()];
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            is.read(buffer);
            os.write(buffer);
            os.close();
            is.close();
            return os.toString();

        }catch (IOException e) {
            return "";
        }
    }

    public void writer() {

        try {

            File file = new File(context.getFilesDir(), "json1.txt");

            OutputStream outputStream = context.openFileOutput("json1.txt", Context.MODE_PRIVATE);
            Resources resources = context.getResources();
            int jsonID = resources.getIdentifier("henny.wirelesshouse:raw/json", null, null);
            InputStream is = resources.openRawResource(jsonID);
            String ret = "";
            StringBuilder stringBuilder = new StringBuilder();

            if ( is != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString).append("\n");
                }

                is.close();
                ret = stringBuilder.toString();
            }
            outputStream.write(ret.getBytes());
            outputStream.close();

        } catch (IOException e) {


        }
    }
}
