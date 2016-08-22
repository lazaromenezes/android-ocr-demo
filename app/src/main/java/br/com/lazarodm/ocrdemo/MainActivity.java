package br.com.lazarodm.ocrdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SCAN_REQUEST_CODE = 1;
    private File temporaryImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.btStart);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btStart){
            try {
                startImageCapture();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (isScanPictureRequest(requestCode, resultCode)) {

            Bitmap image = BitmapFactory.decodeFile(temporaryImageFile.getPath());

            ImageView imageView = (ImageView) findViewById(R.id.scannedImage);
            imageView.setImageBitmap(image);

            TessBaseAPI api = new TessBaseAPI();

            String path = Environment.getExternalStorageDirectory().getPath();

            api.init(path, "por");
            api.setImage(image);

            String text = api.getUTF8Text();

            TextView textView = (TextView) findViewById(R.id.lbResult);
            textView.setText(text);

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            api.end();
        }
    }

    private void startImageCapture() throws IOException {
        temporaryImageFile = File.createTempFile("scanned_immage","jpg", getExternalCacheDir());

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(temporaryImageFile);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, SCAN_REQUEST_CODE);
        }
    }

    private boolean isScanPictureRequest(int requestCode, int resultCode) {
        return requestCode == SCAN_REQUEST_CODE && resultCode == RESULT_OK;
    }
}