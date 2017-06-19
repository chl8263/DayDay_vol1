package com.example.yuntaesik.testactivity.intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.yuntaesik.testactivity.R;
import com.example.yuntaesik.testactivity.main.MainActivity;
import com.example.yuntaesik.testactivity.setting.LockActivity;
import com.example.yuntaesik.testactivity.util.Contact;
import com.example.yuntaesik.testactivity.util.SharedPrefsUtils;


/**
 * Created by User on 2016-04-15.
 */
public class IntroActivity extends Activity {

    private Handler h;//핸들러 선언
    private ImageView logo_image_intro;
    private Animation alphaAni;
    private String lock_flag;
    private String LOCK_SWITCH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //인트로화면이므로 타이틀바를 없앤다
        setContentView(R.layout.activity_intro);
        logo_image_intro = (ImageView) findViewById(R.id.logo_image_intro);

        alphaAni = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
        logo_image_intro.startAnimation(alphaAni);


        h = new Handler(); //딜래이를 주기 위해 핸들러 생성
        h.postDelayed(mrun, 2000); // 딜레이 ( 런어블 객체는 mrun, 시간 2초)
    }

    Runnable mrun = new Runnable() {
        @Override
        public void run() {
            LOCK_SWITCH = SharedPrefsUtils.getStringPreference(getApplicationContext(), Contact.LOCK_SWITCH);
            try {
                if (LOCK_SWITCH.equals("ON")) {
                    lock_flag = "start_lock";
                    Intent intent = new Intent(getApplicationContext(), LockActivity.class);
                    intent.putExtra("lock_flag", lock_flag);
                    startActivityForResult(intent, 1);
                } else {
                    Intent i = new Intent(IntroActivity.this, MainActivity.class); //인텐트 생성(현 액티비티, 새로 실행할 액티비티)
                    startActivity(i);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            } catch (NullPointerException e) {
                Intent i = new Intent(IntroActivity.this, MainActivity.class); //인텐트 생성(현 액티비티, 새로 실행할 액티비티)
                startActivity(i);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                Intent i = new Intent(IntroActivity.this, MainActivity.class); //인텐트 생성(현 액티비티, 새로 실행할 액티비티)
                startActivity(i);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        h.removeCallbacks(mrun);
    }

}
