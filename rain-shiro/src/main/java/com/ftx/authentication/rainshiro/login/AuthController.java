package com.ftx.authentication.rainshiro.login;

import com.ftx.authentication.rainshiro.constant.APPEnums;
import com.ftx.authentication.rainshiro.constant.JsonObject;
import com.ftx.authentication.rainshiro.model.AuthUser;
import com.ftx.authentication.rainshiro.model.RoleQuery;
import com.ftx.authentication.rainshiro.utils.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName AuthController.java
 * @Description TODO
 * @createTime 2020年12月02日 16:09:00
 */
@RestController
@Api(tags = "角色权限管理接口")
public class AuthController {
    Logger log= LoggerFactory.getLogger(LoginsController.class);
    @Autowired
    ShiroDao shiroDao;

    @PostMapping("/getRoleList")
    @ApiOperation("获取角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo",value = "当前页",required = true),
            @ApiImplicitParam(name = "pageSize",value = "页大小",required = true),
            @ApiImplicitParam(name = "name",value = "角色名称",required = false),
            @ApiImplicitParam(name = "status",value = "角色状态",required = false)
    })
    public JsonObject<PageInfo<Map>> getRoleList(@RequestBody RoleQuery roleQuery){
        PageHelper.startPage(roleQuery.getPageNo(),roleQuery.getPageSize());
        List<Map> roleList = shiroDao.getRoleList(roleQuery);
        PageInfo<Map> mapPageInfo = new PageInfo<>(roleList);
        return new JsonObject<>(mapPageInfo, APPEnums.OK);
    }

    @PostMapping("/addRoleUser")
    @ApiOperation("新增角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "角色名称",required = true),
            @ApiImplicitParam(name = "status",value = "角色状态",required = true)
    })
    public JsonObject addRoleUser(@RequestBody RoleQuery roleQuery){
        String uuid = UuidUtil.getUuid();
        roleQuery.setId(uuid);
        roleQuery.setCreatetime(new Date());
        int i = shiroDao.addUser(roleQuery);
        return i>0?new JsonObject(APPEnums.OK):new JsonObject(APPEnums.ERROR);
    }

    @PostMapping("/updateRoleUser")
    @ApiOperation("修改角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "角色编号",required = true),
            @ApiImplicitParam(name = "name",value = "角色名称",required = true),
            @ApiImplicitParam(name = "status",value = "角色状态",required = true)
    })
    public JsonObject updateRoleUser(@RequestBody RoleQuery roleQuery){
        int i = shiroDao.updateUser(roleQuery);
        return i>0?new JsonObject(APPEnums.OK):new JsonObject(APPEnums.ERROR);
    }

    @GetMapping("/deleteRoleUser")
    @ApiOperation("删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "角色编号",required = true)
    })
    public JsonObject deleteRoleUser(String id){
        int i = shiroDao.deleteUser(id);
        return i>0?new JsonObject(APPEnums.OK):new JsonObject(APPEnums.ERROR);
    }

    @PostMapping("/getAuthUserList")
    @ApiOperation("获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo",value = "当前页",required = true),
            @ApiImplicitParam(name = "pageSize",value = "页大小",required = true),
            @ApiImplicitParam(name = "username",value = "用户名称",required = false),
            @ApiImplicitParam(name = "account",value = "账号",required = false)
    })
    public JsonObject<PageInfo<AuthUser>> getAuthUserList(@RequestBody AuthUser authUser){
        PageHelper.startPage(authUser.getPageNo(),authUser.getPageSize());
        List<AuthUser> authUserList = shiroDao.getAuthUserList(authUser);
        PageInfo<AuthUser> mapPageInfo = new PageInfo<>(authUserList);
        return new JsonObject<>(mapPageInfo, APPEnums.OK);
    }

}
