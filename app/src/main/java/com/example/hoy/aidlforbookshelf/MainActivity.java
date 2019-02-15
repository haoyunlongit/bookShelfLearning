package com.example.hoy.aidlforbookshelf;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.duokan.home.sdk.DkHomeAgent;
import com.duokan.home.sdk.DkHomeBookInfo;
import com.duokan.home.sdk.QueryCallback;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public GridView girdView;
    public List<DkHomeBookInfo> bookInfoArr;
    private Handler mainHander = new Handler(Looper.getMainLooper());
    DkHomeAgent dkHomeAgent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dkHomeAgent = DkHomeAgent.init(this);

        dkHomeAgent.searchBooks("我的" ,new QueryCallback<List<DkHomeBookInfo>>() {
            @Override
            public void onQueryBack(DkHomeAgent agent, int code, List<DkHomeBookInfo> result) {
                bookInfoArr = result;
                setupView();
            }
        });


//        dkHomeAgent.queryRecommendBooks(new QueryCallback<List<DkHomeBookInfo>>() {
//            @Override
//            public void onQueryBack(DkHomeAgent dkHomeAgent, int i, List<DkHomeBookInfo>
//                    dkHomeBookInfos) {
//                bookInfoArr = dkHomeBookInfos;
//                setupView();
//            }
//        });
//        dkHomeAgent.queryBookshelfBooks(new QueryCallback<List<DkHomeBookInfo>>() {
//            @Override
//            public void onQueryBack(DkHomeAgent dkHomeAgent, int i, List<DkHomeBookInfo>
//                    dkHomeBookInfos) {
//                bookInfoArr = dkHomeBookInfos;
//                setupView();
//            }
//        });

    }


    public void setupView() {
        girdView = findViewById(R.id.mainView);
        final LinkedList<HashMap<String, String>> list = new LinkedList();
        for (int i = 0; i < bookInfoArr.size() ; i ++) {
            try {
                DkHomeBookInfo bookInfo = bookInfoArr.get(i);
                HashMap hashMap = new HashMap();
                hashMap.put("title", bookInfo.title);
                hashMap.put("coverUri", bookInfo.onlineCoverUri);
                hashMap.put("id", bookInfo.bookId);
                hashMap.put("position", String.valueOf(bookInfo.progress));
                list.add(hashMap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(),list, R.layout.item, new String[]{"title"},
                new int[]{ R.id.itemText} ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View cellView = super.getView(position, convertView, parent);
                ImageView tempView = (ImageView)cellView.findViewById(R.id.itemImage);
                HashMap<String, String> hashMap = list.get(position);
                String imageUrl = hashMap.get("coverUri");

                String tempPosition = hashMap.get("position");
                TextView positionView = (TextView)cellView.findViewById(R.id.position);
                positionView.setText("已读"+ tempPosition + "%");

                Uri uri = Uri.parse(imageUrl);
                Glide.with(getBaseContext())
                        .load(uri)
                        .into(tempView);
                return cellView;
            }
        };

        girdView.setAdapter(adapter);

        girdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    try {
                        dkHomeAgent.gotoBookshelf();
                    } catch (Exception e) {

                    }
                } else {
                    try {
                        dkHomeAgent.gotoBookstore();
                    } catch (Exception e) {

                    }
                }

//
//                HashMap<String, String> hashMap = list.get(position);
//                String idStr = hashMap.get("id");
//
//                try {
//                    dkHomeAgent.openBook(idStr);
//                } catch (Exception e) {
//
//                }

            }
        });
    }

}
