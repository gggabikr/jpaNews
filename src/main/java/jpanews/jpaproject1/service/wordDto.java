package jpanews.jpaproject1.service;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class wordDto {
    @NotEmpty
    private String wordName;
    @NotEmpty
    private String wordClass;
    @NotEmpty
    private String wordMeaning;
    @NotEmpty
    private String wordLanguage;

}
