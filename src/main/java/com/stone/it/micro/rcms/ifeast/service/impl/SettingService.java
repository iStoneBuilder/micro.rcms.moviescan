package com.stone.it.micro.rcms.ifeast.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.stone.it.micro.rcms.ifeast.dao.IRefreshDao;
import com.stone.it.micro.rcms.ifeast.dao.ISettingDao;
import com.stone.it.micro.rcms.ifeast.service.ISettingService;
import com.stone.it.micro.rcms.ifeast.utils.CommonBaseUtil;
import com.stone.it.micro.rcms.ifeast.utils.FileTmdbMetaUtil;
import com.stone.it.micro.rcms.ifeast.utils.WebDavBaseUtil;
import com.stone.it.micro.rcms.ifeast.vo.FileInfoVO;
import com.stone.it.micro.rcms.ifeast.vo.ResourceVO;
import com.stone.it.micro.rcms.ifeast.vo.SearchVO;
import com.stone.it.micro.rcms.ifeast.vo.SettingVO;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cj.stone
 * @Date 2022/12/9
 * @Desc 设置webdav信息
 */
@Named
public class SettingService extends CommonService implements ISettingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingService.class);

    private static String refreshId;

    @Inject
    private ISettingDao settingDao;
    @Inject
    private IRefreshDao refreshDao;


    @Override
    public List<SettingVO> getSettingList(SettingVO setting) throws Exception {
        return settingDao.getSettingList(setting);
    }

    @Override
    public SettingVO getSettingById(SettingVO settingVO) throws Exception {
        return null;
    }

    @Override
    public JSONObject refreshResource() throws Exception {
        if (refreshId != null) {
            LOGGER.info("data refreshing , please wait .......");
            return new JSONObject();
        }
        // 查询配置信息
        List<SettingVO> list = settingDao.getSettingList(new SettingVO());
        LOGGER.info("get setting data " + JSON.toJSONString(list));
        refreshId = CommonBaseUtil.getUuid();
        FileInfoVO fileInfoVO = new FileInfoVO();
        fileInfoVO.setRefreshId(refreshId);
        try {
            // 循环查询数据
            for (SettingVO setting : list) {
                getNasData(setting, "/iMovie/", true);
                getNasData(setting, "/iTv/", false);
                fileInfoVO.setSettingId(setting.getSettingId());
                // 删除没有资源
                refreshDao.deleteFileInfo(fileInfoVO);
            }
            refreshId = CommonBaseUtil.getUuid();
            // 刷新元数据
            refreshMetaData();
        } finally {
            refreshId = null;
        }
        return new JSONObject();
    }

    @Override
    public JSONObject getApi() throws Exception {
        // 获取所有 JAX-RS server 的服务路径
        return new JSONObject();
    }

    @Override
    public JSONObject getTest() throws Exception {
        return null;
    }

    private void refreshMetaData() throws Exception {
        FileInfoVO fileInfoVO = new FileInfoVO();
        fileInfoVO.setRefreshId(refreshId);
        List<FileInfoVO> list = null;
        do {
            list = refreshDao.findNotMatchData(fileInfoVO);
            // 刷新ID排除查询
            list.forEach(fileInfo -> {
                try {
                    fileInfo.setRefreshId(refreshId);
                    // 更新数据
                    refreshDao.updateFileInfo(fileInfo);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            // 查询元数据
            handleFileMeta(list);
        } while (list.size() == 20);
    }

    private void handleFileMeta(List<FileInfoVO> list) throws Exception {
        SearchVO searchVO = new SearchVO();
        for (FileInfoVO fileInfoVO : list) {
            searchVO.setPage(1);
            handleFileName(searchVO, fileInfoVO.getFileName());
            String searchUri = tmdbAddress + "/search/" + fileInfoVO.getFileType();
            // 查询参数处理
            Map<String, String> params = handleQuery(searchVO);
            // 查询数据
            ResourceVO resource = FileTmdbMetaUtil.getTmdbMeta(params, fileInfoVO, searchUri);
            if (resource != null) {
                refreshDao.createFileMeta(resource);
            }
        }
    }


    private void getNasData(SettingVO setting, String filePath, boolean isMovie) throws Exception {
        List<FileInfoVO> files = WebDavBaseUtil.getWebDavData(setting, filePath, isMovie);
        LOGGER.info("handle file size " + files.size());
        for (FileInfoVO fileInfoVO : files) {
            fileInfoVO.setRefreshId(refreshId);
            List<FileInfoVO> list = refreshDao.findFileExist(fileInfoVO);
            if (list.size() > 0) {
                FileInfoVO iFile = list.get(0);
                iFile.setRefreshId(refreshId);
                refreshDao.updateFileInfo(iFile);
            } else {
                refreshDao.createFileInfo(fileInfoVO);
            }
        }
    }

}
