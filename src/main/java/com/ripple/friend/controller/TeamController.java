package com.ripple.friend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ripple.friend.common.BaseResponse;
import com.ripple.friend.common.ErrorCode;
import com.ripple.friend.common.ResultUtils;
import com.ripple.friend.exception.BusinessException;
import com.ripple.friend.model.domain.Team;
import com.ripple.friend.model.domain.User;
import com.ripple.friend.model.dto.TeamQuery;
import com.ripple.friend.model.request.TeamAddRequest;
import com.ripple.friend.model.request.TeamUpdateRequest;
import com.ripple.friend.model.vo.TeamUserVo;
import com.ripple.friend.service.TeamService;
import com.ripple.friend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/team")
@Slf4j
public class TeamController {

    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    //注意 @RequestBody 请求体注解只有 Post 请求方式才会生效，Get 请求不生效，请求体参数封装不进去，
    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "team数据为空");
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        // 把请求参数的属性赋值给后者 team
        BeanUtils.copyProperties(teamAddRequest, team);
        Long teamId = teamService.addTeam(team, loginUser);

        return ResultUtils.success(teamId);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(long teamId) {
        if (teamId <= 0) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "teamId数据为空");
        }
        boolean result = teamService.removeById(teamId);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "team删除失败");
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request
    ) {
        /*
        1. 判断请求参数是否为空
        2. 先查询队伍是否存在
        3. 只有管理员或者队伍创建者可以修改
        4. 如果用户传入的字段新值和老值一致，就不用 update 了（降低数据库使用）
         */
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "teamUpdateRequest数据为空");
        }
        boolean result = teamService.updateTeam(teamUpdateRequest, request);
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "id数据为空");
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询失败");
        }
        return ResultUtils.success(team);
    }

    // 注意 @RequestBody 请求体注解只有 Post 请求方式才会生效，Get 请求不生效，请求体参数封装不进去，所以此处  @RequestBody TeamQuery 前面该注解无效，会报错
    @GetMapping("/list")
    public BaseResponse<List<TeamUserVo>> getTeams(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "teamQuery数据为空");
        }
        // 判断是否为管理员，只有管理员才能查看私有或加密的队伍
        boolean isAdmin = userService.isAdmin(request);
        if (!isAdmin) {
            throw new BusinessException(ErrorCode.NO_AUTH, "只有管理员才能查看私有或加密的队伍");
        }
        List<TeamUserVo> teamList = teamService.listTeams(teamQuery, isAdmin);
        return ResultUtils.success(teamList);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> getTeamsPage(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "teamQuery数据为空");
        }
        Team team = new Team();
        // 把后者对象的字段属性复制给前者对象
        try {
            BeanUtils.copyProperties(team, teamQuery);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Page page = new Page<>(teamQuery.getPageNum(), teamQuery.getPageSize());
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> pageResult = teamService.page(page, queryWrapper);

        return ResultUtils.success(pageResult);
    }

}