package vnpt_it.vn.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@EnableMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchange -> exchange
//                        .pathMatchers("/userinfo", "/jobs/**").authenticated()
                                .anyExchange().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter()))
                        )
                )
                // wrap JwtAuthenticationConverter thành reactive bằng ReactiveJwtAuthenticationConverterAdapter.
                //ReactiveJwtAuthenticationConverterAdapter dùng để bọc JwtAuthenticationConverter (blocking)
                // thành reactive converter đúng kiểu mà WebFlux yêu cầu.
                .csrf(ServerHttpSecurity.CsrfSpec::disable);
//                .cors(ServerHttpSecurity.CorsSpec::disable); // hoặc bật CORS nếu cần

        return http.build();
    }

    //chuyển đổi thông tin trong Access Token (JWT) thành danh sách các quyền (authorities) mà Spring Security có thể hiểu và sử dụng để phân quyền.
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = Optional.ofNullable(jwt.getClaimAsStringList("roles"))
                    .orElse(List.of()).stream().map(role -> "ROLE_" + role).toList();
            List<String> scopes = Optional.ofNullable(jwt.getClaimAsStringList("scope"))
                    .orElse(List.of()).stream().map(scope -> "SCOPE_" + scope).toList();
            // Tạo list mutable thay vì dùng List.of()
            List<String> authorities = new ArrayList<>();
            authorities.addAll(roles);
            authorities.addAll(scopes);

            return authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });
        return jwtAuthenticationConverter;
    }

}
