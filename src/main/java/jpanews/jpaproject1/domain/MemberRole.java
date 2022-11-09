package jpanews.jpaproject1.domain;

import lombok.Getter;


@Getter
public enum MemberRole {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    MemberRole(final String value) {
        this.value = value;
    }

    private final String value;
}
