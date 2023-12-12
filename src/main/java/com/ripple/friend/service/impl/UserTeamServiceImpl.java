package com.ripple.friend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ripple.friend.model.domain.UserTeam;
import com.ripple.friend.service.UserTeamService;
import com.ripple.friend.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author 花海
* @description 针对表【user_team(用户-队伍关系)】的数据库操作Service实现
* @createDate 2023-12-12 20:39:21
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




