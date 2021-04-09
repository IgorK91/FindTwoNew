package ru.korelyakov.findtwonew;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class Game extends BaseAdapter {

    private Context mContext;
    private Integer mCols, mRows;
    private ArrayList<String> arrPict; // массив картинок
    private String PictureCollection; // Префикс набора картинок
    private Resources mRes; // Ресурсы приложения
    private static enum Status {CELL_OPEN, CELL_CLOSE, CELL_DELETE}
    private ArrayList<Status> arrStatus; // состояние ячеек

    public Game(Context context, int cols, int rows) {
        mContext = context;
        mCols = cols;
        mRows = rows;
        arrPict = new ArrayList<String>();
        arrStatus = new ArrayList<Status>();

        PictureCollection = "pict";

        // Получаем все ресурсы приложения
        mRes = mContext.getResources();

        // Метод заполняющий массив vecPict
        makePictArray();

//        openAllCells();

        // Метод устанавливающий всем ячейкам статус CELL_CLOSE
        closeAllCells();
    }

    private void makePictArray() {
        arrPict.clear();
        for (int i = 0; i < ((mCols * mRows) / 2); i++) {
            arrPict.add(PictureCollection + Integer.toString(i));
            arrPict.add(PictureCollection + Integer.toString(i));
        }
        // перемешиваем
        Collections.shuffle(arrPict);
    }

    public void closeAllCells() {
        arrStatus.clear();
        for (int i = 0; i < mCols * mRows; i++) {
            arrStatus.add(Status.CELL_CLOSE);
        }
    }

    //  public void openAllCells() {
    //     arrStatus.clear();
    //     for (int i = 0; i < mCols * mRows; i++) {
    //         arrStatus.add(Status.CELL_OPEN);
    //     }
    //  }

    @Override
    public int getCount() {
        return mCols * mRows;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view;
        if (convertView == null)
            view = new ImageView(mContext);
        else
            view = (ImageView) convertView;
        switch (arrStatus.get(position)) {
            case CELL_OPEN:
                Integer drawableId = mRes.getIdentifier(arrPict.get(position), "drawable", mContext.getPackageName());
                view.setImageResource(drawableId);
                break;
            case CELL_CLOSE:
                view.setImageResource(R.drawable.close);
                break;
            default:
                view.setImageResource(R.drawable.none);
        }
        return view;
    }

    // Проверка совпадения картинок, логика действий при совпадении\не совпадении
    public void checkOpenCells() {
        int first = arrStatus.indexOf(Status.CELL_OPEN);
        int second = arrStatus.lastIndexOf(Status.CELL_OPEN);
        if (first == second)
            return;
        if (arrPict.get(first).equals(arrPict.get(second))) {
            arrStatus.set(first, Status.CELL_DELETE);
            arrStatus.set(second, Status.CELL_DELETE);
        } else {
            arrStatus.set(first, Status.CELL_CLOSE);
            arrStatus.set(second, Status.CELL_CLOSE);
        }
        return;
    }

    public void openCell(int position) {
        if (arrStatus.get(position) != Status.CELL_DELETE)
            arrStatus.set(position, Status.CELL_OPEN);
        notifyDataSetChanged();
        return;
    }

    // Условие выигрыша
    public boolean checkGameOver() {
        if (!arrStatus.contains(Status.CELL_CLOSE))
            return true;
        return false;
    }
}
