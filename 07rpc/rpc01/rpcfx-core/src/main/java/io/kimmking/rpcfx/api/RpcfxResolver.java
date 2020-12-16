package io.kimmking.rpcfx.api;

public interface RpcfxResolver<T> {

   <T> T resolve(Class<T> t);

}
