package io.kimmking.rpcfx.client;

import io.kimmking.rpcfx.proxy.RpcfxInvocationHandler;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

public final class RpcfxByteBuddy extends RpcProxyCache implements RpcfxProxy{

    @Override
    public <T> T create(final Class<T> serviceClass, final String url) {
        if (isExit(serviceClass.getName())) {
            return (T) getProxy(serviceClass.getName());
        }
        T proxy = newProxy(serviceClass, url);
        add(serviceClass.getName(), proxy);
        return proxy;
    }

    public <T> T newProxy(final Class<T> serviceClass, final String url) {
        // 使用 ByteBuddy
        // 0. 替换动态代理 -> AOP
        T byteBuddy = null;
        try {
            byteBuddy = new ByteBuddy()
                    .subclass(serviceClass)
                    .method(ElementMatchers.any())
                    .intercept(InvocationHandlerAdapter.of(new RpcfxInvocationHandler(serviceClass, url)))
                    .make()
                    .load(serviceClass.getClassLoader())
                    .getLoaded().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return byteBuddy;
    }
}
