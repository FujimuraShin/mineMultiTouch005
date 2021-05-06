package com.example.minemultitouch005;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;


public class MainActivity extends AppCompatActivity {

    ImageView imageView001;
    ImageView imageView002;
    ImageView imageView003;

    ImageView imageView004;
    ImageView imageView005;
    ImageView imageView006;

    ImageView imageView007;
    ImageView imageView008;
    ImageView imageView009;

    //float[] x,y;
    ArrayList<Float> x;
    ArrayList<Float> y;

    int tap_count;
    int count=0;
    int returnData;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    private SoundPool soundPool;
    private int soundOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView001=findViewById(R.id.imageView001);
        imageView002=findViewById(R.id.imageView002);
        imageView003=findViewById(R.id.imageView003);

        imageView004=findViewById(R.id.imageView004);
        imageView005=findViewById(R.id.imageView005);
        imageView006=findViewById(R.id.imageView006);

        imageView007=findViewById(R.id.imageView007);
        imageView008=findViewById(R.id.imageView008);
        imageView009=findViewById(R.id.imageView009);

        Button button=findViewById(R.id.button);



        //スリープから立ち上がり時にPreferencesに登録したカウント数から、ImageViewを数分だけ表示する
        //Preferncesからカウント数を読み込む
        sp= PreferenceManager.getDefaultSharedPreferences(this);
        //Preferencesに書き込むための、Editorクラスを取得する
        editor=sp.edit();

        //Preferencesのデータのクリア
        //sp.edit().clear().commit();

        int returnData00 = sp.getInt("count", 0);
        //Log.d("CreateOn-ReturnData00", String.valueOf(returnData00));

        //スリープ時から戻った時に以前にタップしたスタンプを表示する
        if(returnData00>0) {
            for (int i = 1; i <= returnData00; i++) {
                int show_ImageView = getResources().getIdentifier("imageView00" + i, "id", getPackageName());
                    //Log.d("show_ImageView", String.valueOf(show_ImageView));
                ImageView show_imageView = findViewById(show_ImageView);
                    //Log.d("show_ImageView", String.valueOf(show_ImageView));
                show_imageView.setImageResource(R.drawable.img001);
            }

            //Preferencesデータが９以上の時は９に固定
            if(returnData00>9){
                returnData00=9;
            }

        }

        //クリックされたらスタンプ数を減らす
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Log.d("onCLick","クリックされました");

                int returnData02=sp.getInt("count",0);
                //Log.d("returnData02=", String.valueOf(returnData02));
                SharedPreferences.Editor editor=sp.edit();

            if(returnData02>0){

                //ボタンをクリックして、スタンプを一枚ずつ減らしていく
                int show_ImageView01=getResources().getIdentifier("imageView00"+returnData02,"id",getPackageName());
                ImageView show_imageView01=findViewById(show_ImageView01);
                //Log.d("show_image01=", String.valueOf(show_imageView01));
                show_imageView01.setImageDrawable(null);

                count=count-1;
                //ボタンがマイナスになったら、countを０にする
                if(count==0){
                    count=0;
                }

                //書き込むデータを登録する
                editor.putInt("count",count);
                //書き込みを確定させる
                editor.commit();

            }else{
                count=0;
                editor.putInt("count",count);
                editor.commit();
            }

            }
        });


    }

    public boolean onTouchEvent(MotionEvent motionEvent){

        int tap_count=motionEvent.getPointerCount();
        Log.d("TAP_COUNT=", String.valueOf(tap_count));



        for(int i=0;i<tap_count;i++){
            int pid=motionEvent.getPointerId(i);
            int masked=motionEvent.getActionMasked();
            //x=motionEvent.getX(i);
            //y=motionEvent.getY(i);


        }

        //タップ回数とタッチダウン時に限定で呼びこむ
        if(tap_count==2 && motionEvent.getActionMasked()==MotionEvent.ACTION_POINTER_DOWN){

            x=new ArrayList<Float>();
            y=new ArrayList<Float>();

            for(int i=0;i<tap_count;i++) {
                x.add(motionEvent.getX(i));
                y.add(motionEvent.getY(i));

                Log.d("X=", String.valueOf(x));
                Log.d("Y=", String.valueOf(y));
            }

            //2点間の座標の距離を求める
            double Pointe_distance=Point_Length(x.get(0),y.get(0),x.get(1),y.get(1));
            Log.d("Distance=", String.valueOf(Pointe_distance));





            //◆◇◆◇◆◇アプリ標準のPreferencesを取得する
            //sp= PreferenceManager.getDefaultSharedPreferences(this);

            //現在のcountをPreferenceから読み込む
            count=sp.getInt("count",0);
            count=count+1;


            //書き込むデータを登録する
            editor.putInt("count", count);

            //書き込みを確定する
            editor.commit();


            //Preferencesからデータを読み込む
            returnData=sp.getInt("count", 0);
            Log.d("ReturnData=", String.valueOf(returnData));

            //String TAP_NAME=returnData;
            //Log.d("TAP_NAME",TAP_NAME);

            int TAP_ImageView=getResources().getIdentifier("imageView00"+returnData,"id",getPackageName());
            Log.d("TAP_ImageViewNAMe=", String.valueOf(TAP_ImageView));
            ImageView tap_imageView=findViewById(TAP_ImageView);

            tap_imageView.setImageResource(R.drawable.img001);



            if(count>9){
                count=0;
            }

            //SNSの非同期処理
            Looper mainLooper= Looper.getMainLooper();
            Handler handler= HandlerCompat.createAsync(mainLooper);

            BackgroundTask backgroundTask=new BackgroundTask(handler);
            ExecutorService executorService= Executors.newSingleThreadExecutor();
            executorService.submit(backgroundTask);

            //タップしてサウンドをつける
            tap_sound();


        }


        return true;
    }

    private void tap_sound() {

        AudioAttributes audioAttributes=new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();

        soundPool=new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(2)
                .build();

        //soundOne=soundPool.load(this,R.raw.coin05,1);

        //音楽の再生
        soundPool.play(soundOne,1.0f,1.0f,0,0,1);

    }


    private double Point_Length(float x0, float y0, float x1, float y1) {

        double distance=Math.sqrt((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0));

        return distance;
    }


    private class BackgroundTask implements Runnable{

        private final Handler _handler;

        public BackgroundTask(Handler handler){
            _handler=handler;
        }

        @Override
        public void run() {

            //Twitterの投稿は同じ内容の投稿は弾かれるので、現在時刻を投稿に盛り込む
            final DateFormat df=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            final Date date=new Date(System.currentTimeMillis());

            String word=date+"デジタルスタンプが押されましたよ！";
            Twitter twitter= TwitterFactory.getSingleton();
            try{
                twitter4j.Status status=twitter.updateStatus(word);
                Uri uri= Uri.parse("https://twitter.com/FujimuraMf");
                Intent i=new Intent(Intent.ACTION_VIEW,uri);
                //startActivity(i);
            }catch(TwitterException e){
                e.printStackTrace();
            }
        }
    }
}