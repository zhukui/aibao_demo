package com.bin.kndle.fragment;

import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bin.kndle.AbsFragment;
import com.bin.kndle.R;
import com.bin.kndle.adapter.DynamicAdapter;
import com.bin.kndle.adapter.HabitAdapter;
import com.bin.kndle.bean.DynamicResult;
import com.bin.kndle.bean.HabitResult;
import com.core.CommonResponse;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshListView;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;

import butterknife.Bind;

public class HabitFragment extends AbsFragment {

    @Bind(R.id.listview)
    PullToRefreshListView listview;
    @Bind(R.id.ll_listEmpty)
    LinearLayout llListEmpty;

    private ListView mActualListView;
    private HabitAdapter mAdapter;

    private ArrayList<HabitResult> arrayList = new ArrayList<>();

    private void initListView() {

        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                arrayList = null;
                getListData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getListData();
            }
        });

        mActualListView = listview.getRefreshableView();

        // Need to use the Actual ListView when registering for Context Menu
        registerForContextMenu(mActualListView);

        mAdapter = new HabitAdapter(getActivity(), arrayList);

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);

        animationAdapter.setAbsListView(mActualListView);

        mActualListView.setAdapter(animationAdapter);

        listview.setEmptyView(llListEmpty);
    }

    private void getListData() {
        int startIndex = arrayList == null || arrayList.size() <= 0 ? 0 : arrayList.size();
        activityHandler.sendEmptyMessageDelayed(INIT_LIST, 2500);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_dtnameic;
    }

    @Override
    public void EInit() {
        initListView();
        activityHandler.sendEmptyMessageDelayed(INIT_LIST, 500);
    }

    private static final int INIT_LIST = 0x01;//初始化数据处理
    private static final int LOAD_DATA = 0x02;//加载数据处理
    private static final int COUNT_MAX = 15;//加载数据最大值
    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case INIT_LIST:
                case LOAD_DATA:
                    loadData((CommonResponse) msg.obj, what);
                    break;
                default:
                    break;
            }
        }
    };

    private void loadData(CommonResponse resposne, int what) {
        dialogDismiss();
        if (true) {
            for (int i = 0; i < COUNT_MAX; i++) {
                if(arrayList==null){
                    arrayList = new ArrayList<>();
                }
                arrayList.add(HabitResult.testData());
            }
            mAdapter.refresh(arrayList);
        } else {
            //避免第一次应用启动时 创建fragment加载数据多次提示
        }
        listview.onRefreshComplete();
    }

}
