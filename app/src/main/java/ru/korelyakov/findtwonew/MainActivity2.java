package ru.korelyakov.findtwonew;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

// игра
public class MainActivity2 extends AppCompatActivity {

    private GridView mGrid;
    public Game mAdapter;
    int GRID_SIZE = 6;
    public Chronometer mTime;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        if (Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //отвечает за скрытие
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

        // Переход в приложение со сторонней ссылки, рабочий, дописать манифест.
        //  Uri uri = getIntent().getData();
        //  if(uri!=null)
        //  {
        //      String path = uri.toString();
        //       Toast.makeText(MainActivity.this, "Переведен по ссылке =" + path, Toast.LENGTH_LONG).show();
        //  }

        // Создаем таймер с условием прекращения игры
        mTime = (Chronometer) findViewById(R.id.time);
        mTime.setCountDown(true);
        mTime.setBase(SystemClock.elapsedRealtime() + 1000 * 120);
        mTime.start();
        mTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (chronometer.getText().toString().equalsIgnoreCase("00:00")) {
                    mTime.stop();
                    ShowGameOver();
                }
            }
        });


        // Создаем новую игру
        mGrid = (GridView) findViewById(R.id.field);
        mGrid.setNumColumns(GRID_SIZE);
        mGrid.setEnabled(true);
        mAdapter = new Game(this, GRID_SIZE, GRID_SIZE);
        // mAdapter.openAllCells();
        //  mAdapter.closeAllCells();
        mGrid.setAdapter(mAdapter);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mAdapter.checkOpenCells();
                mAdapter.openCell(position);
                if (mAdapter.checkGameOver()) {
                    mTime.stop();
                    ShowWin();
                }
            }
        });
    }


    // Окно победы
    private void ShowWin() {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setCancelable(false);
        alertbox.setTitle("Поздравляем!");
        String time = mTime.getText().toString();
        String TextToast = "Игра закончена, вы победили!";
        alertbox.setMessage(TextToast);
        // Кнопка закрытия окна с перезапуском активити
        alertbox.setNeutralButton("Рестарт", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                recreate();
            }
        });
        alertbox.show();
    }

    // Окно проигрыша
    private void ShowGameOver() {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setCancelable(false);
        alertbox.setTitle("Вы програли((");
        String time = mTime.getText().toString();
        String TextToast = "Игра закончена, время вышло " + time;
        alertbox.setMessage(TextToast);
        alertbox.setNeutralButton("Рестарт", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                recreate();
            }
        });
        alertbox.show();
    }
}