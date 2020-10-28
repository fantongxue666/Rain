# Rain平台更新日志

<table><thead><tr><th>模块名称</th><th>内容</th><th>版本说明</th></tr></thead><tbody><tr><td>RAIN-SHIRO</td><td>Rain平台安全模块，登录和认证授权，Token校验</td><td>无</td></tr><tr><td>RAIN-EXCEL</td><td>Rain平台Excel导出模块，配置xls/xlsx模板和装载sql的xml文件，快速导出excel</td><td>此版本暂不支持类型包括：多sheet页，xls版本，导出查询列表只能全是字符串格式，该模块待后期完善</td></tr></tbody></table>

## 2020-10-19

构建shiro模块，初始化shiro模块工程，并集成token，实用redis存储token

## 2020-10-20

集成log4j2日志框架，shiro过滤器，token工具类

## 2020-10-23

shiro模块完成，并集成到了Rain平台

## 2020-10-24

shiro模块增加MD5算法加密，新增RAIN-EXCEL模块

## 2020-10-27

导出excel模块封装完毕，下一步准备集成

## 2020-10-28

rain-excel模块集成到rain平台，rain平台集成swagger接口文档