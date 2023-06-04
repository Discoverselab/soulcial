package org.springblade.modules.admin.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
		PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
		paginationInnerInterceptor.setMaxLimit(Long.MAX_VALUE);
		interceptor.addInnerInterceptor(paginationInnerInterceptor);
		return interceptor;
	}

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        // 允许跨域的域名
                        .allowedOriginPatterns("*")
                        // 允许任何方法（post、get等）
                        .allowedMethods("*")
                        // 允许任何请求头
                        .allowedHeaders("*")
                        // 允许证书、cookie
                        .allowCredentials(true)
                        .exposedHeaders(HttpHeaders.SET_COOKIE)
                        // maxAge(3600)表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
                        .maxAge(3600L);
            }
        };
    }

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册Sa-Token的路由拦截器
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                //TODO 测试阶段先不拦截请求
//				.addPathPatterns("/**")
				.excludePathPatterns("/**")

				.excludePathPatterns("/api/admin/login/**")
				.excludePathPatterns("/api/app/register/**")
                .excludePathPatterns("/login/**")
                .excludePathPatterns("/doc.html")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/swagger-ui.html/**")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/error")

                .excludePathPatterns("/resources/**");
    }

}
