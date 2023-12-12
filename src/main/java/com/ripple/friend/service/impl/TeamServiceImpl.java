package com.ripple.friend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ripple.friend.model.domain.Team;
import com.ripple.friend.service.TeamService;
import com.ripple.friend.mapper.TeamMapper;
import org.springframework.stereotype.Service;

/**
* @author 花海
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2023-12-12 20:33:27
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

}




