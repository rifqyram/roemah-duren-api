package ac.unindra.roemah_duren_api.constant;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_SUPER_ADMIN("Super Admin"),
    ROLE_ADMIN("Admin");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }
}
