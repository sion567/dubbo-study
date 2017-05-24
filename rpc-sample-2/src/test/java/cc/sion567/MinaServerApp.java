package cc.sion567;


import cc.sion567.mina.RpcServer;
import cc.sion567.service.HelloServiceImpl;
import com.google.common.collect.Maps;

import java.util.Map;

public class MinaServerApp {
    final static int port = 8080;

    public static void main(String[] args) throws Exception {
        //测试用,临时写死的
        Map<String, Object> vars = Maps.newHashMap();
        vars.put("cc.sion567.service.HelloService", new HelloServiceImpl());
        new RpcServer(port,vars).run();
    }
}
