package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.support.ErrorCode;
import kr.hhplus.be.server.support.HangHeaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserById(long userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new HangHeaException(ErrorCode.NOT_FOUND_USER));
    }


}
