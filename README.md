# GPSd
A GPSd client with Java
# Warning:You should use the module with Socket(https://github.com/tianwei0828/Socket.git)!!!
## 一、初始化
```java
  client = new GPSdClient(ip, port);
  client.retryCount(retryCount);
  client.retryInterval(retryInterval);
  client.connectTimeout(connectTimeout);
  client.readTimeout(readTimeout);
  client.isDebug(isDebug);
  client.logPath(logPath);
  client.logName(logName);
  client.isCover(isCover);
  //设置JsonParser
  client.setJsonParser(new JsonParser());
  //添加JsonResultCallback
  client.addJsonResultCallback(new JsonResultCallback() {

            @Override
            public void onTPV(TPV tpv) {
               //收到TPV
            }


            @Override
            public void onGPSdException(GPSdException e) {
                //异常信息
            }

            @Override
            public void onStatusChanged(int status, String message) {
                //状态变化
            }
    });
    //设置NMEAParser
    client.setNMEAParser(new NMEAParser());
    //添加NMEAResultCallback
    client.addNMEAResultCallback(new NMEAResultCallback() {

            @Override
            public void onGGASentence(GGASentence ggaSentence) {
                //GGASentence
            }

            @Override
            public void onRMCSentence(RMCSentence rmcSentence) {
                //RMCSentence
            }

            @Override
            public void onStatusChanged(int status, String message) {
                //状态变化
            }
     });
     //添加原始数据的监听
     client.addRawDataResultCallback(new RawDataResultCallback() {
            @Override
            public void onRawData(String rawData) {
                //原始数据
            }

            @Override
            public void onStatusChanged(int status, String message) {
                //状态改变
            }
     });
```
## 二、连接
```java
client.connect();
```
## 三、发送指令
```java
  Watch watch = new Watch();
  //开启数据接收
  watch.enable = true;
  //启用nmea格式数据
  watch.nmea = true;
  //启用json格式数据
  watch.json = true;
  client.newRequest(watch).send();
```
## 四、关闭
```java
client.close();
```
