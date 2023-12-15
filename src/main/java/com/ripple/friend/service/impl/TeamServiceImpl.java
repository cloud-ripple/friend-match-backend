package com.ripple.friend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ripple.friend.common.ErrorCode;
import com.ripple.friend.exception.BusinessException;
import com.ripple.friend.model.domain.Team;
import com.ripple.friend.model.domain.User;
import com.ripple.friend.model.domain.UserTeam;
import com.ripple.friend.model.dto.TeamQuery;
import com.ripple.friend.model.enums.TeamStatusEnum;
import com.ripple.friend.model.request.TeamUpdateRequest;
import com.ripple.friend.model.vo.TeamUserVo;
import com.ripple.friend.model.vo.UserVo;
import com.ripple.friend.service.TeamService;
import com.ripple.friend.mapper.TeamMapper;
import com.ripple.friend.service.UserService;
import com.ripple.friend.service.UserTeamService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 花海
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2023-12-12 20:33:27
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {

    @Resource
    private UserTeamService userTeamService;

    @Resource
    private UserService userService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addTeam(Team team, User loginUser) {
        //1. 请求参数是否为空？
        if (team == null || loginUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        //2. 是否登录，未登录不允许创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "未登录");
        }
        //3. 校验信息
        /*
            a. 队伍人数 > 1 且 <= 20
            b. 队伍标题 <= 20
            c. 描述信息 <= 512
            d. status 是否公开(int)，不传默认 0 公开
            e. 如果 status 是加密状态，一定要有密码，且密码  <= 32
            f. 队伍过期时间 > 当前时间
            g. 校验用户最多创建 5 个队伍
         */
        // 如果为空，给一个指定的默认值
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不满足要求，最多20");
        }
        String name = team.getTeamName();
        if (StringUtils.isBlank(name) || name.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题不满足要求");
        }
        String desc = team.getDescription();
        if (StringUtils.isBlank(desc) || name.length() > 500) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述过长");
        }
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum teamStatusEnum = TeamStatusEnum.getEnumByValue(status);
        if (teamStatusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不满足要求");
        }
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(teamStatusEnum)) {
            if (StringUtils.isBlank(password) || password.length() > 32) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍加密密码设置不合法");
            }
        }
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍过期时间 > 当前时间");
        }
        // todo 可能同时创建100个队伍
        final long userId = loginUser.getId();
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long hasTeamNum = this.count(queryWrapper);
        if (hasTeamNum >= 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "当前用户最多可以创建 5 个队伍");
        }
        //4. 插入队伍信息到队伍表
        team.setId(null); //自增
        team.setUserId(userId);
        boolean save = this.save(team);
        // 插入数据后得到生成的主键id
        Long teamId = team.getId();
        if (!save || teamId == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建队伍失败");
        }
        //5. 插入用户 == 队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        boolean save1 = userTeamService.save(userTeam);
        if (!save1) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "插入用户队伍关系失败");
        }
        return teamId; //返回队伍id
    }

    @Override
    public List<TeamUserVo> listTeams(TeamQuery teamQuery, boolean isAdmin) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        //从请求参数中取出队伍名称、最大人数等查询条件，如果存在则作为组合查询条件 and 方式拼接
        if (teamQuery != null) {
            Long id = teamQuery.getId();
            // 根据队伍id查询
            if (id != null && id > 0) {
                queryWrapper.eq("id", id);
            }
            // 根据队伍名查询
            String teamName = teamQuery.getTeamName();
            if (StringUtils.isNotBlank(teamName)) { //判空
                queryWrapper.like("teamName", teamName);
            }
            // 根据队伍描述查询
            String description = teamQuery.getDescription();
            if (StringUtils.isNotBlank(description)) {
                queryWrapper.like("description", description);
            }
            // 也可以通过某个关键词同时对队伍名称和描述查询，此时前端不用传入 teamName、description参数
            String searchText = teamQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)) {
                queryWrapper.and(qw -> qw.like("teamName", searchText).or().like("description", searchText));
            }
            Integer status = teamQuery.getStatus();
            // 根据状态获取枚举值
            TeamStatusEnum teamStatusEnum = TeamStatusEnum.getEnumByValue(status);
            if (teamStatusEnum == null) {
                // 如果为空默认查询公开的
                teamStatusEnum = TeamStatusEnum.PUBLIC;
            }
            // 根据状态查询，只有管理员才能查看私有或加密的队伍
            if (!isAdmin && !teamStatusEnum.equals(TeamStatusEnum.PUBLIC)) {
                throw new BusinessException(ErrorCode.NO_AUTH);
            }
            // 根据队伍状态查询  status 0-公开、1-私有、2-加密
            queryWrapper.eq("status", teamStatusEnum.getValue());

            Integer maxNum = teamQuery.getMaxNum();
            // 根据最大人数查询
            if (maxNum != null && maxNum > 0) {
                queryWrapper.eq("maxNum", maxNum);
            }
            Long userId = teamQuery.getUserId();
            // 根据创建人查询
            if (userId != null && userId > 0) {
                queryWrapper.eq("userId", userId);
            }
        }
        // 队伍过期时间为null代表永久生效，信息流中不展示已过期的队伍数据
        // and expireTime is null or expireTime > now()
        queryWrapper.and(qw -> qw.isNull("expireTime").or().gt("expireTime", new Date()));
        // 1. 根据以上查询条件，查询出所有队伍信息
        List<Team> teamList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(teamList)) {
            return new ArrayList<>();
        }

        // 2. 关联查询创建人的用户信息
        /* 实现方式：
        （1）自己写SQL
        查询队伍和创建人的信息
        select *
        from team t
            left join user u on t.userId = u.id;
        查询队伍和已加入队伍成员的信息
        select *
        from team t
             left join user_team ut on t.id = ut.teamId
             left join user u on ut.userId = u.id;
        （2）手写逻辑
         */

        // 队伍用户信息列表（脱敏）
        List<TeamUserVo> teamUserVoList = new ArrayList<>();
        // 遍历查询到的队伍列表，封装队伍-用户关系信息，添加到列表
        for (Team team : teamList) {
            Long userId = team.getUserId();
            if (userId == null) {
                continue;
            }
            // 根据当前队伍的创建人id查询用户
            User user = userService.getById(userId);
            // 队伍-用户关系信息（主要包含队伍、创建人信息）
            TeamUserVo teamUserVo = new TeamUserVo();
            // 把前者对象的字段属性复制给后对象，没有匹配对应的字段不会赋值
            BeanUtils.copyProperties(team, teamUserVo);
            if (user != null) {
                // 脱敏用户
                UserVo userVo = new UserVo();
                BeanUtils.copyProperties(user, userVo);
                // 设置队伍创建人
                teamUserVo.setCreateUser(userVo);
            }
            // 把封装好的队伍-用户关系信息添加到列表篇
            teamUserVoList.add(teamUserVo);
        }

        return teamUserVoList;
    }

    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
        /*
            1. 判断请求参数是否为空
            2. 先查询队伍是否存在
            3. 只有管理员或者队伍创建者可以修改
            4. 如果用户传入的字段新值和老值一致，就不用 update 了（降低数据库使用）
         */
        Long id = teamUpdateRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 从数据库中查询队伍
        Team oldTeam = this.getById(id);
        if (oldTeam == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在");
        }
        // 如果当前登录用户不是该修改队伍的创建者，并且不是管理员
        User loginUser = userService.getLoginUser(request);
        if (oldTeam.getUserId() != loginUser.getId() && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        Team updateTeam = new Team();
        BeanUtils.copyProperties(teamUpdateRequest, updateTeam);
        boolean update = this.updateById(updateTeam);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "修改队伍失败");
        }
        return true;
    }
}




