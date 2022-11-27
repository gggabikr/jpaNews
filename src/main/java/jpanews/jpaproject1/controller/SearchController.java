package jpanews.jpaproject1.controller;

import jpanews.jpaproject1.service.articleCrawling;
import jpanews.jpaproject1.service.crawlingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class SearchController {

    private final articleCrawling articleCrawling;

    @GetMapping("/searchURL")
    public String searchURL(Model model, String URL){
        crawlingDto dto = articleCrawling.crawl(URL);
        model.addAttribute("ArticleTitle", dto.getTitle());
        model.addAttribute("ArticleSubtitle", dto.getSubTitle());
        model.addAttribute("ArticleBodyList", dto.getArticleBody());
        return "articleSearch";
    }
}
