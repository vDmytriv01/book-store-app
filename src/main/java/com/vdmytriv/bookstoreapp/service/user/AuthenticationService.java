package com.vdmytriv.bookstoreapp.service.user;

public interface AuthenticationService {
    String authenticate(String email, String password);
}
