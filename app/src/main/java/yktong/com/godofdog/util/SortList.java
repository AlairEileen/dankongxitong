package yktong.com.godofdog.util;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * Created by vampire on 2017/7/11.
 */

public class SortList<E> {
    public void Sort(List<E> list, final String method, final String sort) {
        Collections.sort(list, (a, b) -> {
            int ret = 0;
            try {
                Method m1 = ((E) a).getClass().getMethod(method, null);
                Method m2 = ((E) b).getClass().getMethod(method, null);
                Long s1 = Long.valueOf(m2.invoke(((E) b), null).toString());
                Long s2 = Long.valueOf(m1.invoke(((E) a), null).toString());
                if (sort != null && "desc".equals(sort)) {
                    // 倒序
                    ret = s1 > s2 ? 1 : -1;
                } else {
                    // 正序
                    ret = s2 > s1 ? 1 : -1;
                }
            } catch (NoSuchMethodException ne) {
                Log.d("SortList", "ne:" + ne);
            } catch (IllegalAccessException ie) {
                Log.d("SortList", "ie:" + ie);
            } catch (InvocationTargetException it) {
                Log.d("SortList", "it:" + it);
            }
            return ret;
        });
    }
}
