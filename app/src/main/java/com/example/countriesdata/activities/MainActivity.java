package com.example.countriesdata.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.countriesdata.R;
import com.example.countriesdata.model.Country;
import com.example.countriesdata.sqlite_access.GetDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Environment.getExternalStorageDirectory;

public class MainActivity extends AppCompatActivity {

    Context context = this;

    AsyncTask<String, String, String> downloadOrGetFile;

    private boolean permissionResult;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private String fileName = "countries.json";
    private String appName = "CountriesData";
    private String path = getExternalStorageDirectory() + "/" + appName + "/" + fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean hasPermission;
        String[] PERMISSIONS = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                READ_EXTERNAL_STORAGE,
        };
        hasPermission = hasPermissions(this, PERMISSIONS);

        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
        if (hasPermission) {
            downloadOrGetFile = new DownloadOrGetFile().execute();
        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {

                boolean flag = true;
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                    downloadOrGetFile = new DownloadOrGetFile().execute();
                } else {
                    finish();
                }

            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            downloadOrGetFile = new DownloadOrGetFile().execute();
        }
    }

    public class DownloadOrGetFile extends AsyncTask<String,String,String> {

        ArrayList<Country> countriesList;

        GetDatabase getDatabase;

        @Override
        protected void onPreExecute() {
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... strings) {

            File file = new File(path);

            if (!file.exists()) {
                download();
            }
            getDatabase = new GetDatabase(context);
            countriesList = getListFromDatabase();
            toListActivity(countriesList);

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void download() {
            try {
                //connecting the web page from which data will be read
                String downloadUrl = "http://restcountries.eu/rest/v2/all";
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(downloadUrl).openConnection();
                //an object that reads from the internet
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder fullJSON = new StringBuilder(); // a string that will hold the JSON
                String line; // will hold a certain line from the JSON
                while ((line = bufferedReader.readLine()) != null) { //unless the read line is null,
                    // it's being saved in line
                    fullJSON.append(line); // adding the read line to the already saved string
                }
                //Close our InputStream and Buffered reader
                bufferedReader.close();
                String responseTxt = fullJSON.toString();
                Log.d("monitoring", "doInBackground: responseText " + responseTxt);
                // PREPARE FOR WRITING A FILE TO A DEVICE DIRECTORY
                FileOutputStream fos;
                String folder = fileFolderDirectory();
                path = folder + fileName;
                try {
                    fos = new FileOutputStream(new File(path));
                    fos.write(responseTxt.getBytes());
                    Log.d("monitoring", "downloaded " + fos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
        }

        private String fileFolderDirectory() {
            String folder = Environment.getExternalStorageDirectory() + "/" +appName +"/";
            File directory = new File(folder);
            if (!directory.exists()) {
                boolean directoryExists = directory.mkdirs();
                Log.i("fileFolderDirectory", String.valueOf(directoryExists));
            }
            return folder;
        }

        private ArrayList<Country> getListFromDatabase() {

            getDatabase.open();
            countriesList = getDatabase.getCountries();
            getDatabase.close();

            return countriesList;
        }
    }

    private void toListActivity(ArrayList<Country> countriesList){

        Intent intent = new Intent(context, ListActivity.class);
        Bundle listBundle = new Bundle();
        listBundle.putParcelableArrayList("countriesList", countriesList);
        intent.putExtras(listBundle);
        startActivity(intent);
    }
//    private void toListFragment(ArrayList<Country> countriesList) {
//
//        FragmentTransaction ft;
//
//        Bundle listBundle = new Bundle();
//        listBundle.putParcelableArrayList("countriesList", countriesList);
//
//        Fragment mlf = new ListFragment();
//        mlf.setArguments(listBundle);
//        ft = getSupportFragmentManager().beginTransaction();
//        ft.add(R.id.fragments_container, mlf);
//
//        Log.d("Monitoring", "Going to ListFragment");
//
//        ft.commit();
//    }
}