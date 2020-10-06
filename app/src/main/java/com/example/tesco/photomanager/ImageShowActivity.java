package com.example.tesco.photomanager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tecso.photomanager.R;

public class ImageShowActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView deleteBtn, selectedImage;
    LinearLayout slideShowBtn, personTAG, placeTAG, placeTagLinear, personTagLinear;
    TextView placeTag, personTag, caption, captureTime, placeTagLabel, personTagLabel;
    ImageInfo imageInfo;
    DBClass dbClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_show_activity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        dbClass = new DBClass(getApplicationContext());
        selectedImage = findViewById(R.id.selected_image);
        placeTag = findViewById(R.id.placeTag);
        personTag = findViewById(R.id.personTag);
        caption = findViewById(R.id.caption);
        deleteBtn = findViewById(R.id.delete_btn);
        slideShowBtn = findViewById(R.id.slideshow_btn);
        personTAG = findViewById(R.id.person_tag);
        placeTAG = findViewById(R.id.place_tag);
        caption = findViewById(R.id.caption);
        captureTime = findViewById(R.id.captureTime);
        personTagLabel = findViewById(R.id.personTagLabel);
        placeTagLabel = findViewById(R.id.placeTagLabel);
        personTagLinear = findViewById(R.id.personTagLinear);
        placeTagLinear = findViewById(R.id.placeTagLinear);
        deleteBtn.setOnClickListener(this);
        slideShowBtn.setOnClickListener(this);
        personTAG.setOnClickListener(this);
        placeTAG.setOnClickListener(this);
        placeTag.setOnClickListener(this);
        personTag.setOnClickListener(this);

        imageInfo = (ImageInfo) getIntent().getSerializableExtra("image");
        if (imageInfo == null) {
            return;
        }
        Uri imageUri = Uri.parse(imageInfo.path);
        selectedImage.setImageURI(imageUri);
        if (!imageInfo.caption.isEmpty()) {
            caption.setVisibility(View.VISIBLE);
            caption.setText(imageInfo.caption);
        }
        captureTime.setText(imageInfo.captureTime);
        if (imageInfo.tagPerson == null) {
            return;
        }
        if (!imageInfo.tagPerson.isEmpty()) {
            personTag.setText(imageInfo.tagPerson);
            personTagLinear.setVisibility(View.VISIBLE);
            personTag.setVisibility(View.VISIBLE);
            personTagLabel.setVisibility(View.VISIBLE);
        }
        if (imageInfo.tagPlace == null) {
            return;
        }
        if (!imageInfo.tagPlace.isEmpty()) {
            placeTag.setText(imageInfo.tagPlace);
            placeTagLinear.setVisibility(View.VISIBLE);
            placeTag.setVisibility(View.VISIBLE);
            placeTagLabel.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onClick(View v) {
        if (v == deleteBtn) {
            int a = dbClass.DeleteImage(imageInfo.id);
            if (a > 0) {
                Toast.makeText(this, "Image deleted", Toast.LENGTH_SHORT).show();
            }
            finish();

        } else if (v == slideShowBtn) {
            Intent intent = new Intent(this, ImageSliderActivity.class);
            intent.putExtra("folderID", imageInfo.folderId);
            startActivity(intent);

        } else if (v == personTAG) {
            PersonTagDialog personTagDialog = new PersonTagDialog(this);
            personTagDialog.show();

        } else if (v == placeTAG) {
            PlaceTagDialog placeTagDialog = new PlaceTagDialog(this);
            placeTagDialog.show();
        } else if (v == placeTag) {
            DeletePlaceTagDialog customizeDialog = new DeletePlaceTagDialog(this);
            customizeDialog.show();
        } else if (v == personTag) {
            DeletePersonTagDialog customizeDialog = new DeletePersonTagDialog(this);
            customizeDialog.show();
        }

    }

    public class PersonTagDialog extends Dialog implements View.OnClickListener {
        TextView okBtn;
        EditText personTagEdit;

        public PersonTagDialog(Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.person_tag_dialog);
            personTagEdit = findViewById(R.id.tag_text);
            okBtn = findViewById(R.id.ok_btn);
            okBtn.setOnClickListener(this);
            setCancelable(true);

        }

        @Override
        public void onClick(View v) {
            if (v == okBtn) {
                if (personTagEdit.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Add person TAG", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "TAG Added", Toast.LENGTH_SHORT).show();
                    dbClass.AddPersonTag(Integer.parseInt(imageInfo.id), personTagEdit.getText().toString());
                    imageInfo.tagPerson = personTagEdit.getText().toString();
                    personTag.setText(personTagEdit.getText().toString());
                    personTagLinear.setVisibility(View.VISIBLE);
                    personTag.setVisibility(View.VISIBLE);
                    personTagLabel.setVisibility(View.VISIBLE);
                    dismiss();
                }

            }
        }
    }

    public class PlaceTagDialog extends Dialog implements View.OnClickListener {
        TextView okBtn;
        EditText editText;

        public PlaceTagDialog(Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.place_tag_dialog);
            editText = findViewById(R.id.tag_text);
            okBtn = findViewById(R.id.ok_btn);
            okBtn.setOnClickListener(this);
            setCancelable(true);

        }

        @Override
        public void onClick(View v) {
            if (v == okBtn) {
                if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Add person TAG", Toast.LENGTH_SHORT).show();
                } else {
                    String s = editText.getText().toString();
                    dbClass.AddLocationTag(imageInfo.id, s);
                    imageInfo.tagPlace = editText.getText().toString();
                    placeTag.setText(s);
                    placeTag.setVisibility(View.VISIBLE);
                    placeTagLabel.setVisibility(View.VISIBLE);
                    placeTagLinear.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Place TAG Added", Toast.LENGTH_SHORT).show();
                    dismiss();
                }

            }
        }
    }

    public class DeletePersonTagDialog extends Dialog implements View.OnClickListener {
        LinearLayout deleteBtn;

        public DeletePersonTagDialog(Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.delete_tag_dialog);
            deleteBtn = findViewById(R.id.delete_btn);
            deleteBtn.setOnClickListener(this);
            setCancelable(true);

        }

        @Override
        public void onClick(View v) {
            if (v == deleteBtn) {

                int a = dbClass.DeletePersonTag(Integer.parseInt(imageInfo.id));
                if (a > 0) {
                    Toast.makeText(getBaseContext(), "Person TAG deleted", Toast.LENGTH_SHORT).show();
                    personTag.setText("");
                    personTag.setVisibility(View.GONE);
                    personTagLabel.setVisibility(View.GONE);
                    personTagLinear.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getBaseContext(), " Try Again Later", Toast.LENGTH_SHORT).show();
                }
                dismiss();

            }
        }
    }

    public class DeletePlaceTagDialog extends Dialog implements View.OnClickListener {
        LinearLayout deleteBtn;

        public DeletePlaceTagDialog(Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.delete_tag_dialog);
            deleteBtn = findViewById(R.id.delete_btn);
            deleteBtn.setOnClickListener(this);
            setCancelable(true);

        }

        @Override
        public void onClick(View v) {
            if (v == deleteBtn) {

                int a = dbClass.DeletePlaceTag(Integer.parseInt(imageInfo.id));
                if (a > 0) {
                    Toast.makeText(getBaseContext(), "Place TAG deleted", Toast.LENGTH_SHORT).show();
                    placeTag.setText("");
                    placeTag.setVisibility(View.GONE);
                    placeTagLabel.setVisibility(View.GONE);
                    placeTagLinear.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getBaseContext(), "Try again Later", Toast.LENGTH_SHORT).show();
                }
                dismiss();

            }
        }
    }
}
