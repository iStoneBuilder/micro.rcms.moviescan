package com.stone.it.micro.rcms.ifeast.service;

import com.alibaba.fastjson.JSONObject;
import com.stone.it.micro.rcms.ifeast.vo.SettingVO;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * @author cj.stone
 * @Date 2022/12/9
 * @Desc
 */

@Path("/settingService")
@Produces("application/json")
@Consumes("application/json")
public interface ISettingService {

    @GET
    @Path("/find/list")
    List<SettingVO> getSettingList(@QueryParam("")SettingVO setting) throws Exception;

    @GET
    @Path("/find/detail/{settingId}")
    SettingVO getSettingById(@PathParam("")SettingVO settingVO) throws Exception;

    @GET
    @Path("/refresh")
    JSONObject refreshResource() throws Exception;

    @GET
    @Path("/getapi")
    JSONObject getApi() throws Exception;

    JSONObject getTest() throws Exception;

}
