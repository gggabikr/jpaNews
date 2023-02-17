package jpanews.jpaproject1.controller;

import jpanews.jpaproject1.domain.Member;
import jpanews.jpaproject1.domain.WordList;
import jpanews.jpaproject1.domain.Words.Word;
import jpanews.jpaproject1.repository.CustomWordListToWordRepositoryImpl;
import jpanews.jpaproject1.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class SearchController {

    private final articleCrawling articleCrawling;
    private final WordService wordService;
    private final WordListService wordListService;
    private final MemberService memberService;

    @GetMapping("/searchURL")
    public String searchURL(Model model, String URL) throws Exception {
        System.out.println(URL);
        crawlingDto dto = articleCrawling.crawl(URL);
//        System.out.println(dto.getTitle());
//        System.out.println(dto.getSubTitle());
//        System.out.println(dto.getArticleBody());

        ArrayList<String> allWordsOfArticle = new ArrayList<>();
        allWordsOfArticle.addAll(dto.getTitle_each());
        allWordsOfArticle.addAll(dto.getSubTitle_each());
        allWordsOfArticle.addAll(dto.getArticleBody());

        HashMap<String, ArrayList<wordDto>> wordData = new HashMap<>();
        for (String word: allWordsOfArticle) {
            word = word.toLowerCase().replaceAll("[^A-Za-z0-9-]","");
            if(word.length()>=2){
                ArrayList<wordDto> wordDtoList = new ArrayList<>();
                if(!wordData.containsKey(word)){
                    List<Word> resultWords = wordService.findWithStringOnlyExactResults(word);
                    if(resultWords.size()==0){
                        wordDto wordDto = new wordDto();
                        wordDto.setWordName("No result found for this word.");
                        wordDto.setWordMeaning("There is no result.");
                        wordDto.setWordLanguage("English");
                        wordDtoList.add(wordDto);
                    } else{
                        int count = 0;
                        for(Word word2: resultWords){
                            if(count<2){
                                wordDto wordDto = new wordDto();
                                wordDto.setId(word2.getId());
                                wordDto.setWordName(word2.getName());
                                wordDto.setWordClass(String.valueOf(word2.getWordClass()));
                                wordDto.setWordMeaning(word2.getMeaning());
                                wordDto.setWordLanguage("English");
                                wordDtoList.add(wordDto);
                                count++;
                            }
                        }
                    }
                    wordData.put(word, wordDtoList);
                }
            }
        }

        model.addAttribute("ArticleTitle", dto.getTitle_each());
        model.addAttribute("ArticleSubtitle", dto.getSubTitle_each());
        model.addAttribute("ArticleBodyList", dto.getArticleBody());
        model.addAttribute("wordData", wordData);
        getWordlists(model);
        //각단어마다 단어 찾아서 wordDto생성, arraylist of wordDto를넘기고
        //
        return "articleSearch";
    }

    private void getWordlists(Model model) {
        getWordLists(model, memberService, wordListService);
    }

    static void getWordLists(Model model, MemberService memberService, WordListService wordListService) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("username: " + authentication.getName()); // == username
        if (!authentication.getName().equals("anonymousUser")) {
            Member member = memberService.findByUsername(authentication.getName()).get(0);
            // System.out.println("member's name: " + member.getUsername());
            // System.out.println("member's role: " + member.getRole());
            // System.out.println("member's id: " + member.getId());
            List<WordList> wordListByMember = wordListService.findAllWordListByMember(member.getId());
            for (WordList wordList : wordListByMember) {
                wordList.updateMemorizedStatus();
            }
            model.addAttribute("member", member);
            model.addAttribute("wordLists", wordListByMember);
        }
    }
}
