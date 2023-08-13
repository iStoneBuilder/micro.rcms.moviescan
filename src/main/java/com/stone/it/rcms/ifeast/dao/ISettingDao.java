package com.stone.it.rcms.ifeast.dao;


import com.stone.it.rcms.ifeast.vo.SettingVO;
import java.util.List;

/**
 * @author cj.stone
 * @Date 2022/12/9
 * @Desc
 */
public interface ISettingDao {

    List<SettingVO> getSettingList(SettingVO setting) throws Exception;

    SettingVO getSettingById(SettingVO settingVO) throws Exception;
}
