package com.ddq;
import com.ddq.ApplicationContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyApplicationContext implements ApplicationContext {

    /**
     * 1.读取xml配置文件
     * 2.生成Bean实例
     * 3.取出bean
     * @param id
     * @return
     */

    private String path; // 读取xml文件时使用的xml文件地址（spring配置文件地址）
    private List<MyBean> beanList = new ArrayList<MyBean>(); // 读取xml文件时，存放创建的JavaBean对象
    private Map<String,Object> beanMap = new HashMap<String , Object>();
    // 读取xml文件，创建了JavaBean对象后，结合JavaBean对象的id属性，存放JavaBean

    public MyApplicationContext(String path){  // 注意构造函数没有返回类型，如果加上返回类型，这个方法就是一个普通的成员方法了，就不是构造函数了
        this.path = path;
        parseXml(path);
        creatBeans();

     }

     // 读取xml配置文件，并根据配置文件中内容，创建JavaBean对象，并存入list容器中
    private void parseXml(String path){
        SAXReader reader = new SAXReader(); //创建xml解析器对象
        URL  url = this.getClass().getClassLoader().getResource(path);
        // 注意此处的入参path的写法，由于我们在IDEA项目中已经配置了项目的资源包路径：file:/D:/workSpace/springhelloworld02/src/resources
        // 也配置了项目源路径：file:/D:/workSpace/springhelloworld02/src/main、java，
        // 所以，项目中涉及到这些路径的，就不用再重复写了，IDEA可以自动去相应的包下寻找，
        // 所以，上述代码中，getResource(path)中的入参path的值只要是“spring02”就可以了。
        /*
         此处使用了反射，this.getClass()：获取到MyAPPlicatioContext类的Class类对象
         .getClassLoader()：通过Class类对象获取到类加载器（即加载这个MyAPPlicatioContext类的Class类对象的那个加载器）
         .getResource(path)：通过类加载器获取到给定参数指向的文件的路径，
         但是有一点很重要的地方需要注意，就是getResource(path)方法是java中根据相对目录或者绝对目录获文件的方法，
         该方法通常是有两种使用格式（Class类和ClassLoader类，两个类分别 都有getResource(path)方法，）：
         （1）classLoader.getResource(path)：
            接收一个表示路径的参数，返回一个URL类型的对象，该URL对象表示path指向的资源（文件）
            注意：该处的入参path，只能是一个相对路径不能是绝对路径，并且接收的相对路径是相对于项目的包的根目录来说的相对路劲。
         （2）Class.getResource(path):
            该方法也是接收一个表示路径的参数，返回一个URL对象，该URL对象表示path指向的资源（文件）
            该方法中，入参path可以是相对路径也可以是绝对路径，其中，如果是相对路径，则是相对于该Class类来说的相对路径
            如果是绝对路径，则是相对于项目的包的根目录来说的相对路径，此时，“/”表示项目路径，而不是表示磁盘的跟目录
          */
        File filePath = new File(url.getPath());
        // 获取到要读取的xml配置文件的地址并转化为File类型的值，url.getpath()：获取到此url的路径部分，返回值类型为String类型
        System.out.println("url的值为：" + url);
        System.out.println("url.getPath()的值为：" + url.getPath());
        System.out.println("filePath的值为：" + filePath);

        if (path != null && path != "") {
            try{
                Document document = reader.read(filePath);
                List<Element> list = document.selectNodes("//beans/bean");
                /*
                   SAXReader为xml文件解析器，Document为xml文件解析后的根节点对象（Document）
                   Element为xml文件解析得到的节点对象
                   document.selectNodes()：方法为dom4j中XmlDocument或者XmlNode的一个方法（XPath为XML的内容），
                   selectNode用XPath来选取满足给定入参所指定的节点。
                   以上程序中，入参表示：从任意位置的节点上选择名称为 beans的节点，并在每个beans节点下选取出所有名称为bean的节点

                   SelectNodes("/item")
                        从根节点的儿子节点中选择名称为 item 的节点。
                   SelectNodes("//item")
                        从任意位置的节点上选择名称为 item 的节点。要重点突出这个任意位置，它不受当前节点的影响，
                        也就是说假如当前节点是在第 100 层（有点夸张），也可以选择第一层的名称为 item 的节点。
                   注：selectNodes（）方法的入参是一个字符串
                */

                for (Element element:list){  // 增强for
                    MyBean myBean = new MyBean();
                    myBean.setId(element.attributeValue("id"));
                    myBean.setClazz(element.attributeValue("class"));
                    beanList.add(myBean);
                }

            }catch(DocumentException e){
                e.printStackTrace();
            }


        }else {
            System.out.println("文件不存在");
        }
    }

    /*
        生成相应的类对象，并结合类名进行存储
     */
    private void creatBeans(){
        if(beanList != null && beanList.size() > 0){
            for (MyBean myBean:beanList){
                try{
                    beanMap.put(myBean.gertId(),Class.forName(myBean.getClazz()).newInstance());
                    // 此处利用反射技术生成我们想要生成的类对象
                    //  并且此处还利用到了多态，将子类对象赋值给父类对象的引用，
                    // 因此，存入map容器后，后续使用对象时，从map容器中取出对象后还需要进行一步向下转型。
                }catch(ClassNotFoundException e){
                    e.printStackTrace();
                }catch (IllegalAccessException e){
                    e.printStackTrace();
                }catch (InstantiationException e){
                    e.printStackTrace();
                }
            }

        }
    }

    /*
        将已经创建的类对象取出
     */
    public Object getBean(String id){

        return beanMap.get(id);
    }


}

