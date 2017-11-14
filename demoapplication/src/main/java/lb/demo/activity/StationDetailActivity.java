package lb.demo.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import lb.demo.R;
import lb.demo.fragment.LBSFragment;
import lb.demo.fragment.ListFragment;
import lb.demo.fragment.StationDetailFragment;
import lb.demo.location.IntentStr;

/**
 *
 */
public class StationDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ssd_bottom_station;
    private ImageView ssd_bottom_station_iv;
    private LinearLayout ssd_bottom_unknow;
    private ImageView ssd_bottom_unknow_iv;
    private LinearLayout ssd_bottom_list;
    private ImageView ssd_bottom_list_iv;

    public String s_code;
    public String s_name;

    private FragmentManager fragmentManager;
    private List<Fragment> mFragmentList;
    private Fragment currentFragment;
    private ListFragment listFragment;
    private LBSFragment lbsFragment;
    private StationDetailFragment stationDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_station_detail);
        init();


    }

    private void init() {

        s_code = getIntent().getStringExtra(IntentStr.S_CODE);
        s_name = getIntent().getStringExtra(IntentStr.S_NAME);
        mFragmentList = new ArrayList<>();
        lbsFragment = new LBSFragment();
        listFragment = new ListFragment();
        stationDetailFragment = new StationDetailFragment();
        mFragmentList.add(stationDetailFragment);
        mFragmentList.add(lbsFragment);
        mFragmentList.add(listFragment);
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.ssd_fragment_container, mFragmentList.get(2)).commit();//.addToBackStack("ListFragment")
        fragmentManager.beginTransaction().add(R.id.ssd_fragment_container, mFragmentList.get(1)).commit();//.addToBackStack("ListFragment")
        fragmentManager.beginTransaction().add(R.id.ssd_fragment_container, mFragmentList.get(0)).commit();//.addToBackStack("StationDetailFragment")
        currentFragment = stationDetailFragment;

        ssd_bottom_station = findViewById(R.id.ssd_bottom_station);
        ssd_bottom_station_iv = findViewById(R.id.ssd_bottom_station_iv);
        ssd_bottom_unknow = findViewById(R.id.ssd_bottom_unknow);
        ssd_bottom_unknow_iv = findViewById(R.id.ssd_bottom_unknow_iv);
        ssd_bottom_list = findViewById(R.id.ssd_bottom_list);
        ssd_bottom_list_iv = findViewById(R.id.ssd_bottom_list_iv);
        ssd_bottom_unknow.setOnClickListener(this);
        ssd_bottom_station.setOnClickListener(this);
        ssd_bottom_list.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.ssd_back:
//                onBackPressed();
//                break;
            case R.id.ssd_bottom_station:
                if (currentFragment instanceof StationDetailFragment) {
                    return;
                }
                fragmentManager.beginTransaction().hide(mFragmentList.get(1)).hide(mFragmentList.get(2)).show(mFragmentList.get(0)).commit();
                currentFragment = stationDetailFragment;

                ssd_bottom_station_iv.setImageResource(R.drawable.ssd_bottom_station_selected);
                ssd_bottom_unknow_iv.setImageResource(R.drawable.ssd_bottom_unknow_unselected);
                ssd_bottom_list_iv.setImageResource(R.drawable.ssd_bottom_chart_unselected);
                break;
            case R.id.ssd_bottom_unknow:
                if (currentFragment instanceof LBSFragment) {
                    return;
                }
//                fragmentManager.beginTransaction().replace(R.id.ssd_fragment_container, mFragmentList.get(1)).commit();

                fragmentManager.beginTransaction().hide(mFragmentList.get(0)).hide(mFragmentList.get(2)).show(mFragmentList.get(1)).commit();
                currentFragment = lbsFragment;

                ssd_bottom_station_iv.setImageResource(R.drawable.ssd_bottom_station_unselected);
                ssd_bottom_unknow_iv.setImageResource(R.drawable.ssd_bottom_unknow_selected);
                ssd_bottom_list_iv.setImageResource(R.drawable.ssd_bottom_chart_unselected);
                break;
            case R.id.ssd_bottom_list:
                if (currentFragment instanceof ListFragment) {
                    return;
                }
                fragmentManager.beginTransaction().hide(mFragmentList.get(1)).hide(mFragmentList.get(0)).show(mFragmentList.get(2)).commit();
                currentFragment = listFragment;

                ssd_bottom_station_iv.setImageResource(R.drawable.ssd_bottom_station_unselected);
                ssd_bottom_unknow_iv.setImageResource(R.drawable.ssd_bottom_unknow_unselected);
                ssd_bottom_list_iv.setImageResource(R.drawable.ssd_bottom_chart_selected);
                break;
            default:
                break;
        }
    }

}
