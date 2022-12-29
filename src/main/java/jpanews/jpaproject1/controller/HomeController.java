package jpanews.jpaproject1.controller;

import jpanews.jpaproject1.domain.Member;
import jpanews.jpaproject1.domain.WordClass;
//import jpanews.jpaproject1.domain.Words.EngWord;
//import jpanews.jpaproject1.domain.Words.KorWord;
import jpanews.jpaproject1.domain.WordList;
import jpanews.jpaproject1.domain.WordListToWord;
import jpanews.jpaproject1.domain.Words.Word;
import jpanews.jpaproject1.repository.CustomWordListToWordRepositoryImpl;
import jpanews.jpaproject1.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final WordListService wordListService;
    private final MemberService memberService;
    private final WordService wordService;
    private final CustomWordListToWordRepositoryImpl wlwRepository;



    //아래와 같이 로거를 뽑을 수 있지만 롬복을 쓰면 어노테이션으로 가능하다.
    //Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/")
    public String home(){
        log.info("home controller");
        return "home";
    }

//    //유저 이름 가져오는거 테스트용
//    @RequestMapping(value = "/username", method = RequestMethod.GET)
//    @ResponseBody
//    public String currentUserName(Principal principal) {
//        return principal.getName();
//    }

    @GetMapping("/user/wordList")
//        public String ToWordList(Model model, @AuthenticationPrincipal Principal principal){
        public String ToWordList(Model model){
        getWordlists(model);
        return "wordListPage";
    }

    @GetMapping("/user/inWordList")
    public String inWordList(@RequestParam("wordListSelect") Long wordListId, Model model){
        System.out.println(wordListId);
        ArrayList<Word> words = new ArrayList<>();
//        List<WordListToWord> allByWordList = wlwRepository.findAllByWordList(wordListId);
        List<WordListToWord> allByWordList = wlwRepository.findAllByWordListWithWordData(wordListId);
        for(WordListToWord wlw: allByWordList){
            words.add(wlw.getWord());
        }
        model.addAttribute("WLWs", allByWordList);
        model.addAttribute("words", words);
        model.addAttribute("wordListId", wordListId);
        return "insideOfWordlist";
    }

    @GetMapping("/addNewWordList")
    public String AddNewWordList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberService.findByUsername(authentication.getName()).get(0);
        wordListService.createWordList(member.getId());
        return "redirect:/user/wordList";
    }

    @GetMapping("/deleteWordList")
    public String DeleteWordList(@RequestParam("wordListSelect") Long wordListId){
        System.out.println(wordListId);
        wordListService.deleteWordList(wordListId);
        return "redirect:/user/wordList";
    }

    @GetMapping("/admin")
    public String AdminPage(){
        return "admin";
    }

    @GetMapping("/admin/AddWordsToDB")
    public String AddWordToDB(){
        return "AddWordsToDB";
    }

    @GetMapping("/admin/AddSingleWordToDB")
    public String AddSingleWordPage(Model model){
        model.addAttribute("word", new wordDto());
        return "AddSingleWordToDB";
    }

    @PostMapping("/admin/AddSingleWordToDB")
    public String AddSingleWordToDB(@Valid wordDto Dto, BindingResult result) throws Exception {
        if(result.hasErrors()){
            for(ObjectError error:result.getAllErrors()){
                System.out.println(error.getDefaultMessage());
            }
            return "/errorPage";
        }
            Word word = new Word();
//        } else {
//            word = new EngWord();
//        }
        word.setName(Dto.getWordName());
        word.setWordClass(WordClass.valueOf(Dto.getWordClass()));
        word.setMeaning(Dto.getWordMeaning());
        word.setLanguage(Dto.getWordLanguage());
        wordService.saveWordToDb(word);


        System.out.println("A word is added to DB: " + Dto.getWordName() + ", "
                + Dto.getWordClass() + ", "
                + Dto.getWordMeaning() + ", "
                + Dto.getWordLanguage());

        return "/admin";
    }


    @Resource(name= "fileService")
    FileService fileService;

    @PostMapping("/admin/AddWordsToDB")
    public String uploadFile(MultipartHttpServletRequest multiRequest, Model model){
        try{
            String savedFilePathAndName = fileService.uploadFile(multiRequest);
            wordService.read(savedFilePathAndName);
//            fileService.uploadDataFromFile(savedFilePathAndName);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
            return "errorPage";
        }
        return "admin";
    }

    @GetMapping("/searchWord")
    public String searchWordPage(){
        return "searchWord";
    }

    @PostMapping("/searchWord")
    public String searchWordResult(Model model, @RequestParam String searchBar) {
        String result = null;
        try {
            System.out.println("result is: " + searchBar);
            List<Word> searchWithString = wordService.findWithString(searchBar);
//            for(Word word: searchWithString){
//                System.out.println(word.getName() + "//" + word.getWordClass() + "//" + word.getMeaning());
//            }
            model.addAttribute("result", searchWithString);
            getWordlists(model);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
            result = "errorPage";
        }
        if (result == null) {
            result = "searchWordResult";
        }
        return result;
    }

    private void getWordlists(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("username: "+ authentication.getName()); // == username
        if(!authentication.getName().equals("anonymousUser")){
            Member member = memberService.findByUsername(authentication.getName()).get(0);
//            System.out.println("member's name: " + member.getUsername());
//            System.out.println("member's role: " + member.getRole());
//            System.out.println("member's id: " + member.getId());
            List<WordList> wordListByMember = wordListService.findAllWordListByMember(member.getId());
            for (WordList wordList:wordListByMember) {
                wordList.updateMemorizedStatus();
            }
            model.addAttribute("member", member);
            model.addAttribute("wordLists", wordListByMember);

        }
    }

    @GetMapping("/admin/modifyWord/{wordId}")
    public String modifyWordPage(@PathVariable("wordId") Long wordId, Model model){
        Word word = wordService.findById(wordId);
        wordDto Dto = new wordDto();
        Dto.setId(word.getId());
        Dto.setWordName(word.getName());
        Dto.setWordClass(String.valueOf(word.getWordClass()));
        Dto.setWordMeaning(word.getMeaning());
        Dto.setWordLanguage(word.getLanguage());
        model.addAttribute("Dto", Dto);
        return "modifyWord";
    }

    @PostMapping("/admin/modifyWord/{wordId}")
    public String updateWord(@PathVariable Long wordId, @ModelAttribute("Dto") wordDto Dto){
        System.out.println(Dto.getId() + ", " + Dto.getWordName());
        Word word = new Word();
        word.setId(wordId);
        word.setName(Dto.getWordName());
        word.setWordClass(WordClass.valueOf(Dto.getWordClass()));
        word.setMeaning(Dto.getWordMeaning());
        word.setLanguage(Dto.getWordLanguage());
        wordService.updateWord(word);
        return "redirect:/searchWord";
    }

    @GetMapping("/addWordToList/{wordId}")
    public String addWordToList(@PathVariable Long wordId, @RequestParam Long listSelect){
        System.out.println("word Id: " + wordId);
        System.out.println("list Id: " + listSelect);
        Word word = wordService.findById(wordId);
        wordListService.addWordsToWordList(listSelect, word);
        return "redirect:/user/wordList";
    }

    @GetMapping("user/deleteWLWsFromList/{wordListId}")
    public String deleteWLWsFromList(@PathVariable Long wordListId, @RequestParam Long[] checkedWords, Model model){

        try{
        System.out.println("wordListId: " + wordListId);
//        for(Long id:checkedWords){
//            System.out.println(id);
//            wlwRepository.deleteWlw(wordListId, id);}
            wordListService.deleteWordsFromWordListWithIds(wordListId, checkedWords);
        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
            return "errorPage";
        }
        return "redirect:/user/inWordList?wordListSelect=" + wordListId;
    }

    @GetMapping("/user/resetTestResult/{wlwId}")
    public String resetTestResults(@PathVariable Long wlwId, Model model) {
        Long wordListId;
        try {
//            WordListToWord wordListToWord = wlwRepository.findOne(wlwId);
            wordListId = wordListService.resetTestResult(wlwId);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
            return "errorPage";
        }
        return "redirect:/user/inWordList?wordListSelect=" + wordListId;
    }

    @GetMapping("/user/toggleStatus/{wlwId}")
    public String toggleStatus(@PathVariable Long wlwId){
        Long wordListId = wordListService.toggleStatus(wlwId);
        return "redirect:/user/inWordList?wordListSelect="+wordListId;
    }

    @GetMapping("/user/testWords/{wordListId}")
    public String testWords(@PathVariable Long wordListId, @RequestParam @Nullable Long[] checkedWords, Model model) throws Exception {
        if(checkedWords != null && checkedWords.length>0){
            //test selected words
            ArrayList<WordListToWord> wlws = new ArrayList<>();
            for(Long wlwId: checkedWords){
                wlws.add(wlwRepository.findByWordIdAndWordListId(wordListId, wlwId).get(0));
            }
            WordListToWord[] wlwList = wlws.toArray(new WordListToWord[0]);

            //테스트를 해봅시당 !
            wordListService.testWords(wordListId, wlwList);
        } else{
            //test random words
            System.out.println("checkedWords is null");

        }
        return "/user/testWords/";
    }

}