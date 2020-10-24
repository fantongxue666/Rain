package com.ftx.authentication.rainshiro.login;

import com.ftx.authentication.rainshiro.model.AuthUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    @Select(value = "select * from tb_user where account=#{account}")
    List<AuthUser> getUser(@Param("account")String account);

    @Select(value = "select url from tb_power where id in(select powerid from tb_role_power where roleid=(SELECT b.id from tb_user a left join tb_role b on a.roleid=b.id where a.account=#{account}")
    List<String> getRolesByUsername(@Param("account")String account);

    @Insert(value = "insert into tb_user values(#{id},#{account},#{username},#{pwd},'df8s90g78sdf90gsdf09g67')")
    int registerUser(AuthUser authUser);
}
