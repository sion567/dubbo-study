package cc.sion567.core;

import cc.sion567.dto.RpcRequest;
import cc.sion567.dto.RpcResponse;
import cc.sion567.mina.MinaRpcClient;
import cc.sion567.netty.NettyRpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

public class RpcProxy {

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<?> interfaceClass, final String addr, final int port,final String flag) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        RpcRequest request = new RpcRequest(); // 创建并初始化 RPC 请求
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setClassName(method.getDeclaringClass().getName());
                        request.setMethodName(method.getName());
                        request.setParameterTypes(method.getParameterTypes());
                        request.setParameters(args);

                        RpcClient client = buildClient(flag,addr, port); // 初始化 RPC 客户端
                        RpcResponse response = client.send(request); // 通过 RPC 客户端发送 RPC 请求并获取 RPC 响应

                        if (response.isError()) {
                            throw response.getError();
                        } else {
                            return response.getResult();
                        }
                    }
                }
        );
    }

    private RpcClient buildClient(String flag,String host,int port) {
        if("mina".equals(flag.toLowerCase())){
            return new MinaRpcClient(host,port);
        }else if("netty".equals(flag.toLowerCase())){
            return new NettyRpcClient(host,port);
        }else{
            throw new IllegalArgumentException("type is failed!!!");
        }
    }
}