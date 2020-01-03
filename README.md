# MeetHere-backend:sparkles:
<div align="center"><img src="https://tva1.sinaimg.cn/large/006tNbRwly1gaiix846njj305k05kaa0.jpg"></img></div>
## 项目地址

- 部署地址:rocket:

    http://152.136.173.30/

- 后端项目地址:rocket:

    https://github.com/HavenTong/MeetHere-backend

- 前端项目地址:rocket:

    https://github.com/JJAYCHENooo/MeetHere-frontend



## 小组成员

- 童翰文 10175101152

- 陈俊杰 10175101148

- 项慕凡 10175101151

- 徐滔锴 10175101147

    

## 需求分析

MeetHere是一个场馆预约与管理的Web电子商务网站，主要功能包括：

- 普通用户：

    注册、登录、个人信息管理、查看场馆介绍和预约信息、场馆预约、场馆预约订单管理、查看新闻、留言管理（发布、浏览、删除、修改）

- 管理员

    用户管理、场馆信息管理（场馆介绍、场馆位置、场馆租金、场馆空闲时间...）、预约订单审核、预约订单统计（按照场馆、时间等对预约订单进行统计）、新闻动态管理（发布、增、删、改）、留言审核

- 场馆预约订单

    已预定场馆信息的查看、取消、信息修改

    

## 系统部署说明

系统运行地址：

http://152.136.173.30/

系统部署在我们购买的腾讯服务器中，其中前端使用 nginx 将我们使用 vue 开发的项目进行部署，后端程序是 java 程序，使用 nohup 使其在系统中不挂断得运行。

详细部署信息如下：

![](https://s2.ax1x.com/2020/01/02/lNuvqO.png)



## 后端技术栈

- IDE

    IDEA IntelliJ 2019.3

- DataBase
    - MySQL 8.0.18
    - redis 5.0.6
- Framework & Some Dependency
    - Spring Boot 2.2.1
    - Spring Secruity 
    - Spring Data
    - MyBatis
    - Java Mail
    - ...
- JWT for authentication



## 前端技术栈

- IDE

    VS Code 1.41

- Framework & Dependency

    - Vue.js
    - Vuex
    - Vue Router
    - Element-UI
    - Echats
    - vue-star
    - ...

    

## 单元测试 & 集成测试

- JUnit 5
- Mockito 3.x
- JMockit 1.48

- 测试结果

    ![image-20200102220426215](https://tva1.sinaimg.cn/large/006tNbRwly1gaikh0wln2j327w0lijvp.jpg)

## 系统测试

- Selenium IDE进行录制回放
- 通过Python脚本自动化生成系统测试报告



## 性能测试

- JMeter 5.2.1



## 覆盖度报告

使用IDEA的覆盖度报告工具，生成覆盖度报告

覆盖度报告见 **覆盖度报告/index.html**



## 静态代码分析报告

使用P3C进行静态代码风格及规约检测，生成静态代码分析报告

静态代码分析报告

- **静态代码分析报告/previous**/**index.html** 表示之前的静态代码分析
- **静态代码分析报告/current/index.html** 表示改进后的静态代码分析



## 人员主要分工

|  成员  |                       分工                       |
| :----: | :----------------------------------------------: |
| 童翰文 |      后端开发，单元测试，集成测试，系统测试      |
| 陈俊杰 | 前端开发，单元测试，集成测试，系统测试，性能测试 |
| 项慕凡 | 后端开发，单元测试，集成测试，系统测试，性能测试 |
| 徐滔锴 |           前端开发，系统测试，性能测试           |



## 项目文件说明