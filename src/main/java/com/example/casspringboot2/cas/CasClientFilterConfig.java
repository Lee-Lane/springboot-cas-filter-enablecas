package com.example.casspringboot2.cas;


import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CasClientFilterConfig {


    @Autowired
    private CasClientProperties casClientProperties;


    /**
     * 单点登出
     *
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listener =
                new ServletListenerRegistrationBean<>();
        listener.setEnabled(casClientProperties.isEnable());
        listener.setListener(new SingleSignOutHttpSessionListener());
        listener.setOrder(1);
        return listener;
    }

    @Bean
    public FilterRegistrationBean singleSignOutFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new SingleSignOutFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.addInitParameter("casServerUrlPrefix", casClientProperties.getCasServerUrlPrefix());
        registrationBean.setEnabled(casClientProperties.isEnable());
        registrationBean.setOrder(2);
        return registrationBean;
    }

    /**
     * 认证过滤器
     * 如果用户需要进行身份验证，则会将用户重定向到CAS服务器。
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean authenticationFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new AuthenticationFilter());
        filterRegistrationBean.setEnabled(casClientProperties.isEnable());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("casServerLoginUrl", casClientProperties.getCasServerLoginUrl());
        filterRegistrationBean.addInitParameter("serverName", casClientProperties.getServerName());
        filterRegistrationBean.addInitParameter("gateway", String.valueOf(casClientProperties.isGateway()));
        //   filterRegistrationBean.addInitParameter("renew", String.valueOf(casClientProperties.isRenew()));
        filterRegistrationBean.setOrder(3);
        return filterRegistrationBean;
    }


    /**
     * 使用 CAS 2.0 protocol. ticket校验工作
     * Cas30ProxyReceivingTicketValidationFilter 使用cas3.0 protocol
     * Cas30JsonProxyReceivingTicketValidationFilter 过滤器能够接受CAS的验证响应，根据CAS协议规定的格式为JSON
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean cas20ProxyReceivingTicketValidationFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new Cas20ProxyReceivingTicketValidationFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.addInitParameter("casServerUrlPrefix", casClientProperties.getCasServerUrlPrefix());
        registrationBean.addInitParameter("serverName", casClientProperties.getServerName());
        registrationBean.addInitParameter("useSession", String.valueOf(casClientProperties.isUseSession()));
        registrationBean.addInitParameter("exceptionOnValidationFailure", String.valueOf(casClientProperties.isExceptionOnValidationFailure()));
        registrationBean.addInitParameter("redirectAfterValidation", String.valueOf(casClientProperties.isRedirectAfterValidation()));
        registrationBean.setEnabled(casClientProperties.isEnable());
        registrationBean.setOrder(4);
        return registrationBean;
    }


    /**
     * 将断言信息存放在ThreadLocal中，可以通过此类获取登录的用户信息
     * 可以在任意地方获取到用户信息 AssertionHolder类是专门处理此信息类
     * 但是此类无法访问 HttpServletRequest，因此无法调用 getRemoteUser()
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean assertionThreadLocalFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setEnabled(casClientProperties.isEnable());
        registrationBean.setOrder(5);
        registrationBean.setFilter(new AssertionThreadLocalFilter());
        return registrationBean;
    }

    /**
     * HttpServletRequest包装类
     * 可以通过getRemoteUser()与getPrincipal()获取相应CAS的信息
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean requestWrapperFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setEnabled(casClientProperties.isEnable());
        registrationBean.setFilter(new HttpServletRequestWrapperFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(6);
        return registrationBean;
    }


}
