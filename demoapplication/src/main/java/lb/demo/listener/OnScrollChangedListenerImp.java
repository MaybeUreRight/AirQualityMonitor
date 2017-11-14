package lb.demo.listener;

import lb.demo.view.MyHScrollView;

/**
 * Created by liubo on 2017/9/29.
 */

public class OnScrollChangedListenerImp implements MyHScrollView.OnScrollChangedListener {
    MyHScrollView mScrollViewArg;

    public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
        mScrollViewArg = scrollViewar;
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        mScrollViewArg.smoothScrollTo(l, t);
    }
}
