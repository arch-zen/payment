package com.ymatou.payment.facade.impl.rest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.ymatou.payment.facade.PaymentFacade;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;

/**
 * 支付REST接口实现
 * @author wangxudong
 *
 */
@Path("/api")
@Component("paymentResource")
public class PaymentResourceImpl implements PaymentResource {

	/**
	 * 支付接口
	 */
	@Resource
	private PaymentFacade paymentFacade;
	
	/**
	 * 支付收单
	 */
	@POST
	@Path("/AcquireOrder")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public AcquireOrderResp acquireOrder(AcquireOrderReq req, @Context HttpServletRequest servletRequest) {
		
		return paymentFacade.acquireOrder(req);
	}

}
