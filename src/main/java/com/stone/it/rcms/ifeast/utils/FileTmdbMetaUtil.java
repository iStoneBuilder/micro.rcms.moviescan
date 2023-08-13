package com.stone.it.rcms.ifeast.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.stone.it.rcms.http.RequestUtil;
import com.stone.it.rcms.http.ResponseEntity;
import com.stone.it.rcms.ifeast.vo.FileInfoVO;
import com.stone.it.rcms.ifeast.vo.ResourceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author cj.stone
 * @Date 2022/12/10
 * @Desc
 */
public class FileTmdbMetaUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileTmdbMetaUtil.class);

    private static int total = 1;

    public static ResourceVO getTmdbMeta(Map<String, String> params, FileInfoVO fileInfoVO, String searchUri) throws Exception {
        total = 1;
        JSONArray data = getNetData(searchUri, params);
        if (data.size() == 1) {
            return handleTmdbResource(data.getJSONObject(0),fileInfoVO);
        }
        if (data.size() > 1) {
            return matchTmdbResource(params, searchUri, fileInfoVO, data);
        }
        return null;
    }

    private static JSONArray getNetData(String searchUri, Map<String, String> params) throws Exception {
        ResponseEntity response = RequestUtil.doGet(searchUri, params);
        if ("200".equals(response.getCode())) {
            JSONObject jsonObject = JSONObject.parseObject(response.getBody());
            total = jsonObject.getInteger("total_pages");
            return jsonObject.getJSONArray("results");
        }
        return new JSONArray();
    }

    private static ResourceVO matchTmdbResource(Map<String, String> params, String searchUri, FileInfoVO fileInfoVO,
                                                JSONArray datas) throws Exception {
        JSONObject matchData = getMatchResource(datas, fileInfoVO);
        if (matchData != null) {
            return handleTmdbResource(matchData,fileInfoVO);
        }
        for (int i = 1; i < total; i++) {
            params.put("page", String.valueOf(i + 1));
            JSONArray data = getNetData(searchUri, params);
            matchData = getMatchResource(datas, fileInfoVO);
            if (matchData != null) {
                return handleTmdbResource(matchData,fileInfoVO);
            }
        }
        return null;
    }

    private static JSONObject getMatchResource(JSONArray datas, FileInfoVO fileInfoVO) {
        return null;
    }

    /**
     *  解析匹配的数据
     * @param jsonObject
     * @return
     */
    private static ResourceVO handleTmdbResource(JSONObject jsonObject,FileInfoVO fileInfoVO) {
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setTmdbId(jsonObject.getString("id"));
        resourceVO.setTitle(getValueByKey(jsonObject,"name","title"));
        resourceVO.setLanguage(jsonObject.getString("original_language"));
        resourceVO.setMediaType(fileInfoVO.getFileType());
        resourceVO.setAdult(jsonObject.getString("adult"));
        resourceVO.setReleaseDate(getValueByKey(jsonObject,"first_air_date","release_date"));
        resourceVO.setRegions(jsonObject.getString("origin_country"));
        resourceVO.setGenres(jsonObject.getString("genre_ids"));
        resourceVO.setResourceData(jsonObject.toString());
        return resourceVO;
    }

    private static String getValueByKey(JSONObject data,String key1,String key2){
        return data.containsKey(key1) ? data.getString(key1) :data.getString(key2);
    }
}
