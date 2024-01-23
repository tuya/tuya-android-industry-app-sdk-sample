The `UserService` class provides the API method to enable login, logout, and login session listening.

## Login

```kotlin
UserService.loginWithParams(String, String, String, IndustryValueCallBack<IUser>)
```

This method is used for user login and returns the login result through a callback.

**Parameter description**

| Parameter | Type | Required | Description |
| ------ | ---- | -------- | ---- |
| projectCode | String | Yes | The project code. |
| userName | String | Yes | The username. |
| password | String | Yes | The password. |
| callback | IndustryValueCallBack\<IUser> | Yes | The callback. |

**Example**

```java
UserService.loginWithParams("projectCode", "userName", "password", new IndustryValueCallBack<IUser>() {
    @Override
    public void onSuccess(IUser iUser) {
        Toast.makeText(v.getContext(), "login success : " + s, Toast.LENGTH_SHORT).show();
        
    }

    @Override
    public void onError(int i, String s) {
        Toast.makeText(v.getContext(), "login fail : " + s, Toast.LENGTH_SHORT).show();
    }
});
```


## Logout

This method is used to implement user logout.

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| callBack | IndustryCallBack | Yes | The callback object for handling the logout result. |

**Example**

```
UserService.logout(new IndustryCallBack() {
    @Override
    public void onSuccess() {
        Toast.makeText(v.getContext(), "logout success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(int i, String s) {
        Toast.makeText(v.getContext(), "logout fail", Toast.LENGTH_SHORT).show();
    }
});
```

## Query user information

**Parameter description**

| Parameter | Type | Required | Description |
|-------|------|----------|------|
| callback | IndustryValueCallBack<UserInfo> | Yes | The callback. |

**Example**

```java
UserService.fetchUserInfo(new IndustryValueCallBack<UserInfo>() {
    @Override
    public void onSuccess(UserInfo userInfo) {
        // Success, processing user information.
        // ...
    }

    @Override
    public void onError(int errorCode, String errorMessage) {
        // Failure, processing error message.
        // ...
    }
});
```

## Check whether the user is logged in

**Example**

```java
boolean isLogin = UserService.isLogin();
        if (isLogin) {
            // Logic processing in login status.
        }

```

## Register a listener for login session expiration
After you register a listener for login session expiration, this callback will be triggered when the user changes their password or remains inactive for a period. A successful callback indicates that the login has expired and prompts the user to log in again.

**Example**

```java
UserService.setLoginExpiredListener(new LoginExpiredListener() {
    @Override
    public void onLoginExpired() {
        // Handle the logic of session expiration.
    }
});
```
