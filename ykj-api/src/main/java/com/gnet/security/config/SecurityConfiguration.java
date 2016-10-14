package com.gnet.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.gnet.app.authentication.user.SysUser;
import com.gnet.security.client.SpringDataMyBatisClientDetailsService;
import com.gnet.security.cors.CorsFilter;
import com.gnet.security.entryPoint.RestAuthenticationEntryPoint;
import com.gnet.security.handler.SavedRequestAwareAuthenticationFailureHandler;
import com.gnet.security.handler.SavedRequestAwareAuthenticationSuccessHandler;
import com.gnet.security.token.CustomTokenEnhancer;
import com.gnet.security.user.SpringDataMyBatisUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private SpringDataMyBatisUserDetailsService userDetailsService;

	@Override
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.userDetailsService(this.userDetailsService)
		.passwordEncoder(SysUser.PASSWORD_ENCODER);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers(
					  "/druid/**",
					  "/images/**"
			);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class);
		
		http.authorizeRequests().anyRequest().denyAll();
	}
	
	@Order(1)
	@Configuration
	@EnableAuthorizationServer
	public static class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	    private final AuthenticationManager authenticationManager;
	    @Autowired
	    private TokenStore tokenStore;
	    @Autowired
	    private SpringDataMyBatisClientDetailsService clientDetailsService;
	    
	    @Autowired
	    public AuthorizationServerConfig(AuthenticationManager authenticationManager) {
	    	this.authenticationManager = authenticationManager;
	    }
	    
	    /**
	     * Defines the security constraints on the token endpoints /oauth/token_key and /oauth/check_token
	     * Client credentials are required to access the endpoints
	     *
	     * @param oauthServer
	     * @throws Exception
	     */
	    @Override
	    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
	        oauthServer
//	        .passwordEncoder(Client.PASSWORD_ENCODER)
	        .tokenKeyAccess("permitAll()")
			.checkTokenAccess("isAuthenticated()");
	    }

	    /**
	     * Defines the authorization and token endpoints and the token services
	     *
	     * @param endpoints
	     * @throws Exception
	     */
	    @Override
	    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
	        endpoints
	        .authenticationManager(this.authenticationManager)
	        .tokenEnhancer(tokenEnhancer())
	        .tokenStore(tokenStore);
	    }

	    @Override
	    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
	        clients
	        .withClientDetails(clientDetailsService);
	    }
	    
	    @Bean
	    public TokenEnhancer tokenEnhancer() {
	    	return new CustomTokenEnhancer();
	    }
		
	}
	
	@Order(3)
	@Configuration
	@EnableResourceServer
	public static class ApiResources extends ResourceServerConfigurerAdapter {
		
		@Autowired
	    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
		@Autowired
		private AuthenticationSuccessHandler successHandler;
		@Autowired
		private AuthenticationFailureHandler failureHandler;
		@Autowired
	    private TokenStore tokenStore;

		@Override
		public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
			resources
			.tokenStore(tokenStore);
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {
			http
			.antMatcher("/api/**")
			.exceptionHandling()
			.authenticationEntryPoint(restAuthenticationEntryPoint)
			.and()
			.authorizeRequests()
//				.anyRequest().hasIpAddress("10.32.49.0/24")
				.anyRequest().authenticated()
				.anyRequest().access("#oauth2.isUser()")
				.and()
			.formLogin()
				.successHandler(successHandler)
				.failureHandler(failureHandler)
				.and()
			.logout();
		}
		
	}
	
	@Bean
	public static AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
		return new SavedRequestAwareAuthenticationSuccessHandler();
	}
	
	@Bean
	public static AuthenticationFailureHandler myAuthenticationFailureHandler() {
		return new SavedRequestAwareAuthenticationFailureHandler();
	}
	
}
