
package com.example.applicationmanager.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.applicationmanager.R;
import com.example.applicationmanager.vo.DocInfo;

public class DocumentListActivity extends FragmentActivity {

    DocumentListAdapter mAdapter = null;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_list);
        listView = (ListView)findViewById(R.id.list);

        // get the documents list

        GetDocumentAsyncTask getDocumentAsyncTask = new GetDocumentAsyncTask();
        getDocumentAsyncTask.execute();

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DocInfo docInf = (DocInfo)view.getTag();
                if (docInf != null) {
                    startActivity(docInf.getLaunchIntent(getApplicationContext()));
                }
            }
        });

    }

    private void setListAdapter(List<DocInfo> docList) {
        mAdapter = new DocumentListAdapter(getApplicationContext(), docList);
        listView.setAdapter(mAdapter);
    }

    private class GetDocumentAsyncTask extends AsyncTask<String, Void, List<DocInfo>> {

        String pdfPattern = ".pdf";

        List<DocInfo> list = new ArrayList<DocInfo>();

        @Override
        protected List<DocInfo> doInBackground(String... params) {

            File file = Environment.getExternalStorageDirectory();

            getFiles(file);
            return list;
        }

        @Override
        protected void onPostExecute(List<DocInfo> result) {
            super.onPostExecute(result);
            if (result != null) {
                setListAdapter(result);
            }
        }

        private void getFiles(File file) {

            File listFile[] = file.listFiles();
            if (listFile != null) {
                for (int i = 0; i < listFile.length; i++) {

                    if (listFile[i].isDirectory()) {
                        getFiles(listFile[i]);
                    } else {
                        if (listFile[i].getName().endsWith(pdfPattern)) {
                            // Do what ever u want
                            DocInfo docInfo = new DocInfo();
                            docInfo.setFileName(listFile[i].getName());
                            docInfo.setPath(listFile[i].getAbsolutePath());
                            docInfo.setFileType(pdfPattern);

                            list.add(docInfo);

                        }
                    }
                }
            }
        }

    }

    private class DocumentListAdapter extends BaseAdapter {

        List<DocInfo> docsList;

        Context context;

        public DocumentListAdapter(Context applicationContext, List<DocInfo> docsList) {
            this.docsList = docsList;
            this.context = applicationContext;
        }

        @Override
        public int getCount() {
            return docsList.size();
        }

        @Override
        public Object getItem(int position) {
            return docsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater)context
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.document_list_item, null);
            }

            ImageView imgIcon = (ImageView)convertView.findViewById(R.id.icon);
            TextView txtTitle = (TextView)convertView.findViewById(R.id.title);

            imgIcon.setImageResource(R.drawable.ic_pdf);
            txtTitle.setText(docsList.get(position).getFileName());
            convertView.setTag(docsList.get(position));
            return convertView;
        }

    }

}
