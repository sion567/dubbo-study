package cc.sion567.mina;


import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;

public class RpcServer {
    private static final Logger log = LoggerFactory.getLogger(RpcServer.class);

    private final int port;
    private Map<String, Object> handlerMap; // 存放接口名与服务对象之间的映射关系

    public RpcServer(int port,Map<String, Object> handlerMap) {
        this.port = port;
        this.handlerMap = handlerMap;
    }

    public void run() throws Exception {
        // 服务器端的主要对象
        IoAcceptor acceptor = new NioSocketAcceptor();
        // 设置接收缓存区大小
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);  //读写通道无任何操作就进入空闲状态

        // 设置Filter链
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        // 协议解析，采用mina现成的UTF-8字符串处理方式
        acceptor.getFilterChain().addLast("myChin", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        // 设置消息处理类（创建、关闭Session，可读可写等等，继承自接口IoHandler）
        acceptor.setHandler(new RpcServerHandler(handlerMap));
        try {
            // 服务器开始监听
            acceptor.bind(new InetSocketAddress(port));
            log.info("start up......");
        }catch(Exception e){
            log.error("server error!!!",e);
        }
    }
}
