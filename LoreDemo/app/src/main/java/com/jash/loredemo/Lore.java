package com.jash.loredemo;

import android.widget.ImageView;
import android.widget.TextView;

import com.jash.tools.viewutil.BindView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jash on 16-4-1.
 */
public class  Lore  {
    @BindView(resId = R.id.item_title, bindMethod = "setText", bindType = CharSequence.class)
    private String title;//资讯标题
    private int infoclass;//分类
    @BindView(resId = R.id.item_icon, adapterMethod = "loadImage", viewType = ImageView.class)
    private String img;//图片
    private String description;//描述
//    @BindView(resId = R.id.item_keywords, bindMethod = "setText", bindType = CharSequence.class)
    private String keywords;//关键字
    //    private String message;//资讯内容
    private int count ;//访问次数
    private int fcount;//收藏数
    private int rcount;//评论读数
    @BindView(resId = R.id.item_keywords, adapterMethod = "showDate", viewType = TextView.class)
    private Date time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getInfoclass() {
        return infoclass;
    }

    public void setInfoclass(int infoclass) {
        this.infoclass = infoclass;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFcount() {
        return fcount;
    }

    public void setFcount(int fcount) {
        this.fcount = fcount;
    }

    public int getRcount() {
        return rcount;
    }

    public void setRcount(int rcount) {
        this.rcount = rcount;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public static void loadImage(ImageView view, String img){
        Picasso.with(view.getContext())
                .load("http://tnfs.tngou.net/img"+img)
                .into(view);
    }
    public static void showDate(TextView text, Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        text.setText("发布时间:" + sdf.format(date));
    }
}