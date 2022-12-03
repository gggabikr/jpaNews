package jpanews.jpaproject1.service;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;

@Getter @Setter
public class crawlingDto {
    @NotEmpty(message = "Article must have a title")
    String title;

    ArrayList<String> title_each  = new ArrayList<>();

    String subTitle;

    ArrayList<String> subTitle_each = new ArrayList<>();

    @NotEmpty(message = "Article must have a body")
    ArrayList<String> ArticleBody = new ArrayList<>();

//    ArrayList<String[]> ArticleBody_each;
}
