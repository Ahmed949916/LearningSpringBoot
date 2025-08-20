package org.redmath.taskmanagement.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redmath.taskmanagement.entity.Users;
import org.redmath.taskmanagement.repository.UserRepo;
import org.redmath.taskmanagement.service.CustomOAuth2UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private CustomOAuth2UserService customOAuth2UserService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private Authentication authentication;

    @Mock
    private OAuth2User oAuth2User;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private SecurityConfig securityConfig;

    private JwtEncoder jwtEncoder;
    private Users testUser;

    @BeforeEach
    void setUp() {
        String testSecret = "234444444444444444444444444444342333333333333332333333333342343424";
        SecretKeySpec secretKey = new SecretKeySpec(testSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        jwtEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));

        ReflectionTestUtils.setField(securityConfig, "frontendBaseUrl", "http://localhost:3000");
        ReflectionTestUtils.setField(securityConfig, "signingKey", testSecret);

        testUser = new Users();
        testUser.setUserId(1L);
        testUser.setUsername("test@example.com");
    }

    @Test
    void testGenerateJwtAndRedirect_Success() {

        String email = "test@example.com";
        String picture = "http://example.com/picture.jpg";


        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(authentication.getName()).thenReturn("test-user");
        doReturn(authorities).when(authentication).getAuthorities();
        when(oAuth2User.getAttribute("email")).thenReturn(email);
        when(oAuth2User.getAttribute("picture")).thenReturn(picture);
        when(userRepo.findByUsername(email)).thenReturn(Optional.of(testUser));


        String result = (String) ReflectionTestUtils.invokeMethod(
                securityConfig,
                "generateJwtAndRedirect",
                authentication,
                jwtEncoder,
                userRepo
        );


        assertNotNull(result);
        assertTrue(result.startsWith("http://localhost:3000/oauth2/redirect?token="));
        assertTrue(result.contains("&userId=1"));

        verify(userRepo).findByUsername(email);
        verify(oAuth2User).getAttribute("email");
        verify(oAuth2User).getAttribute("picture");
        verify(authentication).getName();
        verify(authentication).getAuthorities();
    }

    @Test
    void testGenerateJwtAndRedirect_UserNotFound() {

        String email = "someone@example.com";
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("email")).thenReturn(email);
        when(userRepo.findByUsername(email)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                ReflectionTestUtils.invokeMethod(
                        securityConfig,
                        "generateJwtAndRedirect",
                        authentication,
                        jwtEncoder,
                        userRepo
                )
        );

        assertEquals("User not found", exception.getMessage());
        verify(userRepo).findByUsername(email);
    }


    @Test
    void testJwtAuthenticationConverter_Creation() {
        JwtAuthenticationConverter converter = securityConfig.jwtAuthenticationConverter();
        assertNotNull(converter);
    }

    @Test
    void testJwtEncoder() {
        JwtEncoder encoder = securityConfig.jwtEncoder();
        assertNotNull(encoder);
        assertInstanceOf(NimbusJwtEncoder.class, encoder);
    }

    @Test
    void testJwtDecoder() {
        JwtDecoder decoder = securityConfig.jwtDecoder();
        assertNotNull(decoder);
    }

    @Test
    void testConfigurationFields() {
        assertEquals("http://localhost:3000", ReflectionTestUtils.getField(securityConfig, "frontendBaseUrl"));
        assertNotNull(ReflectionTestUtils.getField(securityConfig, "signingKey"));
    }


    @Test
    void testJwtAuthenticationConverter_WithSingleRole() {
        var converter = securityConfig.jwtAuthenticationConverter();

        when(jwt.getClaimAsStringList("roles")).thenReturn(List.of("ROLE_USER"));

        var auth = converter.convert(jwt);

        assertNotNull(auth);
        assertNotNull(auth.getAuthorities());
        assertEquals(1, auth.getAuthorities().size());
        assertEquals("ROLE_USER", auth.getAuthorities().iterator().next().getAuthority());

        verify(jwt).getClaimAsStringList("roles");

    }
    @Test
    void testJwtAuthenticationConvertor_WithNullRoles() {
        var converter = securityConfig.jwtAuthenticationConverter();

        when(jwt.getClaimAsStringList("roles")).thenReturn(null);

        var auth = converter.convert(jwt);

        assertNotNull(auth);
        assertNotNull(auth.getAuthorities());
        assertTrue(auth.getAuthorities().isEmpty());

        verify(jwt).getClaimAsStringList("roles");
    }



}