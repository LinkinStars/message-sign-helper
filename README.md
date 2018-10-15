# 通信消息签名封装

## 使用场景
用于给与第三方开放的接口时进行校验，防止信息在传输过程中被篡改。

## 签名规则
1. 如果参数的值为空不参与签名；  
2. 参数名区分大小写；
3. key为自定义的私钥，可以为任何形式的字符，通信双方需要约定一致；

## 签名步骤
1. 将参数名ASCII码从小到大排序（字典序）；
2. 使用‘&’和‘=’进行拼接
3. 拼接最后的自定义的key
4. 使用大写32位md5获得签名

## 签名举例：  
一、需要传递的参数如下：   
"name" : "xxx"  
"age" : "2"  
"username" : "admin"  
"test" : ""  

二、经过1.2两步之后得到的字符串为： age=2&name=xxx&username=admin  

三、经过第三步：
age=2&name=xxx&username=admin&key=XXX

四、经过第四部：
sign = MD5(age=2&name=xxx&username=admin&key=XXX)

五、最后传输的json为：

````json
{   
    "test":"",
    "name":"xxx",
    "sign":"C96E0D49DEE5550716783458C3A98F24",
    "age":"2",
    "username":"admin"
}
````

验签为一样的过程，不再赘述

## 使用方式说明
请求发起方：  
通过调用sign方法，传入Map格式的数据和key，得到请求的json  
发送json给接收方  

请求的接收方：  
通过调用inspect方法，传入收到的json数据和key，返回签名是否正确  
如果正确则证明数据没有被篡改

## 使用案例

```java

    String paramKey = "XXX";
    Map<String, String> map = new HashMap<>();
    map.put("username", "admin");
    map.put("name", "xxx");
    map.put("age", "2");
    map.put("test", null);
    
    JSONObject jsonObject = SignMessageHelper.sign(map, paramKey);
    System.out.println(jsonObject);
    
    boolean result = SignMessageHelper.inspect(jsonObject, paramKey);
    System.out.println(result);

````


