package com.kamaa.myplaylist;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

import java.nio.file.Files;
import java.security.Permission;
import java.util.List;

//import static android.Manifest.permission_group.STORAGE;

public class MainActivity extends AppCompatActivity {
    ListView listview1;
    String[] items;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview1= (ListView) findViewById(R.id.listview);

        runtimePermission();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    public void runtimePermission()
    {

        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
               .withListener(new MultiplePermissionsListener() {
                   @Override
                   public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                       dispsong();
                   }

                   @Override
                   public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
                   }
               }).check();


    }
    public ArrayList<File>findsong(File file){
        ArrayList<File>arrayList =new ArrayList<File>();
        File []files =file.listFiles();
        //assert files != null;
        for (File sgfiles:files){
            if (sgfiles.isDirectory()&&!sgfiles.isHidden())
            {
                arrayList.addAll(findsong(sgfiles));
            }
            else{
                if (sgfiles.getName().endsWith(".mp3")||sgfiles.getName().endsWith(".m4a")){
                    arrayList.add(sgfiles);
                }
            }
        }
        return arrayList;

    }
    void dispsong()
    {
        final ArrayList<File> audio =findsong(Environment.getExternalStorageDirectory());
        items = new String[audio.size()];
        for (int i=0;i<audio.size();i++)
        {
            items[i]= audio.get(i).getName().replace("mp3","").replace("ma4","");



        }
       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        listview1.setAdapter(adapter);*/
       CustomAdapter customadapter =new CustomAdapter() ;
       listview1.setAdapter(customadapter);
       listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String songName = (String) listview1.getItemAtPosition(position);
               startActivity( new Intent(getApplicationContext(),playlist.class)
                       .putExtra("audio",audio)
                       .putExtra("songname", songName)
                       .putExtra("pos",position));
           }
       });

    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View myview =getLayoutInflater().inflate(R.layout.items,null);
            TextView txt =myview.findViewById(R.id.txtsong);
            txt.setSelected(true);
            txt.setText(items[i]);
            return myview;
        }
    }

}