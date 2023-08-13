package com.stone.it.rcms.ifeast.service;

import com.alibaba.fastjson.JSONObject;
import com.stone.it.rcms.ifeast.vo.SettingVO;
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

@Path("/setting")
@Produces("application/json")
@Consumes("application/json")
public interface ISettingService {

    @GET
    @Path("/records/list")
    List<SettingVO> getSettingList(@QueryParam("")SettingVO setting) throws Exception;

    @GET
    @Path("/records/{settingId}")
    SettingVO getSettingById(@PathParam("")SettingVO settingVO) throws Exception;

    @GET
    @Path("/refresh")
    JSONObject refreshResource() throws Exception;


}
