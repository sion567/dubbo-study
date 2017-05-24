package cc.sion567.service;


public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        String result = "Hello! " + name;
        System.out.println("HelloService>>>>"+result);
        return result;
    }
}