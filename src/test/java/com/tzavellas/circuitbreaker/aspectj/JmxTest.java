package com.tzavellas.circuitbreaker.aspectj;

import static org.junit.Assert.*;

import java.lang.management.ManagementFactory;

import javax.management.JMX;
import javax.management.ObjectName;

import org.junit.After;
import org.junit.Test;

import com.tzavellas.circuitbreaker.CircuitInfoMBean;
import com.tzavellas.circuitbreaker.OpenCircuitException;
import com.tzavellas.circuitbreaker.aspectj.CircuitBreakerConfigurator;
import com.tzavellas.test.TimeService;

public class JmxTest {
	
	TimeService time ;
	
	@After
	public void disableJmx() {
		CircuitBreakerConfigurator.aspectOf().setEnableJmx(false);
		IntegrationPointBreaker.aspectOf(time).setEnableJmx(false);
	}
	
	@Test(expected=OpenCircuitException.class)
	public void enable_jmx_via_configurator() throws Exception {		
		CircuitBreakerConfigurator.aspectOf().setEnableJmx(true);
		time = new TimeService();
		testOpenCircuitViaJmx();
	}
	
	@Test(expected=OpenCircuitException.class)
	public void enable_jmx_via_aspect_setter() throws Exception {
		time = new TimeService();
		IntegrationPointBreaker.aspectOf(time).setEnableJmx(true);
		testOpenCircuitViaJmx();
	}
	
	private void testOpenCircuitViaJmx() throws Exception {
		time.networkTime();
		
		CircuitInfoMBean mbean = JMX.newMBeanProxy(
				ManagementFactory.getPlatformMBeanServer(),
				new ObjectName("com.tzavellas.circuitbreaker:type=CircuitInfo,target=TimeService"),
				CircuitInfoMBean.class);
		
		assertEquals(1, mbean.getCalls());
		mbean.open();
		time.networkTime();
	}
}