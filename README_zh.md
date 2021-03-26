# Android IoT App SDK Sample

[English](README.md) | [中文版](README_zh.md)

## 功能概述

IoT App SDK Sample（以下简称 Sample） 提供了设备配网，登录注册，资产管理等基本功能的示例。Sample 基于 Tuya OpenAPI 的 Restful API 接口实现相关功能. 涂鸦 IoT App SDK 是涂鸦 SaaS应用开发框架 产品的重要组成部分。

IoT App SDK Sample 实现了以下功能：

- 用户模块（登录、登出）
- 资产模块（资产查询、选择）
- 设备配网模块（AP、EZ、扫码模式）
- 设备模块 (设备查询、设备解绑）

## 快速开始

### 第一步：获取SHA1密钥

1. 将 Sample 工程下载到本地。

2. 针对 Sample 生成自己的 keyStore 文件，并记录文件密码。操作步骤如下：
	> **注意：**当前生成的文件的后缀是`.jks`而不是以前的`.keyStore`。
	1. 单击**Build**。
	![image.png](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/goat/20210326/3c802b06784d4a6fac9a7a744f35803d.png)
	2. 在下拉框中选择**Generate Signed APK**。
	3. 选择**APK**，然后按照提示创建 keyStore 文件。

3. 在同一目录下执行命令，以过滤出生成的`.jks`文件。

	```java
	keytool -list -v -keystore  xx.jks
	输入密码
	```

4. 获取文件中的 SHA1 密钥。

	```java
	Valid from Tue Mar 16 10:22:14 CST 2021 to Sun Mar 06 10:22:14 CST 2061
	Certificate fingerprint:
	MD5: 6B:8C:94:15:35:7C:2E:E8:6E:76:7F:8C:F9:4B:05:BC
	SHA1: 8F:AC:5D:50:65:22:C2:2A:E4:96:3D:8F:9E:DC:5B:43:11:49:55:B1
	SHA256: 0B:98:89:D8:D5:FC:B1:23:9A:76:B1:2B:8F:4C:5E:24:BF:E7:60:E8:FF:EF:E9:40: 48:14:60:D3:62:00:5C:5F
	Signature algorithm name: SHA256withRSA
	Subject public key algorithm: 2048-bit RSA key
	Version: 3
	```

5. 接下来获取程序包名称（如下所示：您还可以自定义程序包名称）。

	<img src="https://images.tuyacn.com/app/iotappsample/applicationId.png" style="zoom:50%;" />

### 第二步：配置云开发项目

1. 登录涂鸦智能 [IoT 平台](https://developer.wgine.com/cloud/)，并完成实名认证。

2. 创建**行业项目**类型项目。
	2. 单击左侧导航栏中的**云开发**。
	3. 在**云开发**>**项目**页面，点击**创建项目**。
		![创建项目](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/goat/20210323/90d1c8edd87747c48e46c7d056818c14.png)
	4. 在**创建项目**页面，配置**项目名称**、**项目类型**、**项目描述**和**服务行业**信息。
	> **说明：** **行业类型**选择**行业项目**。即基于资产、用户体系，构建任何行业场景的物联网 SaaS 项目。在行业项目下还能创建多个应用，共享同样资产和用户资源。

	5. 单击**确定**，完成项目创建。

3. 创建应用。
	1. 在**项目管理**>**我的项目**区域，单击目标项目。
	2. 在顶部导航栏，单击**应用**。
	![image.png](https://images.tuyacn.com/fe-static/docs/img/a39f6787-7fc5-49ac-bf3d-0b3077c20499.png)
	3. 在**添加应用**窗口，配置应用信息。选择 Android 应用类型。
	![image.png](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/goat/20210325/33b590534f5c42c48d0b5bc3fe68560e.png)
	
4. 创建用户。
	1. 在**项目管理**>**我的项目**区域，单击目标项目。
	2. 在顶部导航栏，单击**用户**。
		![image.png](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/goat/20210325/ebb6b2caaa9247bc9a7f99afd319dc7f.png)
	3. 单击**新建用户**。
	4. 在**新增用户**窗口，输入用户账号和密码，单击**确定**。


5. 创建资产。
	1. 在**项目管理**>**我的项目**区域，单击目标项目。
	2. 在顶部导航栏，单击**资产**。
		![image.png](https://images.tuyacn.com/fe-static/docs/img/345ed640-73eb-48ed-a9ab-c38a98a97ff4.png)
	3. 单击**新建资产**。
	4. 在**新建资产**窗口，输入资产名称，单击**确定**。
	
6. 资产授权用户。
	1. 在**项目管理**>**我的项目**区域，单击目标项目。
	2. 在顶部导航栏，单击**资产**。
		![image.png](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/goat/20210325/b31ee09d2fc34968b37da970bf2e3e7c.png)
	3. 单击目标项目**操作**列下的**管理**。
	4. 在**授权用户**页签，单击**新增授权**。
	5. 在**新增授权用户**窗口，添加待授权账号信息，单击**确定**。


### 第三步：工程项目配置（Secret Key, Client ID 等）

1. 在**应用**>**App 应用** 界面获取已创建 Andriod 应用的 Client ID 和 Client Secret。
	<img src="https://images.tuyacn.com/fe-static/docs/img/693f7507-2e94-4acf-9499-f86f89dce2fc.png" style="zoom:50%;" />

2. 粘贴到 Sample 代码对应位置，如下图所示。
	<img src="https://images.tuyacn.com/fe-static/docs/img/7d117bd1-6748-4932-b01c-f44b2a3789ec.png" style="zoom:50%;" />

## Sample 功能展示

###  账户登录
<img src="https://images.tuyacn.com/app/iotappsample/login.png" width="35%" /> 
<img src="https://images.tuyacn.com/app/iotappsample/main.png" width="35%" />

### 资产选择
<img src="https://images.tuyacn.com/app/iotappsample/assets_list.png" width="35%" /> 
<img src="https://images.tuyacn.com/app/iotappsample/asset_choose.png" width="35%" />

### 配网操作

- AP 配网

	<img src="https://images.tuyacn.com/app/iotappsample/ap.png" width="35%" />

- EZ 配网

	<img src="https://images.tuyacn.com/app/iotappsample/ez.png" width="35%" />

- 二维码扫码配网

	<img src="https://images.tuyacn.com/app/iotappsample/qr.png" width="35%" />
	<img src="https://images.tuyacn.com/app/iotappsample/qr_token.png" width="35%" />
<img src="https://images.tuyacn.com/fe-static/docs/img/974c0be9-0535-4a92-8475-7e8f88e34b53.png" width="35%" />

- 配网结果页

	<img src="https://images.tuyacn.com/app/iotappsample/loading.png" width="35%" /> 
	<img src="https://images.tuyacn.com/app/iotappsample/cofig_result.png" width="35%" />

### 设备查询解绑

<img src="https://images.tuyacn.com/app/iotappsample/devices_list.png" width="35%" /> 
<img src="https://images.tuyacn.com/app/iotappsample/unbind_device.png" width="35%" />

