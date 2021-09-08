package com.ftx.authentication.rainshiro.login;

import com.ftx.authentication.rainshiro.model.AuthUser;
import com.ftx.authentication.rainshiro.model.Role;
import com.ftx.authentication.rainshiro.model.RoleQuery;
import com.ftx.authentication.rainshiro.model.TreeNode;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName TestDao.java
 * @Description TODO
 * @createTime 2020年10月19日 16:36:00
 */
@Repository
@Mapper
public interface ShiroDao {

    /**
     * 查询用户列表
     * @param roleid
     * @return
     */
    @Select({"<script>",
            "select a.*,b.rolename from tb_user a left join tb_role b on a.roleid=b.id where 1=1",
            "<if test='username!=null and username!=\"\"'>",
            "and a.username like concat('%',#{username},'%')",
            "</if>",
            "<if test='account!=null and account!=\"\"'>",
            "and a.account=#{account}",
            "</if>",
            "</script>"})
    List<AuthUser> getAuthUserList(AuthUser authUser);

    @Delete(value = "delete from tb_role_power where roleid=#{roleid}")
    int delRole(String roleid);

    @Insert(value = "insert into tb_role_power values(#{id},#{roleid},#{powerid})")
    int insertRole(Role role);

    @Select(value = "select * from tb_power")
    List<TreeNode> getRole(String roleid);

    @Select(value = "select a.powerid from tb_role_power a,tb_power b where a.powerid=b.id and b.parentid!=0 and roleid=#{roleid}")
    List<Integer> getPowerIdsByAccount(String roleid);

    /**
     * 删除角色
     */
    @Update(value = "update tb_role set yxbz='n' where id=#{id}")
    int deleteUser(String id);

    /**
     * 修改角色
     */
    @Update(value = "update tb_role set rolename=#{name},wlbz=#{status} where id=#{id}")
    int updateUser(RoleQuery roleQuery);

    /**
     * 新增角色
     */
    @Insert(value = "insert into tb_role values(#{id},#{name},#{status},'y',#{createtime})")
    int addUser(RoleQuery roleQuery);

    /**
     * 查询角色列表
     * @param roleQuery
     * @return
     */
    @Select({"<script>",
            "select * from tb_role where yxbz='y'",
            "<if test='name!=null and name != \"\"'>",
                "and rolename like concat('%',#{name},'%')",
            "</if>",
            "<if test='status!=null and status != \"\"'>",
            "and wlbz = #{status}",
            "</if>",
            "order by createtime desc",
            "</script>"})
    List<Map> getRoleList(RoleQuery roleQuery);

    @Select(value = "select * from tb_power where id in(select powerid from tb_role_power where roleid=(SELECT b.id from tb_user a left join tb_role b on a.roleid=b.id where b.wlbz='1' and b.yxbz='y' and a.account=#{account}))")
    List<TreeNode> getTreeListByAccount(@Param("account")String account);

    @Select(value = "select * from tb_user where account=#{account}")
    List<AuthUser> getUser(@Param("account")String account);

    @Select(value = "select a.*,b.rolename from tb_user a left join tb_role b on a.roleid=b.id where b.yxbz='y' and a.account=#{account}")
    List<AuthUser> getUserInfo(@Param("account")String account);

    @Select(value = "select url from tb_power where id in(select powerid from tb_role_power where roleid=(SELECT b.id from tb_user a left join tb_role b on a.roleid=b.id where b.wlbz='1' and b.yxbz='y' and a.account=#{account}))")
    List<String> getRolesByUsername(@Param("account")String account);

    @Insert(value = "insert into tb_user values(#{id},#{account},#{username},#{pwd},'h5ui123o45h324io5h23p4')")
    int registerUser(AuthUser authUser);

    @Select(value = "SELECT url from tb_power where id in (select powerid from tb_role_power where roleid=(SELECT roleid from tb_user WHERE account=#{account}))")
    List<String> getPowersByAccount(@Param("account")String account);

}
