package com.lzw.bottomsheetdialogdemo;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lzw.bottomsheetdialogdemo.share.ShareRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    BottomSheetBehavior mBottomSheetBehavior;
    GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View bottomSheet = findViewById(R.id.bottom_sheet);
        Button showBottomSheetBtn = (Button)findViewById(R.id.btn_show_BottomSheet);
        Button showBottomSheetDialogBtn1 = (Button)findViewById(R.id.btn_show_BottomSheetDialog1);
        Button showBottomSheetDialogBtn2 = (Button)findViewById(R.id.btn_show_BottomSheetDialog2);
        Button showBottomSheetDialogBtn3 = (Button)findViewById(R.id.btn_show_BottomSheetDialog3);
        Button showBottomSheetDialogFragmentBtn = (Button)findViewById(R.id.btn_show_BottomSheetDialogFragment);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(0);//设置内容栏默认高度

        showBottomSheetBtn.setOnClickListener(this);
        showBottomSheetDialogBtn1.setOnClickListener(this);
        showBottomSheetDialogBtn2.setOnClickListener(this);
        showBottomSheetDialogBtn3.setOnClickListener(this);
        showBottomSheetDialogFragmentBtn.setOnClickListener(this);

        /*mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //这里是bottomSheet状态的改变
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //这里是拖拽中的回调，根据slideOffset可以做一些动画

            }
        });*/

        initGestureDetector();
    }

    private void initGestureDetector() {
        //这里使用的是界面手势识别器,可以识别界面的单击事件,来给Behavior设置相应的属性来控制隐藏.
        mGestureDetector = new GestureDetector(MainActivity.this,
                new GestureDetector.SimpleOnGestureListener(){
                    //处理单击事件 -- 控制面板的显示or隐藏
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            return true;
                        }
                        return super.onSingleTapConfirmed(e);
                    }
                });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_BottomSheet:
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;

            case R.id.btn_show_BottomSheetDialog1:
                initBottomSheetDialog1();
                break;

            case R.id.btn_show_BottomSheetDialog2:
                initBottomSheetDialog2();
                break;

            case R.id.btn_show_BottomSheetDialog3:
                //使用BottomSheetDialog实现简单分享功能
                initBottomSheetDialog3();
                break;

            case R.id.btn_show_BottomSheetDialogFragment:
                new FullSheetDialogFragment().show(getSupportFragmentManager(), "dialog");
                break;
        }
    }

    //展示BottomSheetDialog,少量数据
    private void initBottomSheetDialog1() {
        final BottomSheetDialog bottomSheetDialog  = new BottomSheetDialog(this);
        View inflate = View.inflate(this, R.layout.dialog_bottom_sheet1, null);
        View qq = inflate.findViewById(R.id.share_qq);
        View wx = inflate.findViewById(R.id.share_wx);
        View sina = inflate.findViewById(R.id.share_sina);
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "分享到qq", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });
        wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "分享到wx", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });
        sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "分享到sina", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(inflate);
        bottomSheetDialog.show();
    }

    //展示BottomSheetDialog，列表形式
    private void initBottomSheetDialog2() {
        List<String> mList;
        mList = new ArrayList<>();
        for(int i=0; i<20; i++){
            mList.add("item "+i);
        }

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        //创建recyclerView
        RecyclerView recyclerView = new RecyclerView(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(mList,this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View item, int position) {
                Toast.makeText(MainActivity.this, "item "+position, Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(recyclerView);
        bottomSheetDialog.show();
    }

    //使用BottomSheetDialog实现简单分享功能
    private void initBottomSheetDialog3() {
        RecyclerView recyclerView;
        //startActivity(new Intent(MainActivity.this,RecyclerVireWithBottomSheetDialogActivity.class));
        BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this);
        View view = View.inflate(MainActivity.this, R.layout.dialog_bottom_sheet2, null);
        dialog.setContentView(view);
        recyclerView = (RecyclerView) dialog.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new ShareRecyclerViewAdapter(MainActivity.this,dialog));
        dialog.show();


    }


}
