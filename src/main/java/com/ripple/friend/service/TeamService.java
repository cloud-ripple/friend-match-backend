package com.ripple.friend.service;

import com.ripple.friend.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ripple.friend.model.domain.User;
import com.ripple.friend.model.dto.TeamQuery;
import com.ripple.friend.model.request.TeamUpdateRequest;
import com.ripple.friend.model.vo.TeamUserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 花海
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2023-12-12 20:33:27
*/
public interface TeamService extends IService<Team> {

    /**
     * 创建队伍
     * @param team 添加队伍参数
     * @param loginUser 当前登录用户
     * @return
     */
    long addTeam(Team team, User loginUser);


    /**
     * 搜索队伍
     * @param teamQuery 队伍查询参数
     * @param isAdmin 是否为管理员
     * @return
     */
    List<TeamUserVo> listTeams(TeamQuery teamQuery, boolean isAdmin);

    /**
     * 修改队伍
     * @param teamUpdateRequest 更新队伍参数
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, HttpServletRequest request);

}
