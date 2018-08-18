package cn.bluemobi.dylan.step.adapter;
import android.util.SparseArray;
import android.view.View;
/**
 * Created by 夏天 on 2018/8/12.
 */
public class CommonViewHolder {
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View chidlView = viewHolder.get(id);
        if (chidlView == null) {
            chidlView = view.findViewById(id);
            viewHolder.put(id, chidlView);
        }
        return (T) chidlView;
    }
}
