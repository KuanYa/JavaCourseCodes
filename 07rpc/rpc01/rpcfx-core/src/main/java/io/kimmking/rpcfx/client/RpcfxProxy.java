package io.kimmking.rpcfx.client;

public interface RpcfxProxy {

    <T> T create(final Class<T> serviceClass, final String url);
}
