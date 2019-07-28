package com.orangelala.service.auth.service;

import com.orangelala.framework.common.auth.request.LoginRequest;
import com.orangelala.framework.common.auth.response.LoginResponse;

public interface AuthService {
	public LoginResponse login(LoginRequest loginRequest);
}
