package com.example.appnew.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appnew.R;
import com.example.appnew.WebViewActivity;
import com.example.appnew.adapter.HienThiTinTucApdapter;
import com.example.appnew.enity.DanhMuc;
import com.example.appnew.enity.TinTuc;
import com.example.appnew.xmlpullparser.XmlPullParserHandler;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TinHotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TinHotFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TinHotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TinHotFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TinHotFragment newInstance(String param1, String param2) {
        TinHotFragment fragment = new TinHotFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tin_hot, container, false);
    }
    ListView lv;
    public List<TinTuc> ItemLists = new ArrayList<>();
    String link ;
    List<DanhMuc> lstDanhMuc;

    FirebaseFirestore firestore;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Activity activity = getActivity();

        lv = activity.findViewById(R.id.lv_news_hot);
        Bundle bundle = getArguments();
        if (bundle != null) {
            link = bundle.getString("link"); // Thay "key" bằng key duy nhất bạn đã sử dụng
            // Sử dụng giá trị nhận được
            // Ví dụ: textView.setText(value);
        }else {
            link = "https://vnexpress.net/rss/tin-moi-nhat.rss";
        }

        Toast.makeText(activity.getApplicationContext(), ""+link, Toast.LENGTH_SHORT).show();
        if (checkInternet()){
            downloadNew();
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openLink(i);
            }
        });
    }

    public boolean checkInternet(){
        return true;
    }
    public void openLink(int i){
        Toast.makeText(requireActivity().getApplicationContext(), ItemLists.get(i).getLinkBaiBao(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("linknews", ItemLists.get(i).getLinkBaiBao());
        startActivity(intent);
    }
    public void downloadNew(){
        new downloadXML(getActivity(), lv).execute(link);
    }
    public class downloadXML extends AsyncTask<String, Void, List<TinTuc>> {

        HienThiTinTucApdapter adapter;
        private ListView listView;
        private Context context;
        public downloadXML(Context context, ListView lv) {
            this.context = context;
            this.listView = lv;
        }

        @Override
        protected List<TinTuc> doInBackground(String... strings) {
            try {
                ItemLists  =  loadURLfromNetWork(strings[0]);
                return ItemLists;
            }catch (Exception e){
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<TinTuc> list) {
            super.onPostExecute(list);
            adapter = new HienThiTinTucApdapter(context, (ArrayList<TinTuc>) list);
            if (list != null){
                listView.setAdapter(adapter);
            }
        }
        private InputStream downloadURL(String url)throws IOException {
            java.net.URL url1 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream in = conn.getInputStream();
            //Log.i("00000", in.toString());
            return in;
        }
        public List<TinTuc> loadURLfromNetWork(String strUrl)throws Exception{
            InputStream stream = null;
            XmlPullParserHandler handler = new XmlPullParserHandler();
            ItemLists = null;
            try {
                stream = downloadURL(strUrl);
                Log.i("00000", stream.toString());
                ItemLists = handler.Pasers(stream);
            }finally {
                if (stream != null){
                    stream.close();
                }
            }
            Log.i("00000", String.valueOf(ItemLists.size()));
            return ItemLists;
        }
    }
}