package jpanews.jpaproject1.service;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;

@Getter @Setter
public class crawlingDto {
    @NotEmpty(message = "Article must have a title")
    String title;

    String subTitle;

    @NotEmpty(message = "Article must have a body")
    ArrayList<String> ArticleBody = new ArrayList<>();
}
