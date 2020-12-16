package io.kimmking.rpcfx.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResolver;
import io.kimmking.rpcfx.api.RpcfxResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class RpcfxInvoker {

    private RpcfxResolver resolver;


    public RpcfxInvoker(RpcfxResolver resolver) {
        this.resolver = resolver;
    }

    public RpcfxResponse invoke(RpcfxRequest request){
        RpcfxResponse response = new RpcfxResponse();
        String serviceClass = request.getServiceClass();
        Object[] params = request.getParams();
        // 获取参数类型
        try {
            // 作业1：改成泛型和反射
            // 先反射获取Class
            Class<?> aClass = Class.forName(serviceClass);
            // 通过 class 获取注入的bean，就可以实现去掉 @Bean(name = "XXX")中的name
            Object service =  resolver.resolve(aClass);//this.applicationContext.getBean(serviceClass);

            Method method = aClass.getMethod(request.getMethod(),request.getParameterTypes());
            // Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            Object result = method.invoke(service, params); // dubbo, fastjson,
            // 两次json序列化能否合并成一个
            response.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
            response.setStatus(true);
            return response;
        } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException | NoSuchMethodException  e) {

            // 3.Xstream

            // 2.封装一个统一的RpcfxException
            // 客户端也需要判断异常
            e.printStackTrace();
            response.setException(e);
            response.setStatus(false);
            return response;
        }
    }

    /**
     * 此处使用反射替换
     *
     * @param klass
     * @param methodName
     * @return
     */
    private Method resolveMethodFromClass(Class<?> klass, String methodName) {
        return Arrays.stream(klass.getMethods()).filter(m -> methodName.equals(m.getName())).findFirst().get();
    }

}
