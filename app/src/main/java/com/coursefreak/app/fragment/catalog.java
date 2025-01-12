package com.coursefreak.app.fragment;

import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coursefreak.app.Course;
import com.coursefreak.app.CourseLineAdapter;
import com.coursefreak.app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static android.view.View.GONE;

public class catalog extends Fragment {
    private View rootView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    final ArrayList<Course> res = new ArrayList<>();

    private recommended recommendedFragment;
    private interested  bookmarkFragment;

    private ListView courses_list;

    public catalog() {
    }

    public void setRecommendedFragment(recommended recommendedFragment) {
        this.recommendedFragment = recommendedFragment;
    }

    public void setBookmarkFragment(interested bookmarkFragment) {
        this.bookmarkFragment = bookmarkFragment;
    }

    public void updateBookmarkedCourse(String courseID) {
        for(int i = 0; i < this.courses_list.getAdapter().getCount(); i++) {
            View v = this.courses_list.getChildAt(i);
            if(v == null)
                continue;
            String text = ((TextView)v.findViewById(R.id.textViewCourseID)).getText().toString();
            if(text.contains(courseID)) {
                ImageView bookmarkView = v.findViewById(R.id.bookmarkBtn);
                bookmarkView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_bookmark));
                bookmarkView.setTag(R.drawable.bookmark_ribbon);
                break;
            }
        }
    }

    public void removeBookmarkedCourse(String courseID) {
        for(int i = 0; i < this.courses_list.getAdapter().getCount(); i++) {
            View v = this.courses_list.getChildAt(i);
            if(v == null)
                continue;
            String text = ((TextView)v.findViewById(R.id.textViewCourseID)).getText().toString();
            if(text.contains(courseID)) {
                ImageView bookmarkView = v.findViewById(R.id.bookmarkBtn);
                bookmarkView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_bookmark_border));
                bookmarkView.setTag(R.drawable.bookmark_outline);
                break;
            }
        }
    }

    public void updateLikedCourse(String courseID, int updatedIcon) {
        if(this.courses_list == null)
            return;
        for(int i = 0; i < this.courses_list.getAdapter().getCount(); i++) {
            View v = this.courses_list.getChildAt(i);
            if(v == null)
                continue;
            String text = ((TextView)v.findViewById(R.id.textViewCourseID)).getText().toString();
            if(text.contains(courseID)) {
                ImageView bookmarkView = v.findViewById(R.id.likeCourseButton);
                if(updatedIcon == R.drawable.ic_love)
                    bookmarkView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_love));
                else
                    bookmarkView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_love_empty));
                bookmarkView.setTag(updatedIcon);
                break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.catalog, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
        final ListView lv = rootView.findViewById(R.id.catalogCoursesListView);
        this.courses_list = lv;

        myRef.child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot dataSnapshot){
                res.clear();
                for (DataSnapshot courseSnap : dataSnapshot.getChildren()) {
                    Course c = courseSnap.getValue(Course.class);
                    c.parseCatsReqs();
                    res.add(c);
                }
                res.removeAll(Collections.singleton(null));
                CourseLineAdapter cla = new CourseLineAdapter(getContext(), res,
                        catalog.this,
                        catalog.this.recommendedFragment,
                        catalog.this.bookmarkFragment);
                lv.setAdapter(cla);
                ProgressBar pB = rootView.findViewById(R.id.progressBarCatalog);
                pB.setVisibility(GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                res.clear();
//                Log.d("Courses", "Database Error");
            }
        });

        this.courses_list = rootView.findViewById(R.id.catalogCoursesListView);

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
        }
    }
}

