package lb.demo.listener;

import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import lb.demo.R;

/**
 * Created by liubo on 2017/9/29.
 */

public class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {
    private View view;
    public ListViewAndHeadViewTouchLinstener(View view) {
        this.view = view;
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        HorizontalScrollView headSrcrollView = (HorizontalScrollView) view.findViewById(R.id.horizontalScrollView1);
        headSrcrollView.onTouchEvent(arg1);
        return false;
    }
}
