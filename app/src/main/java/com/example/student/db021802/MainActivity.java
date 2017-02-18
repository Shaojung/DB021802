package com.example.student.db021802;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    MyTask task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        task = new MyTask(MainActivity.this);

    }
    public void click1(View v)
    {
        task.execute("https://static.pexels.com/photos/39517/rose-flower-blossom-bloom-39517.jpeg");
    }
}




class MyTask extends AsyncTask<String, Integer, Bitmap>
{
    Context context;
    private Bitmap bitmap = null;
    private InputStream inputStream = null;
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    public MyTask(Context c)
    {
        this.context = c;
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        TextView tv = (TextView) ((MainActivity)this.context).findViewById(R.id.textView2);
        Log.d("TASK", "進度更新:" + values[0]);
        tv.setText("" + values[0]);
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        URL url = null;
        try {
            url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            inputStream = conn.getInputStream();
            double fullSize = conn.getContentLength(); // 總長度
            byte[] buffer = new byte[64]; // buffer ( 每次讀取長度)
            int readSize = 0; // 當下讀取長度
            double sum = 0;
            while ((readSize = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readSize);
                sum += (readSize / fullSize) * 100; // 累計讀取進度
                publishProgress((int)sum);
            }
// 將 outputStream 轉 byte[] 再轉 Bitmap
            byte[] result = outputStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        ImageView img = (ImageView) ((MainActivity)this.context).findViewById(R.id.imageView);
        img.setImageBitmap(bitmap);
    }
}
