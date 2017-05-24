package cc.sion567.core;

import cc.sion567.dto.RpcRequest;
import cc.sion567.dto.RpcResponse;

public interface RpcClient {
    RpcResponse send(RpcRequest request) throws Exception;
}
