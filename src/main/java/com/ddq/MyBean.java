package com.ddq;

public class MyBean {
    /*
        javaBean，JavaBean本质上是一个对象类，它是一种符合JavaBean API规范的特殊的类。
        JavaBan相比其他java类而言，独一无二的特征有：
        （1）包含一个默认的无参构造函数
        （2）需要被序列化并且实现了 Serializable 接口。
        （3）针对JavaBean中的成员变量，对应有setXXX（）方法和getXXX（）方法， 通过setXXX（）方法设置属性,通过getXXX（）方法获取属性。

        该JavaBean是 模拟spring 容器中，读取spring 配置文件（xml文件），根据xml文件中的配置信息，创建JavaBean时使用的JavaBean定义类
        MyBean并不是我们最终要创建的类对象，MyBean只是我们为了利用Spring框架来替我们创建我们想要创建的类对象时使用的中间件，
        该中间件的作用是，用来描述我们最终所要创建的类对象

     */

    private String id; // xml中对应的id 属性
    private String clazz; // xml中对应的 class 属性

    public String getClazz(){
        return clazz;

    }

    public void setClazz(String clazz){
        this.clazz = clazz;
    }

    public String gertId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }


}
