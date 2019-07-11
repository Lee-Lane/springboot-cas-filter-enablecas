package com.example.casspringboot2.cas;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

@ConfigurationProperties(prefix = "cas")
@Getter
@Setter
@Configuration
public class CasClientProperties {
    /**
     * 是否开启单点登录
     */
    private boolean enable = true;
    /**
     * 单点登录需要访问的CAS SERVER URL入口
     */
    private String casServerLoginUrl;
    /**
     * 托管此应用的服务器名称，例如本机：http://localhost:8080
     */
    private String serverName;

    /**
     * 指定是否应将renew = true发送到CAS服务器
     */
    private boolean renew = false;
    /**
     * 指定是否应将gateway = true发送到CAS服务器
     */
    private boolean gateway = false;

    /**
     * cas服务器的开头  例如 http://localhost:8443/cas
     */
    private String casServerUrlPrefix;
    /**
     * 是否将Assertion 存入到session中
     * 如果不使用session(会话)，tickets(票据)将每次请求时都需要tickets
     */
    private boolean useSession = true;
    /**
     * 是否在票证验证后重定向到相同的URL，但在参数中没有票证
     */
    private boolean redirectAfterValidation = true;
    /**
     * 是否在tickets验证失败时抛出异常
     */
    private boolean exceptionOnValidationFailure = false;

    /**
     * 验证白名单，当请求路径匹配此表达式时，自动通过验证
     */
    @Nullable
    private String ignorePattern;

    /**
     * 白名单表达式的类型
     * REGEX 正则表达式 默认的
     * CONTAINS  包含匹配
     * EXACT 精确匹配
     */
    @Nullable
    private String ignoreUrlPatternType;

}
