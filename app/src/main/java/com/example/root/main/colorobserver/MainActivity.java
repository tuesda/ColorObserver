package com.example.root.main.colorobserver;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static final int ITEMS_IN_VISIBLE = 23;

    private ListView lv;

    private EditText start_top, start_bottom, end_top, end_bottom, steps;
    private Button show_color;

    private Argb beginTop;
    private Argb beginBottom;
    private Argb finalTop;
    private Argb finalBottom;

    private TextView display;
    private ProgressBar progress;

    private View back;

    private boolean isReady = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        lv = (ListView)findViewById(R.id.list_view);
        lv.setDivider(null);



        start_top = (EditText)findViewById(R.id.start_top);
        start_bottom = (EditText)findViewById(R.id.start_bottom);

        end_top = (EditText)findViewById(R.id.end_top);
        end_bottom = (EditText)findViewById(R.id.end_bottom);

        steps = (EditText)findViewById(R.id.steps);

        show_color = (Button)findViewById(R.id.show_color);


        show_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    setColor();
                } else {
                    Toast.makeText(MainActivity.this, R.string.input_wrong, Toast.LENGTH_LONG).show();
                }
            }
        });


        display = (TextView)findViewById(R.id.display);
        back = (View)findViewById(R.id.back);


        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                display.setText(firstVisibleItem + "/" + (totalItemCount - ITEMS_IN_VISIBLE));
                if (isReady) {
                    refresh(firstVisibleItem, totalItemCount - ITEMS_IN_VISIBLE);
                }
            }
        });


    }



    private boolean checkInput() {
        boolean result = false;

        if (Argb.check(start_top.getText().toString()) && Argb.check(start_bottom.getText().toString())
                && Argb.check(end_top.getText().toString()) && Argb.check(end_bottom.getText().toString())
                && steps.getText().toString() != null && !steps.getText().toString().isEmpty()
                && Integer.parseInt(steps.getText().toString()) > 0) {
            result = true;
        }

        return result;
    }

    private void setColor() {
        beginTop = new Argb(start_top.getText().toString());
        beginBottom = new Argb(start_bottom.getText().toString());
        finalTop = new Argb(end_top.getText().toString());
        finalBottom = new Argb(end_bottom.getText().toString());
        int nums = Integer.parseInt(steps.getText().toString());
        ColorAdapter colorAdapter = new ColorAdapter(nums, MainActivity.this);
        lv.setAdapter(colorAdapter);
        isReady = true;
    }

    private void refresh(int firstItem, int total) {

        int cur_start_a = beginTop.getA() + (((finalTop.getA()-beginTop.getA()) * firstItem) / total);
        int cur_start_r = beginTop.getR() + (((finalTop.getR()-beginTop.getR()) * firstItem) / total);
        int cur_start_g = beginTop.getG() + (((finalTop.getG()-beginTop.getG()) * firstItem) / total);
        int cur_start_b = beginTop.getB() + (((finalTop.getB()-beginTop.getB()) * firstItem) / total);

        int cur_end_a = beginBottom.getA() + (((finalBottom.getA()-beginBottom.getA()) * firstItem) / total);
        int cur_end_r = beginBottom.getR() + (((finalBottom.getR()-beginBottom.getR()) * firstItem) / total);
        int cur_end_g = beginBottom.getG() + (((finalBottom.getG()-beginBottom.getG()) * firstItem) / total);
        int cur_end_b = beginBottom.getB() + (((finalBottom.getB()-beginBottom.getB()) * firstItem) / total);

        int startColor = Color.argb(cur_start_a, cur_start_r, cur_start_g, cur_start_b);
        int endColor = Color.argb(cur_end_a, cur_end_r, cur_end_g, cur_end_b);
        setBackColor(startColor, endColor);


    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setBackColor(int startColor, int endColor) {

        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {startColor, endColor});
        back.setBackground(gd);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
