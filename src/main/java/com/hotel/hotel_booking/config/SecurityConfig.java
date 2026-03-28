package com.hotel.hotel_booking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.hotel.hotel_booking.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
    private CustomUserDetailsService userDetailsService;
	
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Vô hiệu hóa CSRF (Cần thiết nếu bạn dùng API hoặc chưa cấu hình CSRF token ở giao diện)
            .csrf(csrf -> csrf.disable())
            
            // 2. Cấu hình phân quyền
            .authorizeHttpRequests(auth -> auth
                // Gom nhóm các tài nguyên công khai
                .requestMatchers(
                    "/", "/register", "/login", "/users/register",
                    "/css/**", "/js/**", "/images/**", "/webjars/**"
                ).permitAll()
                
                // Phân quyền dựa trên Role (Spring tự hiểu "ADMIN" là "ROLE_ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Các chức năng yêu cầu đăng nhập
                .requestMatchers("/bookings/**", "/users/change-password", "/profile/**").authenticated()
                
                // Mọi request còn lại đều cho phép truy cập (để xem danh sách phòng, v.v.)
                .anyRequest().permitAll()
            )
            
            // 3. Cấu hình Login
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login") // URL xử lý đăng nhập (trùng với action của form)
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true") // Chuyển hướng khi sai mật khẩu
                .permitAll()
            )
            
            // 4. Cấu hình Logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true) // Xóa session
                .deleteCookies("JSESSIONID") // Xóa cookie
                .permitAll()
            )
            
            // 5. Cấu hình ghi nhớ đăng nhập 
            .rememberMe(remember -> remember
                    .key("uniqueAndSecret") 
                    .tokenValiditySeconds(86400) // 1 ngày
                    .userDetailsService(userDetailsService) // Sử dụng CustomUserDetailsService
                    .rememberMeParameter("remember-me") // Tên của checkbox trong login.html
            );

        return http.build();
    }
}