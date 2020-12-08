<h1>Rain</h1>
<p>Rain平台更新日志~</p>
<p>
    <a href="https://img.shields.io/badge/version-V1.0.1-brightgreen">
        <img src="https://img.shields.io/badge/version-V1.0.1-brightgreen" alt="V1.0.1" />
    </a>
    <a href="https://img.shields.io/badge/author-ftx-orange">
        <img src="https://img.shields.io/badge/author-ftx-orange" alt="ftx">
    </a>
</p>
<table>
    <thead>
        <tr>
            <th>模块名称</th>
            <th>内容</th>
            <th>版本说明</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>RAIN-SHIRO</td>
            <td>Rain平台安全模块，登录和认证授权，Token校验</td>
            <td>
                1，构建shiro模块，初始化shiro模块工程，并集成token，实用redis存储token<br>
                2，集成log4j2日志框架，shiro过滤器，token工具类<br>
                3，shiro模块增加MD5算法加密<br>
                4，rain平台集成swagger接口文档<br>
                5，已支持多用户多角色，权限动态分配
            </td>
        </tr>
        <tr>
            <td>RAIN-EXCEL</td>
            <td>Rain平台Excel导出模块，配置xls/xlsx模板和装载sql的xml文件，快速导出excel</td>
            <td>此版本暂不支持类型包括：<br>多sheet页，xls版本，导出查询列表只能全是字符串格式，该模块待后期完善</td>
        </tr>
        <tr>
            <td>RAIN-AUTOCODE</td>
            <td>集成了开发者工具-代码自动生成</td>
            <td>该模块暂时无法和rain平台进行集成，可作为单应用，代码自动生成已内置到了Rain平台中</td>
        </tr>
        <tr>
            <td>RAIN-LICENSE</td><td>服务器证书解析和校验模块 [单应用]</td>
            <td>已集成</td>
        </tr>
        <tr>
             <td>RAIN-FILESERVER</td><td>文件通用上传下载接口</td>
             <td>文件上传接口<br>文件下载接口<br>图片在线预览接口（可直接拼装http请求地址放入<img src=""/>的src中进行图片的预览）
             </td>
                </tr>
    </tbody>
</table>
<a href="https://github.com/fantongxue666/rain-vue-elementui-admin">附属一：点我进入Rain平台的前端Vue-Cli脚手架项目地址</a>
<br><a href="">附属二：vue-elementui-admin框架介绍- [作者: 阳哥]</a>






















