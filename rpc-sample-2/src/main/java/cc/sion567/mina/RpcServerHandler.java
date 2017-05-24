package cc.sion567.mina;


import cc.sion567.dto.RpcRequest;
import cc.sion567.dto.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class RpcServerHandler extends IoHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(RpcServerHandler.class);

    private final Map<String, Object> handlerMap;

    public RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        System.out.println("---------------------");
        RpcResponse response = new RpcResponse();
        System.out.println(message);
        if (message instanceof RpcRequest) {
            RpcRequest request = (RpcRequest) message;
            response.setRequestId(request.getRequestId());
            try {
                Object result = handle(request);
                response.setResult(result);
            } catch (Throwable t) {
                response.setError(t);
            }
        }
        session.write(response);
    }

    private Object handle(RpcRequest request) throws Throwable {
        System.out.println("==================");
        String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception{
        log.info("新连接的客户端地址：{}" , session.getRemoteAddress());
    }
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        log.info("客户端与服务端断开连接.....");
    }
}
