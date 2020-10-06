package com.example.tesco.photomanager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tecso.photomanager.R;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    LinearLayout createAlbumLayout, selectedAlbumLayout;
    TextView openBtn, renameBtn;
    ImageView deleteBtn;
    DBClass dbClass;
    List<FolderInfo> albumNameList;
    AlbumAdapter albumAdapter;
    TextView noFolder;

    int index = -1;

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noFolder = findViewById(R.id.no_folder);
        //=============db work==========
        dbClass = new DBClass(getApplicationContext());
        checkpermission();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        albumNameList = dbClass.getAllFolders();
        if (albumNameList.size() > 0) {
            noFolder.setVisibility(View.GONE);

        } else {
            noFolder.setVisibility(View.VISIBLE);
        }

        createAlbumLayout = findViewById(R.id.create_album);
        selectedAlbumLayout = findViewById(R.id.layout_for_selected_album);
        openBtn = findViewById(R.id.open_btn);
        renameBtn = findViewById(R.id.rename_btn);
        deleteBtn = findViewById(R.id.delete_btn);
        createAlbumLayout.setOnClickListener(this);
        openBtn.setOnClickListener(this);
        renameBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        albumAdapter = new AlbumAdapter(this, albumNameList);
        gridView = findViewById(R.id.gridview);
        gridView.setAdapter(albumAdapter);
        albumAdapter.notifyDataSetChanged();
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == createAlbumLayout) {
            CustomizeDialog customizeDialog = new CustomizeDialog(this);
            customizeDialog.show();

        } else if (v == openBtn) {
            Intent intent = new Intent(getBaseContext(), ImageGridLayout.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("key", albumNameList.get(index));
            startActivity(intent);

        } else if (v == renameBtn) {
            RenameDialog renameDialog = new RenameDialog(this);
            renameDialog.show();


        } else if (v == deleteBtn) {
            int a = dbClass.DeleteFolder(albumNameList.get(index).id);
            if (a > 0) {
                albumNameList.remove(index);
                albumAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Folder Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Can't be done at the moment", Toast.LENGTH_SHORT).show();

            }
            createAlbumLayout.setVisibility(View.VISIBLE);
            selectedAlbumLayout.setVisibility(View.GONE);

        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        createAlbumLayout.setVisibility(View.VISIBLE);
        selectedAlbumLayout.setVisibility(View.GONE);

    }

    private void checkpermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    101);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        albumNameList = dbClass.getAllFolders();
        albumAdapter = new AlbumAdapter(this, albumNameList);
        gridView.setAdapter(albumAdapter);
        albumAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(this, ImageGridLayout.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("key", albumNameList.get(position));
        startActivity(intent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                ShowDialogue();

            }
        }

    }

    private void ShowDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Important");
        builder.setMessage("If you do not grant the permission then you will be not able to proceed.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkpermission();
            }
        });
        builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        createAlbumLayout.setVisibility(View.GONE);
        selectedAlbumLayout.setVisibility(View.VISIBLE);
        index = position;

        return true;
    }

    //create folder function
    public void createFolder(String fname) {
        FolderInfo folderInfo = new FolderInfo();
        folderInfo.name = fname;
        long a = dbClass.AddFolder(fname);
        folderInfo.id = String.valueOf(a);
        albumNameList.add(folderInfo);
        albumAdapter.notifyDataSetChanged();
        if (albumNameList.size() == 1) {
            noFolder.setVisibility(View.GONE);
        }

    }

    public void renameFolder(String fname) {

        int a = dbClass.RenameFolder(albumNameList.get(index).id, fname);
        if (a > 0) {
            Toast.makeText(this, "Folder name changed", Toast.LENGTH_SHORT).show();
            albumNameList.get(index).name = fname;
            albumAdapter.notifyDataSetChanged();
            createAlbumLayout.setVisibility(View.VISIBLE);
            selectedAlbumLayout.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "Can't be done at the moment", Toast.LENGTH_SHORT).show();
            createAlbumLayout.setVisibility(View.VISIBLE);
            selectedAlbumLayout.setVisibility(View.GONE);
        }

    }
    //dialog for creating folder

    public class CustomizeDialog extends Dialog implements View.OnClickListener {
        TextView tv;
        EditText folder_name;

        public CustomizeDialog(Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.create_folder_dialog);
            folder_name = findViewById(R.id.folder_name);
            tv = findViewById(R.id.ok_btn);
            tv.setOnClickListener(this);
            setCancelable(true);

        }

        @Override
        public void onClick(View v) {
            if (v == tv) {

                if (folder_name.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Enter Folder Name", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("TAG", folder_name.getText().toString());
                    createFolder(folder_name.getText().toString().trim());
                    dismiss();
                }


            }
        }
    }

    public class RenameDialog extends Dialog implements View.OnClickListener {
        TextView tv;
        EditText newFolder;

        public RenameDialog(Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.rename_folder_dialog);
            newFolder = findViewById(R.id.rf_name);
            tv = findViewById(R.id.ok_btn);
            tv.setOnClickListener(this);
            setCancelable(true);

        }

        @Override
        public void onClick(View v) {
            if (v == tv) {

                if (newFolder.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Enter the name", Toast.LENGTH_SHORT).show();

                } else {
                    renameFolder(newFolder.getText().toString());
                    dismiss();
                }


            }
        }
    }
}

