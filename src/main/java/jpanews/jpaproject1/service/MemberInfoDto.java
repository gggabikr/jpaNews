package jpanews.jpaproject1.service;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberInfoDto {
    @NotEmpty(message = "You must enter the username.")
    private String username;
    @NotEmpty(message = "You must enter the password.")
    private String password;
    @NotEmpty(message = "You must choose the role.")
    private String role;
}
