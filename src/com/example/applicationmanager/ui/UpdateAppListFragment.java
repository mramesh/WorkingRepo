
package com.example.applicationmanager.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.applicationmanager.R;
import com.example.applicationmanager.vo.AppInfo;

public class UpdateAppListFragment extends ListFragment {

    ArrayList<AppInfo> installedApps = new ArrayList<AppInfo>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // start async task to get the installed applist

        GetInstalledAppAsyncTask task = new GetInstalledAppAsyncTask();
        task.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootViw = inflater.inflate(R.layout.fragment_update_app, null);
        return rootViw;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void onPostExecuteOfGetInstalledAppListTask() {

        setListAdapter(new ListInstalledAppAdapter(getActivity().getApplicationContext(),
                installedApps));
    }

    private void getInstalledAppList() {

        PackageManager packageManager = getActivity().getPackageManager();
        List<PackageInfo> apps = packageManager
                .getInstalledPackages(PackageManager.SIGNATURE_MATCH);
        if (apps != null && !apps.isEmpty()) {

            for (int i = 0; i < apps.size(); i++) {
                PackageInfo p = apps.get(i);
                ApplicationInfo appInfo = null;
                try {
                    appInfo = packageManager.getApplicationInfo(p.packageName, 0);
                    AppInfo app = new AppInfo();
                    app.setName(p.applicationInfo.loadLabel(packageManager).toString());
                    app.setPackageName(p.packageName);
                    app.setVersionName(p.versionName);
                    app.setVersionCode(p.versionCode);
                    app.setIcon(p.applicationInfo.loadIcon(packageManager));

                    System.out.println(" ***************************** ");
                    System.out.println(" App Name ==== > " + app.getName());
                    System.out.println(" Package Name ==== > " + app.getPackageName());

                    // check if the application is not an application system
                    Intent launchIntent = app.getLaunchIntent(getActivity());
                    if (launchIntent != null && (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        installedApps.add(app);
                    }
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            // sort the list of applications alphabetically
            if (installedApps.size() > 0) {
                Collections.sort(installedApps, new Comparator<AppInfo>() {
                    @Override
                    public int compare(final AppInfo app1, final AppInfo app2) {
                        return app1.getName().toLowerCase(Locale.getDefault())
                                .compareTo(app2.getName().toLowerCase(Locale.getDefault()));
                    }

                });
            }
        }
    }

    private class GetInstalledAppAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            getInstalledAppList();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result) {
                onPostExecuteOfGetInstalledAppListTask();
            }
        }

    }

    private class ListInstalledAppAdapter extends BaseAdapter {

        List<AppInfo> installedAppList;

        Context applicationContext;

        public ListInstalledAppAdapter(Context applicationContext, List<AppInfo> installedAppList) {
            this.applicationContext = applicationContext;
            this.installedAppList = installedAppList;
        }

        @Override
        public int getCount() {
            return installedAppList.size();
        }

        @Override
        public Object getItem(int position) {
            return installedAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(applicationContext).inflate(
                        R.layout.update_app_list_item, parent, false);

                holder.txtAppName = (TextView)convertView.findViewById(R.id.txt_installed_app_name);
                holder.txtAppSize = (TextView)convertView.findViewById(R.id.txt_app_size);
                holder.txtAppInstalledDate = (TextView)convertView
                        .findViewById(R.id.txt_app_installed_date);

                holder.imageAppIcon = (ImageView)convertView.findViewById(R.id.img_car);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            AppInfo appInfo = installedAppList.get(position);
            holder.txtAppName.setText(appInfo.getName());
            holder.txtAppSize.setText(appInfo.getName());
            holder.txtAppInstalledDate.setText(appInfo.getVersionName());
            holder.imageAppIcon.setImageDrawable(appInfo.getIcon());
            return convertView;
        }

    }

    private class ViewHolder {
        TextView txtAppName;

        TextView txtAppSize;

        TextView txtAppInstalledDate;

        ImageView imageAppIcon;
    }

}
