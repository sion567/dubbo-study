package cc.sion567;

import cc.sion567.bean.Foo;
import cc.sion567.bean.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TestFooBean {
    public static void main(String[] args){

        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        Foo foo = (Foo) ctx.getBean("test");
        System.out.println("remark:" + foo.getRemark());


        Student student1 = (Student) ctx.getBean("student1");
        Student student2 = (Student) ctx.getBean("student2");
        System.out.println("name: " +student1.getName()+" age :" + student1.getAge());
        System.out.println("name: " +student2.getName()+" age :" + student2.getAge());

    }
}
