package br.com.lazarodm.ocrdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
            TessBaseAPI api = new TessBaseAPI();

            String path = Environment.getExternalStorageDirectory().getPath();

            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.recibo);

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
}
