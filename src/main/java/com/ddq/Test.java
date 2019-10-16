package com.ddq;

import dao.UserDao;

public class Test {

    public static void main (String[] atgs){
        ApplicationContext context = new MyApplicationContext("spring02"); //此处使用了多态，接口实现类对象的引用赋值给接口的引用
        UserDao userDao = (UserDao) context.getBean("userDao");  // 此处使用了多态中的向下转型，并且利用到了，多态中，调用方法时，调用的是子类的方法
        userDao.userDao();
    }
}
