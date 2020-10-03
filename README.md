# foodie
B2C电商从 0 - 1 再到 100。

sql执行文件：`foodie-shop-dev.sql`。  
项目演示url：http://47.99.55.188:8080/foodie-shop  
swagger文档：http://47.99.55.188:8088/foodie-api/doc.html
  
编译运行之前需要先确认一下几点：
1. `application.yml`中的`spring:profiles:active: prod`是否已经改成dev
2. `/home/possewang/atm/picture/` 这个路径需要修改，代表着你上传用户头像的存储路径。此处是使用了静态资源映射，具体操作在`WebMvcConfig.java`类中配置。
3. 因为是将下面打成 war 包运行到外置 tomcat 中的，所以本地运行需要添加外置 tomcat。如需使用 jar 方式启动，可以全局搜索`打war包`，并将相应的配置删掉。
4. 日志文件需要将此`log4j.appender.file.File`配置参数的路径修改为自己的。
5. 支付接口方面因为麻烦所以就没做了。下订单之后支付页面可以使用`/orders/notifyMerchantOrderPaid`这个接口去一键修改订单状态为已支付。

## 单体架构设计项目
- [x] SpringBoot 整合 HikariCP 数据源
- [x] HikariCP 数据源整合 Mybatis
- [x] 整合 swagger2 生成接口文档
- [x] 设置跨域配置实现前后端联调
- [x] 整合 Log4j 打印日志
- [x] SpringBoot 配置切面监控 Service 执行时间
### 1. 用户模块
- [x] 用户注册
- [x] 用户登录
- [x] 退出登录
### 2. 首页
- [x] 轮播图
- [x] 首页分类
- [x] 商品推荐
### 3. 商品页
- [x] 商品主图
- [x] 商品规格
- [x] 商品参数
- [x] 商品详情
- [x] 商品评价总量
- [x] 商品评价
### 4. 商品搜索
- [x] 按关键词搜索
- [x] 按三级分类搜索
- [x] 默认排序
- [x] 销量排序
- [x] 价格优先
### 5. 购物车
- [x] 加入购物车
- [x] 从购物车删除
- [x] 刷新购物车
- [x] 收货地址
### 6. 订单支付
- [x] 确认订单
- [x] 创建订单
- [ ] 微信支付功能集成(这个目前没有企业资质暂时不管)
- [ ] 支付宝支付功能集成(这个目前没有企业资质暂时不管)
- [ ] 定时关闭未支付订单
### 7. 用户中心
- [x] 查询个人信息
- [x] 更新个人信息
- [x] 上传头像
- [x] 订单管理
- [x] 评价管理
- [x] 个人中心订单状态数量


# 接入应用监控Skywalking
操作说明
https://www.yuque.com/docs/share/eab56147-3475-48ef-ad3a-21d1d87c07ad?#