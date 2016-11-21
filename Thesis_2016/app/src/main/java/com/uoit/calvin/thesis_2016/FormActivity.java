package com.uoit.calvin.thesis_2016;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

;

public class FormActivity extends AppCompatActivity implements Html.ImageGetter {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    final int RESULT_OK = 1;
    Helper helper;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.formToolbar);
        setSupportActionBar(myChildToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        helper = new Helper();
        input = (EditText) findViewById(R.id.tagInput);
    }

    public void clickCamera(View v) {
        dispatchTakePictureIntent();
    }

    public void clickInput(View v) {
        LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        if (buttonLayout != null) {
            buttonLayout.setVisibility(View.VISIBLE);
        }
    }

    public void clickSave(View v) {
        Intent returnIntent = new Intent();
        if (input != null) {
            String trans = Html.toHtml(input.getEditableText());
            trans = helper.HTMLReplaceNewLine(trans);
            trans = helper.HTMLParsing(trans);
            Log.i("MY INPUT", trans);
            //if (trans.matches("(([#@$])(.*))+")) {
            if (trans.charAt(0) == '#' || trans.charAt(0) == '@' || trans.charAt(0) == '$') {
                returnIntent.putExtra("trans", trans );
                setResult(this.RESULT_OK, returnIntent);
                finish();
            } else {
                Toast toast = Toast.makeText(this, getResources().getString(R.string.errorFormat), Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView viewer = (ImageView) findViewById(R.id.formImage);
            if (viewer != null) {
                viewer.setVisibility(View.VISIBLE);
                viewer.setImageBitmap(imageBitmap);
            }
        }

    }

    public void clickHashtag(View v) {
        input.setRawInputType(InputType.TYPE_CLASS_TEXT);
        clickSymbol("hashtag");
    }

    public void clickDollar(View v) {
        input.setRawInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        clickSymbol("dollar");
    }

    public void clickAt(View v) {
        input.setRawInputType(InputType.TYPE_CLASS_TEXT);
        clickSymbol("at");
    }

    public void clickTwitter(View v) {
        input.setRawInputType(InputType.TYPE_CLASS_TEXT);
        clickSymbol("twitter");
    }

    public void clickSymbol(String type) {
        if (input != null) {
            String code = "<img src = '" + type + ".png'>";
            String oldString = helper.HTMLReplaceNewLine(Html.toHtml(input.getEditableText()));
            String newString = oldString + code;
            Spanned spanned = Html.fromHtml(newString, this, null);
            input.setText(spanned);
            input.setSelection(spanned.length());
        }
    }

    @Override
    public Drawable getDrawable(String arg0) {
        int id = 0;

        switch (arg0) {
            case "hashtag.png":
                id = R.drawable.hashtag;
                break;
            case "at.png":
                id = R.drawable.at;
                break;
            case "dollar.png":
                id = R.drawable.dollar;
                break;
            case "twitter.png":
                id = R.drawable.twitter;
                break;
        }

        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = ContextCompat.getDrawable(getApplicationContext(),id);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        return d;
    }



}
