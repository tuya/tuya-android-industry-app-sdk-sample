# Tuya IoT Platform Configuration Guide

## Tuya IoT Platform 

### Configure Cloud Development Project

1. Log in to the Tuya Smart [IoT Platform](https://iot.tuya.com/cloud/?_source=github) and verify your account with enterprise authentication.

2. Create **Industry Solutions** type project.
	1. In the **Projects** page, click **Create**.
	![Create Project](https://images.tuyacn.com/app/iotappsample/en/cr_product_new.png)
	2. In the **Create Project** page, configure **Project Name**, **Project Type**, **Description** and **Industry**.
	
		>**Description:** Select **Industry Solutions** in the **Project Type** . That is, based on assets and user systems, build IoT SaaS projects in any industry scenario. Multiple applications can be created under industry projects to share the same assets and user resources.
		
	3. Click **Create** to complete the project creation.

3. Go to **Projects** > **My Project**, and click the created project to view details. Click **Applications** > **Cloud** to get the **Access ID** and **Access Secret**.

	![image.png](https://images.tuyacn.com/app/Hanh/cloudapplication1.png)

4. Create an app application.
	1. In the **Projects**>**My Project** area, click the target project.
	2. In the top navigation bar, click **Applications** > **App** > **Add Application**.
		![image.png](https://images.tuyacn.com/app/iotappsample/en/cr_app_new.png)
	3. In the **Add Application** window, Select the **Tuya Device Manager (Android)** type.
	![image.png](https://images.tuyacn.com/app/Hanh/addappapplication.png)
   - SHA1 & Application id will be filled automatically.
		
5. Create user.
	1. In the **Projects**>**My Project** area, click the target project.
	2. In the top navigation bar, click **Users**.
	![image.png](https://images.tuyacn.com/app/iotappsample/en/cr_user_new.png)
	3. Click **Add User**.
	4. In the **Add User** window, select region, for example "China", enter the user account and password, and click **OK**.


6. Create assets.
	1. In the **Projects**>**My Project** area, click the target project.
	2. In the top navigation bar, click **Assets**.
	![image.png](https://images.tuyacn.com/app/iotappsample/en/addAsset.png)
	3. Click **New Asset**.
	4. In the **New Asset** window, enter the asset name and click **OK**.

7. Asset authorized users.
	1. In the **Projects**>**My Project** area, click the target project.
	2. In the top navigation bar, click **Assets**.
	![image.png](https://images.tuyacn.com/app/iotappsample/en/cr_auth_new.png)
	3. Click **Manage** in the **Action** column of the target project.
	4. On the **Authorized User** tab, click **Add Authorization**.
	5. In the **Add Authorized User** window, add the account information to be authorized, and click **OK**.
	
8. API Products Subscription

   **1.** Go to **Projects** > **API Products** > **All Products**, click **Industry Project**, and subscribe to your desired API products.
	![image.png](https://images.tuyacn.com/app/Hanh/APIproducts.png)
	
	**You need to subscribe these API Products for [Tuya Home Assistant](https://github.com/tuya/tuya-home-assistant) and [Tuya Home Bridge](https://github.com/tuya/tuya-homebridge) integrations**
	
	![image.png](https://images.tuyacn.com/app/Hanh/openapiproducts.png)
	
	**2.** Go to **Projects** > **API Products** > **Subscribed Products**. Click one of the API products to subscribe.
	
	![image.png](https://images.tuyacn.com/app/Hanh/buyapi.png)
	
	**3.** click **Project** > **New Authorization** to authorize your project to use this API.
	![image.png](https://images.tuyacn.com/app/Hanh/tip.png)
	![image.png](https://images.tuyacn.com/app/Hanh/newauthorization.png)
	![image.png](https://images.tuyacn.com/app/Hanh/apiproductauthorization.png)

## Tuya Device Manager App (Android)

Tuya Device Manager App provides examples of basic functions such as device network configuration, login and registration, and asset management. Sample is based on the Restful API interface of [Tuya Open API](https://developer.tuya.com/en/docs/cloud/) to implement related functions. Tuya IoT App SDK is an important part of the Tuya SaaS Development Framework product series.

Tuya Device Manager App has the following functions:

- User module (login, logout)
- Asset module (asset query, selection)
- Equipment distribution module (AP, EZ, scan qr code mode)
- Equipment module (equipment query, equipment unbinding)

### Step 1: Install the App 

[Tuya Device Manager App](https://github.com/tuya/tuya-android-iot-app-sdk-sample/releases)

### Step 2: Configure the App

1. Scan the QR Code in [Tuya IoT Platform Cloud Project](https://iot.tuya.com/cloud/) to configure the Client ID and Client Secret in the **Applications** > **App** as shown below：

<img src="https://images.tuyacn.com/app/Hanh/applicationAPP1.png" width="70%"/>

### Scan QR Code Page
<img src="https://images.tuyacn.com/app/Hanh/scan.jpg" width="30%" />

### Step 3: Account Login
1. Choose the region where we create our account, for example "China".
2. In the [Tuya IoT Platform]((https://iot.tuya.com/cloud/)) >**Projects**>**Users**, get the username and password.
	
	<img src="https://images.tuyacn.com/app/Hanh/login.jpg" width="30%" />
	
3. Home Page.
	
	<img src="https://images.tuyacn.com/app/Hanh/devicemanager.jpg"  width="30%" />
	
### Step 4: Device Pairing

- EZ Mode

	<img src="https://images.tuyacn.com/app/Hanh/ez.jpg" width="30%" />

- AP Mode

	<img src="https://images.tuyacn.com/app/Hanh/ap.jpg" width="30%" />
	
- QR Code

	<img src="https://images.tuyacn.com/app/Hanh/qr.jpg" width="30%" />
	
### Step 5: Pair Success

- Pair Device Successfully

	<img src="https://images.tuyacn.com/app/Hanh/devicelist1.jpg" width="30%" />

- Device List

	<img src="https://images.tuyacn.com/app/Hanh/devicelist.jpg" width="30%" />
	
	
