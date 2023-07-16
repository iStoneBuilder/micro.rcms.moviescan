package com.stone.it.micro.rcms.ifeast.service.impl;


import com.stone.it.micro.rcms.common.utils.MapUtil;
import com.stone.it.micro.rcms.ifeast.vo.SearchVO;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author cj.stone
 * @Date 2022/11/9
 * @Desc
 */
public class CommonService {

    protected final static String DEFAULT_LANGUAGE = "zh-CN";
    protected final static String MOVIE_KEY = "movie";
    protected final static String LOC_KEY = "loc";
    private final static String LANGUAGE_KEY = "language";
    @Value(value = "${tmdb.api.address}")
    protected String tmdbAddress;
    @Value(value = "${tmdb.api.key}")
    private String apiKey;

    public Map<String, String> handleQuery(Object query) throws Exception {
        Map<String, String> params = new HashMap<>();
        params = MapUtil.convertToMap(query);
        // 设置api_key
        params.put("api_key", apiKey);
        // 判断语言是否传语言，否设置默认值
        if(!params.containsKey(LANGUAGE_KEY)){
            params.put(LANGUAGE_KEY, DEFAULT_LANGUAGE);
        }
        return params;
    }


    public void handleFileName(SearchVO searchVO, String fileName) {
        // 处理（2022）
        fileName = fileName.split(" \\(")[0];
        // TV 判断是否是剧集
        fileName = fileName.split(" S")[0];
        fileName = fileName.replaceAll(" ", "%20");
        searchVO.setQuery(fileName);
    }

}
