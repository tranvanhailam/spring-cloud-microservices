package com.example.authservice.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.KeyStoreKeyFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
// Kích hoạt WebSecurity và các loại annotation phân quyền như: @Secured, @PreAuthorize, @RolesAllowed
public class ServerSecurityConfig {

    private final UserDetailsService userDetailsService;

    public ServerSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Cấu hình xác thực người dùng bằng UserDetailsService và mã hoá mật khẩu bằng BCrypt
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // Cấu hình Spring Authorization Server (vai trò là Authorization Server)
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())// Áp dụng filter chain cho các endpoint của Authorization Server
                .with(authorizationServerConfigurer, (authorizationServer) ->
                        authorizationServer.oidc(Customizer.withDefaults()) // Bật OpenID Connect support (OIDC)
                );

        http
                .exceptionHandling(exception -> exception.defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint("/login"), // Nếu chưa đăng nhập thì chuyển hướng về /login
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                ));

        http
                .cors(Customizer.withDefaults()) // Cho phép CORS
                .formLogin(Customizer.withDefaults()); // Cho phép form login

        return http.build(); // Trả về filter chain đã cấu hình
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // Cấu hình security mặc định cho các request khác (ngoài OAuth2 endpoint)
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/actuator/**").permitAll() // Cho phép truy cập các endpoint giám sát
                        .anyRequest().authenticated() // Các request khác yêu cầu đăng nhập
                )
                .cors(Customizer.withDefaults()) // Cho phép CORS
                .formLogin(Customizer.withDefaults()); // Cho phép login bằng form

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Cấu hình CORS: cho phép tất cả origin, header, method
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Dùng BCrypt để mã hoá mật khẩu
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        // Cấu hình client có thể sử dụng Authorization Server
        RegisteredClient registeredClient = RegisteredClient.withId("client")
                .clientId("client") // client_id
                .clientSecret(this.passwordEncoder().encode("26102004")) // client_secret (được mã hoá)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) // phương thức xác thực client
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE) // hỗ trợ grant_type: authorization_code
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) // hỗ trợ refresh_token
                .redirectUri("https://oauthdebugger.com/debug") // URI redirect sau khi xác thực thành công
                .scope("read") // phạm vi truy cập
                .scope("write")
                .scope("openid")
                .scope("profile")
                .scope("email")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build()) // bắt buộc người dùng xác nhận quyền
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(10)) // thời gian sống của access token
                        .refreshTokenTimeToLive(Duration.ofMinutes(3600)) // thời gian sống của refresh token
                        .build())
                .build();


        RegisteredClient registeredClientAccountService = RegisteredClient.withId("account-service")
                .clientId("account-service") // client_id
                .clientSecret(this.passwordEncoder().encode("26102004")) // client_secret (được mã hoá)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) // phương thức xác thực client
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS) // hỗ trợ grant_type: authorization_code
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) // hỗ trợ refresh_token
                .redirectUri("https://oauthdebugger.com/debug") // URI redirect sau khi xác thực thành công
                .scope("read") // phạm vi truy cập
                .scope("write")
                .scope("internal")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build()) // bắt buộc người dùng xác nhận quyền
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(10)) // thời gian sống của access token
                        .refreshTokenTimeToLive(Duration.ofMinutes(3600)) // thời gian sống của refresh token
                        .build())
                .build();

        RegisteredClient registeredClientJobService = RegisteredClient.withId("job-service")
                .clientId("job-service") // client_id
                .clientSecret(this.passwordEncoder().encode("26102004")) // client_secret (được mã hoá)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) // phương thức xác thực client
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS) // hỗ trợ grant_type: authorization_code
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) // hỗ trợ refresh_token
                .redirectUri("https://oauthdebugger.com/debug") // URI redirect sau khi xác thực thành công
                .scope("read") // phạm vi truy cập
                .scope("write")
                .scope("internal")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build()) // bắt buộc người dùng xác nhận quyền
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(10)) // thời gian sống của access token
                        .refreshTokenTimeToLive(Duration.ofMinutes(3600)) // thời gian sống của refresh token
                        .build())
                .build();

        RegisteredClient registeredClientResumeService = RegisteredClient.withId("resume-service")
                .clientId("resume-service") // client_id
                .clientSecret(this.passwordEncoder().encode("26102004")) // client_secret (được mã hoá)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) // phương thức xác thực client
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS) // hỗ trợ grant_type: authorization_code
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) // hỗ trợ refresh_token
                .redirectUri("https://oauthdebugger.com/debug") // URI redirect sau khi xác thực thành công
                .scope("read") // phạm vi truy cập
                .scope("write")
                .scope("internal")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build()) // bắt buộc người dùng xác nhận quyền
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(10)) // thời gian sống của access token
                        .refreshTokenTimeToLive(Duration.ofMinutes(3600)) // thời gian sống của refresh token
                        .build())
                .build();

        RegisteredClient registeredClientSubscriberService = RegisteredClient.withId("subscriber-service")
                .clientId("subscriber-service") // client_id
                .clientSecret(this.passwordEncoder().encode("26102004")) // client_secret (được mã hoá)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) // phương thức xác thực client
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS) // hỗ trợ grant_type: authorization_code
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) // hỗ trợ refresh_token
                .redirectUri("https://oauthdebugger.com/debug") // URI redirect sau khi xác thực thành công
                .scope("read") // phạm vi truy cập
                .scope("write")
                .scope("internal")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build()) // bắt buộc người dùng xác nhận quyền
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(10)) // thời gian sống của access token
                        .refreshTokenTimeToLive(Duration.ofMinutes(3600)) // thời gian sống của refresh token
                        .build())
                .build();


        return new InMemoryRegisteredClientRepository(registeredClient,
                registeredClientAccountService,
                registeredClientJobService,
                registeredClientResumeService,
                registeredClientSubscriberService);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
        // Tuỳ chỉnh token được tạo (JWT) – thêm thông tin roles vào payload
        return (context) -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                context.getClaims().claims((claims) -> {
                    Set<String> roles = AuthorityUtils.authorityListToSet(context.getPrincipal().getAuthorities());
                    claims.put("roles", roles); // đưa roles vào claims của JWT
                });
            }
        };
    }

    private static KeyPair generateKeyPair() {
        // Tạo cặp khóa (private key) RSA dùng để ký JWT
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        // Nếu dùng file JKS để lấy khóa:
        //Key file: >keytool -genkeypair -alias cloud-server -keyalg RSA -keysize 2048 -validity 365 -keystore cloud-server.jks
        // KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("cloud-server.jks"), "26102004".toCharArray());
        // keyPair = keyStoreKeyFactory.getKeyPair("cloud-server");

        return keyPair;
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        // Chuyển KeyPair sang JWK để Authorization Server có thể ký token bằng RSA
        KeyPair keyPair = generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }
}

