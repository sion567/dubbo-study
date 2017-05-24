package cc.sion567;

import cc.sion567.service.HelloService;
import cc.sion567.core.RpcProxy;


public class TestMain {
    public static void main(String[] args) throws Exception {
        RpcProxy rpcProxy = new RpcProxy();
        HelloService helloService = rpcProxy.create(HelloService.class,"127.0.0.1",8080,"mina");
        String result = helloService.hello("kk");
        System.out.println(result);
    }
}
