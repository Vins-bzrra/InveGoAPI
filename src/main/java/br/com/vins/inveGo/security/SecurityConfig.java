package br.com.vins.inveGo.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.vins.inveGo.filters.AuthenticateFilter;
import br.com.vins.inveGo.filters.ValidadeFilter;
import br.com.vins.inveGo.service.DetailUserServiceImpl;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final DetailUserServiceImpl usuarioService;
	private final PasswordEncoder passwordEncoder;
	private final BlacklistToken blacklistToken;

	public SecurityConfig(DetailUserServiceImpl usuarioService, PasswordEncoder passwordEncoder, BlacklistToken blacklistToken) {
		this.usuarioService = usuarioService;
		this.passwordEncoder = passwordEncoder;
		this.blacklistToken = blacklistToken;
	}

	protected void configure(HttpSecurity http) throws Exception {
		 http.authorizeRequests()
         .antMatchers(HttpMethod.POST, "/api/users/login").permitAll()
         .anyRequest().authenticated()
         .and().cors().and().csrf().disable()
         .addFilter(new AuthenticateFilter(authenticationManager()))
         .addFilter(new ValidadeFilter(authenticationManager(), blacklistToken))
         .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioService).passwordEncoder(passwordEncoder);
	}
}
