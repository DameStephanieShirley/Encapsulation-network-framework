package com.jash.loredemo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jash on 16-4-1.
 */
public class Response {
    private boolean status;
    private int total;
    @SerializedName("tngou")
    private List<Lore> lores;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Lore> getLores() {
        return lores;
    }

    public void setLores(List<Lore> lores) {
        this.lores = lores;
    }
}
