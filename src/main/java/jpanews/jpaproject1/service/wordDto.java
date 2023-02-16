package jpanews.jpaproject1.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class wordDto {
    @Nullable
    private Long Id;
    @NotEmpty
    private String wordName;
    private String wordClass;
    @NotEmpty
    private String wordMeaning;
    @NotEmpty
    private String wordLanguage;

}
