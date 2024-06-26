package ac.unindra.roemah_duren_api.entity;

import ac.unindra.roemah_duren_api.constant.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_role")
@SuperBuilder
public class Role extends BaseEntity {
    @Column(name = "role", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
