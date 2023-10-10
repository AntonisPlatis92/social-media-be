package com.socialmedia.application.domain.controllers.vms;

public record CreateUserVM(String email, String password, Long roleId) {
}
