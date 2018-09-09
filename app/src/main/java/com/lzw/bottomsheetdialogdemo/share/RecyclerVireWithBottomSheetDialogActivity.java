package com.lzw.bottomsheetdialogdemo.share;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzw.bottomsheetdialogdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：带有RecyclerView的BottomSheetDialog
 * @author luzhaowei
 * @email 2497727771@qq.com
 * @time 2017/8/17 16:26
 */
public class RecyclerVireWithBottomSheetDialogActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<AppBean> list;
    BottomSheetDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initButton());

    }

    private LinearLayout initButton() {
        Button button = new Button(RecyclerVireWithBottomSheetDialogActivity.this);
        button.setText("test分享");
        ViewGroup.LayoutParams buttonParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(buttonParams);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog dialog = new BottomSheetDialog(RecyclerVireWithBottomSheetDialogActivity.this);
                View view = View.inflate(RecyclerVireWithBottomSheetDialogActivity.this, R.layout.bottom_sheet_dialog_share, null);
                dialog.setContentView(view);
                recyclerView = (RecyclerView) dialog.findViewById(R.id.recycler);
                recyclerView.setLayoutManager(new GridLayoutManager(RecyclerVireWithBottomSheetDialogActivity.this, 3));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(new Adapter());
                dialog.show();

            }
        });

        LinearLayout layout = new LinearLayout(RecyclerVireWithBottomSheetDialogActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(params);
        layout.addView(button);

        return layout;
    }

    public List<ResolveInfo> getShareApps(Context context) {
        List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
//      intent.setType("*/*");
        PackageManager pManager = context.getPackageManager();
        mApps = pManager.queryIntentActivities(intent,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        return mApps;
    }


    private List<AppBean> getShareAppList() {
        List<AppBean> shareAppInfos = new ArrayList<AppBean>();
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> resolveInfos = getShareApps(RecyclerVireWithBottomSheetDialogActivity.this);
        if (null == resolveInfos) {
            return null;
        } else {
            for (ResolveInfo resolveInfo : resolveInfos) {
                AppBean appBean = new AppBean();
                appBean.pkgName = (resolveInfo.activityInfo.packageName);
//              Log.i("getShareAppList", "pkg>" + resolveInfo.activityInfo.packageName + ";name>" + resolveInfo.activityInfo.name);
                appBean.appLauncherClassName = (resolveInfo.activityInfo.name);
                appBean.appName = (resolveInfo.loadLabel(packageManager).toString());
                appBean.icon = (resolveInfo.loadIcon(packageManager));
                shareAppInfos.add(appBean);
            }
        }
        return shareAppInfos;
    }

    public class Adapter extends RecyclerView.Adapter<ViewHolde> {
        Adapter() {
            list = getShareAppList();
            if (list == null) {
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                Log.e("11111", list.get(i).toString());
            }

        }

        @Override
        public ViewHolde onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolde(getLayoutInflater().inflate(R.layout.item_recyclerview_layout_share, null));
        }

        @Override
        public void onBindViewHolder(ViewHolde holder, final int position) {
            holder.appTextView.setText(list.get(position).appName);
            holder.iconImageView.setImageDrawable(list.get(position).icon);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    AppBean appBean = list.get(position);
                    shareIntent.setComponent(new ComponentName(appBean.pkgName, appBean.appLauncherClassName));
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "分享内容");
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(shareIntent);
                    if (dialog != null) {
                        dialog.dismiss();
                    }

//                    Intent intent2 = new Intent(Intent.ACTION_SEND);
//                    Uri uri = Uri.fromFile(new File("xxx.jpg"));
//                    intent2.setComponent(new ComponentName(appInfo.pkgName, appInfo.appLauncherClassName));
//                    intent2.putExtra(Intent.EXTRA_STREAM, uri);
//                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent2.setType("image/*");
//                    startActivity(Intent.createChooser(intent2, "share"));
                }
            });
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }
    }

    public class ViewHolde extends RecyclerView.ViewHolder {
        public ImageView iconImageView;
        public TextView appTextView;

        public ViewHolde(View itemView) {
            super(itemView);
            iconImageView = (ImageView) itemView.findViewById(R.id.app_icon_iv);
            appTextView = (TextView) itemView.findViewById(R.id.app_tv);
        }
    }

    class AppBean {
        public Drawable icon;
        public String appName;
        public String pkgName;
        public String appLauncherClassName;
    }



}
