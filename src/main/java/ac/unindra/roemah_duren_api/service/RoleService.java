package ac.unindra.roemah_duren_api.service;


import ac.unindra.roemah_duren_api.constant.UserRole;
import ac.unindra.roemah_duren_api.entity.Role;

public interface RoleService {
    Role getOrSave(UserRole role);
}
