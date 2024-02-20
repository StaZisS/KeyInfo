package org.example.key_info.password;

import org.example.key_info.core.util.PasswordTool;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PasswordToolUnitTest {

    @Test
    public void testGoodPassword() {
        String password = "password";
        String hashPassword = PasswordTool.getHashPassword(password);
        assertThat(PasswordTool.isCorrectPassword(hashPassword, password)).isTrue();
    }

    @Test
    public void testBadPassword() {
        String password = "password";
        String hashPassword = PasswordTool.getHashPassword(password);
        assertThat(PasswordTool.isCorrectPassword(hashPassword, "wrongPassword")).isFalse();
    }

    @Test
    public void testEmptyPassword() {
        String password = "";
        String hashPassword = PasswordTool.getHashPassword(password);
        assertThat(PasswordTool.isCorrectPassword(hashPassword, password)).isTrue();
    }

    @Test
    public void testNullPassword() {
        String password = null;
        assertThatThrownBy(() -> PasswordTool.getHashPassword(password))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testNullHashPassword() {
        String password = "password";
        String hashPassword = null;
        assertThat(PasswordTool.isCorrectPassword(hashPassword, password)).isFalse();
    }
}
