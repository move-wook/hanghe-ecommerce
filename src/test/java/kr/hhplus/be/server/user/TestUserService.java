package kr.hhplus.be.server.user;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestUserService {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("사용자 ID 사용자를 조회한다.")
    void shouldReturnUserWhenUserExists() {
        // Given
        long userId = 1L;
        User mockUser = User.builder()
                .id(userId)
                .userName("임동욱")
                .build();
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(mockUser));

        // When
        User result = userService.getUserById(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getUserName()).isEqualTo("임동욱");
        verify(userRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("없는 사용자 조회시 HangHeaException을 발생시킨다.")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        long userId = 1L;

        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        HangHeaException exception1 = assertThrows(HangHeaException.class, () -> {
            userService.getUserById(userId);
        });
        // When & Then
        assertThat(ErrorCode.NOT_FOUND_USER).isEqualTo(exception1.getErrorCode());

    }
}
