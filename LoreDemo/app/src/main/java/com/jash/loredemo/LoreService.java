package com.jash.loredemo;

import com.jash.tools.network.NetworkTask;
import com.jash.tools.network.UrlString;

/**
 * Created by jash on 16-4-1.
 */
public interface LoreService {
    @UrlString("http://www.tngou.net/api/lore/list?id=%d&page=%d&rows=%d")
    NetworkTask<Response> getLoreList(int id, int page, int rows);
}
