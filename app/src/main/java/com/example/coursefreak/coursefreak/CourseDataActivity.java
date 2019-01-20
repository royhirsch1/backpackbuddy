package com.example.coursefreak.coursefreak;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursefreak.coursefreak.R;
import com.example.coursefreak.coursefreak.utils.Tools;
import com.example.coursefreak.coursefreak.utils.ViewAnimation;

public class CourseDataActivity extends AppCompatActivity {

    private View parent_view;

    private NestedScrollView nested_scroll_view;
    private ImageButton bt_toggle_text, bt_toggle_input;
    private Button bt_hide_text, bt_save_input, bt_hide_input;
    private View lyt_expand_text, lyt_expand_input;

    private int mProgressStatus_avg = 0;
    private int mProgressStatus_pop = 0;
    private Handler mBarHandler_avg = new Handler();
    private Handler mBarHandler_pop = new Handler();
    private TextView textViewAverage;
    private TextView textViewPopularity;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_data);
        parent_view = findViewById(android.R.id.content);

        initToolbar();
        initComponent();
        initCourseData();
    }

    private void initCourseData(){
        course = new Course("234218","Example Course",2.8,6,8,76.8,"no","hw;pairs");

        //Update course data texts
        final TextView textViewTitle = findViewById(R.id.textView_CourseTitle);
        textViewTitle.setText(course.getName());
        final TextView textViewCredit = findViewById(R.id.textView_creditAU);
        textViewCredit.setText(course.getPoints().toString()+" AU");
        runProgressBars();
    }

    private void runProgressBars(){

        final int popularity_rate = course.getNumCompleted() > 0 ? (course.getNumLikes())*100/(course.getNumCompleted()) : 0;
        final int course_avg = course.getAverage().intValue();

        // -- Progress Bar Average -- //

        final ProgressBar progressbar_avg = (ProgressBar) findViewById(R.id.progressBar_avg);
        textViewAverage = findViewById(R.id.textView_average_number);

        //set bar style features
        int barColor_avg = getResources().getColor(R.color.colorFacebook);
        Drawable progressDrawable_avg = progressbar_avg.getProgressDrawable().mutate();
        progressDrawable_avg.setColorFilter(barColor_avg, android.graphics.PorterDuff.Mode.SRC_IN);
        progressbar_avg.setProgressDrawable(progressDrawable_avg);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mProgressStatus_avg < course_avg){
                    mProgressStatus_avg++;
                    android.os.SystemClock.sleep(25);
                    mBarHandler_avg.post(new Runnable() {
                        @Override
                        public void run() {
                            progressbar_avg.setProgress(mProgressStatus_avg);
                        }
                    });
                }
                mBarHandler_avg.post(new Runnable() {
                    @Override
                    public void run() {
                        textViewAverage.setText(Integer.toString((course_avg)));
                        textViewAverage.setVisibility(View.VISIBLE);
                    }
                });

            }
        }).start();
        // -- End Of Progress Bar Average -- //

        // -- Progress Bar Popularity -- //

        final ProgressBar progressbar_pop = (ProgressBar) findViewById(R.id.progressBar_pop);
        textViewPopularity = findViewById(R.id.textView_popularity_number);

        //set bar style features
        int barColor_pop = getResources().getColor(R.color.colorFacebook);
        Drawable progressDrawable_pop = progressbar_pop.getProgressDrawable().mutate();
        progressDrawable_pop.setColorFilter(barColor_pop, android.graphics.PorterDuff.Mode.SRC_IN);
        progressbar_pop.setProgressDrawable(progressDrawable_pop);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mProgressStatus_pop < popularity_rate){
                    mProgressStatus_pop++;
                    android.os.SystemClock.sleep(25);
                    mBarHandler_pop.post(new Runnable() {
                        @Override
                        public void run() {
                            progressbar_pop.setProgress(mProgressStatus_pop);
                        }
                    });
                }
                mBarHandler_pop.post(new Runnable() {
                    @Override
                    public void run() {
                        if(popularity_rate>0){
                            textViewPopularity.setText(Integer.toString(((int)popularity_rate))+"%");
                        }else{
                            textViewPopularity.setText("No Data");
                        }

                        textViewPopularity.setVisibility(View.VISIBLE);
                    }
                });

            }
        }).start();


        // -- End Of Progress Bar Popularity -- //
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Course Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {

        // text section
        bt_toggle_text = (ImageButton) findViewById(R.id.bt_toggle_text);
        bt_hide_text = (Button) findViewById(R.id.bt_hide_text);
        lyt_expand_text = (View) findViewById(R.id.lyt_expand_text);
        lyt_expand_text.setVisibility(View.GONE);

        bt_toggle_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText(bt_toggle_text);
            }
        });

        bt_hide_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText(bt_toggle_text);
            }
        });

        // input section
        bt_toggle_input = (ImageButton) findViewById(R.id.bt_toggle_input);
        bt_hide_input = (Button) findViewById(R.id.bt_hide_input);
        bt_save_input = (Button) findViewById(R.id.bt_save_input);
        lyt_expand_input = (View) findViewById(R.id.lyt_expand_input);
        lyt_expand_input.setVisibility(View.GONE);

        bt_toggle_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInput(bt_toggle_input);
            }
        });

        bt_hide_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInput(bt_toggle_input);
            }
        });

        bt_save_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(parent_view, "Data saved", Snackbar.LENGTH_SHORT).show();
                toggleSectionInput(bt_toggle_input);
            }
        });

        // nested scrollview
        nested_scroll_view = (NestedScrollView) findViewById(R.id.nested_scroll_view);
    }

    private void toggleSectionText(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_text, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_text);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_text);
        }
    }

    private void toggleSectionInput(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_input, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_input);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_input);
        }
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}