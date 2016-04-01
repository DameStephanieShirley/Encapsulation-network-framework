package com.jash.tools.viewutil;

import android.content.Context;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by jash on 16-4-1.
 */
public class CommonAdapter<T> extends BaseAdapter {
    private Context context;
    private List<T> list;
    private Class<T> cl;
    private int layoutId;
    public CommonAdapter(Context context, List<T> list, Class<T> cl, int layoutId) {
        this.context = context;
        this.list = list;
        this.cl = cl;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            convertView.setTag(new ViewHolder(convertView, cl));
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        T t = list.get(position);
        for (Pair<View, Field> pair : holder.list) {
            BindView bindView = pair.second.getAnnotation(BindView.class);
            if (!TextUtils.isEmpty(bindView.bindMethod())) {
                try {
                    Method method = pair.first.getClass().getMethod(bindView.bindMethod(), bindView.bindType());
                    pair.second.setAccessible(true);
                    method.invoke(pair.first, pair.second.get(t));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (!TextUtils.isEmpty(bindView.adapterMethod())){
                try {
                    Method method = cl.getMethod(bindView.adapterMethod(), bindView.viewType(), pair.second.getType());
                    pair.second.setAccessible(true);
                    method.invoke(null, pair.first, pair.second.get(t));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return convertView;
    }

    public void addAll(Collection<? extends T> collection){
        list.addAll(collection);
        notifyDataSetChanged();
    }
    public static class ViewHolder{
        private List<Pair<View, Field>> list = new ArrayList<>();
        public ViewHolder(View itemView, Class<?> cl) {
            Field[] fields = cl.getDeclaredFields();
            for (Field f : fields) {
                BindView bindView = f.getAnnotation(BindView.class);
                if (bindView != null) {
                    View view = itemView.findViewById(bindView.resId());
                    Pair<View, Field> pair = Pair.create(view, f);
                    list.add(pair);
                }
            }
        }
    }
}
