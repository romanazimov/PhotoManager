package com.example.tesco.photomanager;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tecso.photomanager.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ImageGridLayout extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    ImageAdapter imageAdapter;
    DBClass dbClass;
    LinearLayout addPhotoLayout, selectedPhotoLayout, searchByPersonBtn, searchByPlaceBtn;
    TextView move, copy, personSearchText, placeSearchText, caption_btn;
    EditText personSearchBar, placeSearchBar;
    ImageView deleteBtn;
    private static final int SELECT_PICTURE = 1;
    GridView gridView;
    FolderInfo info;
    ImageInfo imageInfo;
    List<ImageInfo> imageNameList, tagSearch;
    int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_grid_layout);
        dbClass = new DBClass(getApplicationContext());
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        imageInfo = new ImageInfo();

        info = (FolderInfo) getIntent().getSerializableExtra("key");
        gridView = findViewById(R.id.image_grid_view);
        imageNameList = new ArrayList<>();
        tagSearch = new ArrayList<>();
        imageNameList = dbClass.getAllImages(info.id);
        searchByPersonBtn = findViewById(R.id.search_by_person);
        searchByPlaceBtn = findViewById(R.id.search_by_place);
        searchByPlaceBtn.setOnClickListener(this);
        searchByPersonBtn.setOnClickListener(this);

        personSearchText = findViewById(R.id.search_bar_person_text);
        placeSearchText = findViewById(R.id.search_bar_place_text);
        personSearchText.setOnClickListener(this);
        placeSearchText.setOnClickListener(this);
        personSearchBar = findViewById(R.id.person_search_bar);
        placeSearchBar = findViewById(R.id.place_search_bar);

        imageAdapter = new ImageAdapter(this, imageNameList);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemLongClickListener(this);
        gridView.setOnItemClickListener(this);


        addPhotoLayout = findViewById(R.id.add_image);
        selectedPhotoLayout = findViewById(R.id.layout_for_selected_pic);
        move = findViewById(R.id.move_btn);
        copy = findViewById(R.id.copy_btn);

        deleteBtn = findViewById(R.id.delete_btn);
        caption_btn = findViewById(R.id.caption_btn);
        addPhotoLayout.setOnClickListener(this);
        move.setOnClickListener(this);
        copy.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        caption_btn.setOnClickListener(this);

        personSearchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (personSearchBar.getText().toString().trim().length() == 0) {
                    imageNameList.clear();
                    tagSearch = dbClass.getAllImages(info.id);
                    imageNameList.addAll(tagSearch);
                    imageAdapter.notifyDataSetChanged();
                }
            }
        });
        placeSearchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (personSearchBar.getText().toString().trim().length() == 0) {
                    imageNameList.clear();
                    tagSearch = dbClass.getAllImages(info.id);
                    imageNameList.addAll(tagSearch);
                    imageAdapter.notifyDataSetChanged();
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v == addPhotoLayout) {
            openGallery();

        } else if (v == caption_btn) {
            Caption caption = new Caption(this);
            caption.show();
        } else if (v == move) {
            MoveDialog moveDialog = new MoveDialog(this);
            moveDialog.show();


        } else if (v == deleteBtn) {
            int a = dbClass.DeleteImage(imageNameList.get(index).id);
            if (a > 0) {
                Toast.makeText(this, "Image deleted", Toast.LENGTH_SHORT).show();
                addPhotoLayout.setVisibility(View.VISIBLE);
                selectedPhotoLayout.setVisibility(View.GONE);
                imageNameList.remove(index);
                imageAdapter.notifyDataSetChanged();

            } else {
                addPhotoLayout.setVisibility(View.VISIBLE);
                selectedPhotoLayout.setVisibility(View.GONE);
                Toast.makeText(this, "Try again later", Toast.LENGTH_SHORT).show();
            }

        } else if (v == copy) {
            CopyDialog copyDialog = new CopyDialog(this);
            copyDialog.show();


        } else if (v == personSearchText) {
            personSearchText.setVisibility(View.GONE);
            personSearchBar.setVisibility(View.VISIBLE);
            personSearchBar.requestFocus();
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } catch (Exception e) {
                Log.i("TAG", e.toString());
            }

        } else if (v == placeSearchText) {
            placeSearchText.setVisibility(View.GONE);
            placeSearchBar.setVisibility(View.VISIBLE);
            placeSearchBar.requestFocus();
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } catch (Exception e) {
                Log.i("TAG", e.toString());
            }

        } else if (v == searchByPersonBtn) {
            tagSearch.clear();
            tagSearch = new ArrayList<>();
            for (int i = 0; i < imageNameList.size(); i++) {
                if (imageNameList.get(i).tagPerson == null) {

                } else if (imageNameList.get(i).tagPerson.toLowerCase().contains(personSearchBar.getText().toString().toLowerCase())) {

                    tagSearch.add(imageNameList.get(i));
                }
            }
            imageNameList.clear();
            imageNameList.addAll(tagSearch);
            imageAdapter.notifyDataSetChanged();

        } else if (v == searchByPlaceBtn) {
            tagSearch.clear();
            tagSearch = new ArrayList<>();
            for (int i = 0; i < imageNameList.size(); i++) {
                if (imageNameList.get(i).tagPlace == null) {

                } else if (imageNameList.get(i).tagPlace.toLowerCase().contains(placeSearchBar.getText().toString().toLowerCase())) {

                    tagSearch.add(imageNameList.get(i));
                }
            }
            imageNameList.clear();
            imageNameList.addAll(tagSearch);
            imageAdapter.notifyDataSetChanged();
        }

    }

    public class Caption extends Dialog implements View.OnClickListener {
        TextView captionbtn;
        EditText captionText;

        public Caption(Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.caption_image_dialog);
            captionbtn = findViewById(R.id.move_btn);
            captionbtn.setOnClickListener(this);
            captionText = findViewById(R.id.f_name);
            setCancelable(true);

        }

        @Override
        public void onClick(View v) {
            if (v == captionbtn) {
                if (captionText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Add image caption", Toast.LENGTH_SHORT).show();
                } else {
                    int a = dbClass.AddCaption(imageNameList.get(index).id, captionText.getText().toString());
                    if (a > 0) {
                        Toast.makeText(getApplicationContext(), "Caption added", Toast.LENGTH_SHORT).show();
                        imageNameList.get(index).caption = captionText.getText().toString().trim();
                        imageAdapter.notifyDataSetChanged();
                    }
                    ;
                    dismiss();
                }


            }

        }
    }


    public class MoveDialog extends Dialog implements View.OnClickListener {
        TextView moveBtn;
        Spinner all_folder_name;
        List<String> foldersname;

        public MoveDialog(Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.move_image_dialog);
            moveBtn = findViewById(R.id.move_btn);
            moveBtn.setOnClickListener(this);
            all_folder_name = findViewById(R.id.all_folder_name);
            foldersname = dbClass.getAllFoldersNames();
            int c = 0;
            for (int i = 0; i < foldersname.size(); i++) {
                if (foldersname.get(i).equals(info.name)) {
                    c = i;
                    break;
                }
            }
            foldersname.remove(c);
            foldersname.add(0, "Select Folder");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getBaseContext(), R.layout.spinner_item, foldersname);
            all_folder_name.setAdapter(adapter);
            setCancelable(true);

        }

        @Override
        public void onClick(View v) {
            if (v == moveBtn) {
                Log.i("TAG", all_folder_name.getSelectedItemPosition() + "");
                if (all_folder_name.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "Select Folder", Toast.LENGTH_SHORT).show();
                } else {
                    //dbClass.deleteImage(moveImageInfo);
                    Log.i("TAG", foldersname.get(all_folder_name.getSelectedItemPosition()));

                    int a = dbClass.MoveImage(imageNameList.get(index).id, foldersname.get(all_folder_name.getSelectedItemPosition()));
                    Log.i("TAG", a + "");
                    if (a > 0) {
                        Toast.makeText(getApplicationContext(), "Image Moved", Toast.LENGTH_SHORT).show();
                        addPhotoLayout.setVisibility(View.VISIBLE);
                        selectedPhotoLayout.setVisibility(View.GONE);
                        imageNameList.remove(index);
                        imageAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), "Try again later", Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                }


            }

        }
    }

    public class CopyDialog extends Dialog implements View.OnClickListener {
        TextView copyBtn;
        Spinner all_folder_name;
        List<String> foldersname;

        public CopyDialog(Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.copy_image_dialog);
            copyBtn = findViewById(R.id.copy_btn);
            copyBtn.setOnClickListener(this);
            all_folder_name = findViewById(R.id.all_folder_name);
            foldersname = dbClass.getAllFoldersNames();
            int c = 0;
            for (int i = 0; i < foldersname.size(); i++) {
                if (foldersname.get(i).equals(info.name)) {
                    c = i;
                    break;
                }
            }
            foldersname.remove(c);
            foldersname.add(0, "Select Folder");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getBaseContext(), R.layout.spinner_item, foldersname);
            all_folder_name.setAdapter(adapter);
            setCancelable(true);

        }

        @Override
        public void onClick(View v) {
            if (v == copyBtn) {
                Log.i("TAG", all_folder_name.getSelectedItemPosition() + "");
                if (all_folder_name.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "Select Folder", Toast.LENGTH_SHORT).show();
                } else {
                    //dbClass.deleteImage(moveImageInfo);
                    Log.i("TAG", foldersname.get(all_folder_name.getSelectedItemPosition()));

                    long a = dbClass.CopyImage(imageNameList.get(index), foldersname.get(all_folder_name.getSelectedItemPosition()));
                    Log.i("TAG", a + "");
                    if (a > 0) {
                        Toast.makeText(getApplicationContext(), "Image Copied", Toast.LENGTH_SHORT).show();
                        addPhotoLayout.setVisibility(View.VISIBLE);
                        selectedPhotoLayout.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Try again later", Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                }


            }
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK
                && null != data) {
            if (data.getData() != null) {
                Uri mImageUri = data.getData();
                imageInfo.path = mImageUri.toString();
                imageInfo.folderId = info.id;
                imageInfo.captureTime = DateFormat.getDateTimeInstance().format(new Date());
                imageInfo.caption = "No caption";
                long id = dbClass.AddImage(imageInfo);
                imageInfo.id = String.valueOf(id);
                imageNameList.add(imageInfo);

                imageAdapter.notifyDataSetChanged();


            } else {
                if (data.getClipData() != null) {

                    ClipData mClipData = data.getClipData();

                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri mImageUri = item.getUri();
                        imageInfo.path = mImageUri.toString();
                        imageInfo.folderId = info.id;
                        //imageInfo.captureTime= new Date(file.lastModified()).toString();
                        imageInfo.captureTime = DateFormat.getDateTimeInstance().format(new Date());
                        imageInfo.caption = "No caption";
                        long id = dbClass.AddImage(imageInfo);
                        getContentResolver().takePersistableUriPermission(mImageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        imageInfo.id = String.valueOf(id);
                        imageNameList.add(imageInfo);
                        imageAdapter.notifyDataSetChanged();
                    }
                }
            }
        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        imageNameList = dbClass.getAllImages(info.id);
        imageAdapter = new ImageAdapter(this, imageNameList);
        gridView.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), ImageShowActivity.class);
        intent.putExtra("image", imageNameList.get(position));
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        addPhotoLayout.setVisibility(View.GONE);
        selectedPhotoLayout.setVisibility(View.VISIBLE);
        index = position;
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        addPhotoLayout.setVisibility(View.VISIBLE);
        selectedPhotoLayout.setVisibility(View.GONE);

    }


}
