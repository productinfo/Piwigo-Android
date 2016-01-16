package org.piwigo.ui.viewmodel;

import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.piwigo.R;
import org.piwigo.io.repository.UserRepository;

import java.util.regex.Pattern;

import rx.Observable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginViewModelTest {

    private static final String URL = "http://demo.piwigo.org";
    private static final String USERNAME = "demo";
    private static final String PASSWORD = "demo";

    private LoginViewModel viewModel;

    @Before public void setUp() {
        viewModel = new LoginViewModel();
        viewModel.WEB_URL = Pattern.compile("http://demo.piwigo.org");
        viewModel.userRepository = mock(UserRepository.class);
        when(viewModel.userRepository.login(URL, USERNAME, PASSWORD)).thenReturn(Observable.empty());
    }

    @Test public void shouldSaveState() {
        viewModel.url.set(URL);
        viewModel.username.set(USERNAME);
        viewModel.password.set(PASSWORD);
        Bundle bundle = mock(Bundle.class);

        viewModel.onSave(bundle);

        verify(bundle).putString(LoginViewModel.STATE_URL, URL);
        verify(bundle).putString(LoginViewModel.STATE_USERNAME, USERNAME);
        verify(bundle).putString(LoginViewModel.STATE_PASSWORD, PASSWORD);
    }

    @Test public void shouldSetEmptyUrlError() {
        viewModel.url.set(null);
        viewModel.onLoginClick(null);
        assertThat(viewModel.url.getError()).isEqualTo(R.string.login_url_empty);
    }

    @Test public void shouldSetInvalidUrlError() {
        viewModel.url.set("Junk");
        viewModel.onLoginClick(null);
        assertThat(viewModel.url.getError()).isEqualTo(R.string.login_url_invalid);
    }

    @Test public void shouldSetEmptyUsernameError() {
        viewModel.username.set(null);
        viewModel.onLoginClick(null);
        assertThat(viewModel.username.getError()).isEqualTo(R.string.login_username_empty);
    }

    @Test public void shouldSetEmptyPasswordError() {
        viewModel.password.set(null);
        viewModel.onLoginClick(null);
        assertThat(viewModel.password.getError()).isEqualTo(R.string.login_password_empty);
    }

    @Test public void shouldLoginIfValid() {
        viewModel.url.set(URL);
        viewModel.username.set(USERNAME);
        viewModel.password.set(PASSWORD);

        viewModel.onLoginClick(null);

        verify(viewModel.userRepository).login(URL, USERNAME, PASSWORD);
    }

    @Test public void shouldRestoreState() {
        Bundle bundle = mock(Bundle.class);
        when(bundle.getString(LoginViewModel.STATE_URL)).thenReturn(URL);
        when(bundle.getString(LoginViewModel.STATE_USERNAME)).thenReturn(USERNAME);
        when(bundle.getString(LoginViewModel.STATE_PASSWORD)).thenReturn(PASSWORD);

        viewModel.onRestore(bundle);

        assertThat(viewModel.url.get()).isEqualTo(URL);
        assertThat(viewModel.username.get()).isEqualTo(USERNAME);
        assertThat(viewModel.password.get()).isEqualTo(PASSWORD);
    }

}