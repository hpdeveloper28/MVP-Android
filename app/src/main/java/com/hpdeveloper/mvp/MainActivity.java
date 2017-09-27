package com.hpdeveloper.mvp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.hpdeveloper.mvp.adapters.DataAdapter;
import com.hpdeveloper.mvp.entities.Student;
import com.hpdeveloper.mvp.interfaces.OnLoadMoreListener;
import com.hpdeveloper.mvp.mvp.StudentListContract;
import com.hpdeveloper.mvp.mvp.StudentListPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, StudentListContract.View{

    private RecyclerView mRecyclerView;
    private TextView tvEmptyView;
    private DataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<Student> studentList = new ArrayList<>();
    protected Handler handler;
    private int count = 0;
    private SwipeRefreshLayout swipeRefreshLayout;

    private StudentListContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenter = new StudentListPresenter(this);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.purple, R.color.green, R.color.orange);

        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        studentList = new ArrayList<Student>();
        handler = new Handler();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        // create an Object for Adapter
        mAdapter = new DataAdapter(mRecyclerView);
        mAdapter.setLoadMoreEnable(true);
        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);
        //  mAdapter.notifyDataSetChanged();

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                count++;

                if(count==4){
                    count = 0;
                    mAdapter.setLoadMoreEnable(false);
                }else {
                    mAdapter.setLoadMoreEnable(true);
                }

                //add null , so the adapter will check view_type and show progress bar at bottom
                studentList.add(null);
                mAdapter.notifyItemInserted(studentList.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        studentList.remove(studentList.size() - 1);
                        mAdapter.notifyItemRemoved(studentList.size());
                        //add items one by one
                        int start = studentList.size();
                        int end = start + 20;

                        for (int i = start + 1; i <= end; i++) {
                            studentList.add(new Student("Student " + i, "AndroidStudent" + i + "@gmail.com"));
                            mAdapter.notifyItemInserted(studentList.size());
                        }
                        mAdapter.setLoaded();
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 1000);

            }
        });

        presenter.getStudents();

    }


    @Override
    public void onRefresh() {

        swipeRefreshLayout.setRefreshing(true);

        new CountDownTimer(6000,6000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }.start();
    }

    @Override
    public void showStudents(List<Student> studentList) {
        this.studentList = studentList;
        mAdapter.updateStudentData(this.studentList);
        if (this.studentList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPlaceHolder() {

    }
}
