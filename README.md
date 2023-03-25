# foodie
B2C电商后端单体项目实现。

## 环境
JDK:1.8  
MySQL:5.6
Redis:6.0  

## 启动
- 1、运行 MySQL。sql执行文件：`foodie-shop-dev.sql`。
- 2、运行 Redis 
- 3、SpringBoot Jar包方式启动

swagger文档：
http://localhost:8088/doc.html

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
- [x] 创建订单
- [x] 确认订单
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