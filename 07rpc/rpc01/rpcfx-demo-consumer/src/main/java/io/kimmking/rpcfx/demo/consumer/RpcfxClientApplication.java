package io.kimmking.rpcfx.demo.consumer;

import io.kimmking.rpcfx.client.RpcfxByteBuddy;
import io.kimmking.rpcfx.client.RpcfxProxy;
import io.kimmking.rpcfx.demo.api.Order;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class RpcfxClientApplication {

	// 二方库
	// 三方库 lib
	// nexus, userserivce -> userdao -> user
	//
	public static void main(String[] args) {

		RpcfxProxy rpcfxProxy = new RpcfxByteBuddy();
		UserService userService = rpcfxProxy.create(UserService.class, "http://localhost:8082/");
		User user = userService.findById(1);
		System.out.println("find user id=1 from server: " + user.getName());

		OrderService orderService = rpcfxProxy.create(OrderService.class, "http://localhost:8082/");
		Order order = orderService.findOrderById(1992129);
		System.out.println(String.format("find order name=%s, amount=%f",order.getName(),order.getAmount()));

		// 新加一个OrderService

		// SpringApplication.run(RpcfxClientApplication.class, args);
	}

}
