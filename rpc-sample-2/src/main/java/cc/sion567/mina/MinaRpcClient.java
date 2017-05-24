package cc.sion567.mina;


import cc.sion567.core.RpcClient;
import cc.sion567.dto.RpcRequest;
import cc.sion567.dto.RpcResponse;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class MinaRpcClient extends IoHandlerAdapter implements RpcClient {
    private static final Logger log = LoggerFactory.getLogger(MinaRpcClient.class);

    private String host;
    private int port;

    private RpcResponse response;
    public MinaRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        log.debug("incomming 客户端: {}" , session.getRemoteAddress());
    }
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        log.debug("one Client Connection:{}" , session.getRemoteAddress());
    }
    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        log.debug("客户端发送信息异常....");
    }

    // 当客户端发送消息到达时
    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        log.debug("服务器返回的数据：{}" , message.toString());
        response = (RpcResponse)message;
        log.debug("result:{}",response.getResult());
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        log.debug("客户端与服务端断开连接.....");
    }


    @Override
    public RpcResponse send(RpcRequest request) throws Exception {
        //Create TCP/IP connection
        NioSocketConnector connector = new NioSocketConnector();

        //创建接受数据的过滤器
        DefaultIoFilterChainBuilder chain = connector.getFilterChain();

        //设定这个过滤器将一行一行(/r/n)的读取数据
        chain.addLast("myChin", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

        //客户端的消息处理器：一个SamplMinaServerHander对象
        connector.setHandler(MinaRpcClient.this);
        //set connect timeout
        connector.setConnectTimeoutMillis(5000);

        //连接到服务器：
        ConnectFuture cf = connector.connect(new InetSocketAddress(host,port));
        cf.awaitUninterruptibly();//Wait for the connection attempt to be finished.(等待连接创建成功)
//        session = future.getSession();// 获取会话
        cf.getSession().write(request);
        cf.getSession().getCloseFuture().awaitUninterruptibly();//等待连接断开,如果在Clinet关闭的话，放到它后面肯定一直阻塞，除非是Server那边主动关闭了
        connector.dispose();

        return response;
    }
}
