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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public String testWords(
            @PathVariable Long wordListId,
            @RequestParam @Nullable Long[] checkedWords,
            Model model,
            RedirectAttributes redirectAttributes) throws Exception {
        if(checkedWords != null && checkedWords.length>0){
            //test selected words
            ArrayList<WordListToWord> wlws = new ArrayList<>();
            for(Long wlwId: checkedWords){
                wlws.add(wlwRepository.findByWordIdAndWordListIdWithWordData(wordListId, wlwId).get(0));
            }
            //words to be tested
            WordListToWord[] wlwList = wlws.toArray(new WordListToWord[0]);

            //answer List for each words in above list
            ArrayList<List<String>> answerLists = new ArrayList<>();
            for(WordListToWord wlw: wlwList){
                List<String> answerList = wordListService.makeAnswerList(wlw);
                answerLists.add(answerList);
            }

//            System.out.println("WLWS: "+ wlws);
//            for (List<String> answerList:answerLists) {
//                for (String answer:answerList) {
//                    System.out.println("answers: " + answer);
//                }
//            }

            Map<WordListToWord, List<String>> questions = IntStream.range(0, wlws.size()).boxed()
//                    .collect(Collectors.toMap(i -> wlws.get(i), i -> answerLists.get(i)));
                    .collect(Collectors.toMap(wlws::get, answerLists::get));

            redirectAttributes.addFlashAttribute("questionMap", questions);
            //==테스트를 해봅시당 ! ==//
            model.addAttribute("questions", questions);

//            wordListService.testWords(wlwList);
            return "testWordPage";
        } else{
            //test random words
            model.addAttribute("wordListId", wordListId);
            System.out.println("checkedWords is null");
        }
        return "testWordPage";
    }

    @GetMapping("/user/markTest")
    public String testResultPage(
            @RequestParam Map<String, String> requestParams,
            @RequestBody @ModelAttribute("questionMap") Map<WordListToWord, List<String>> questions){
        //이 외에도 테스트 페이지에서 만들어진 answerlist or 단어+선택지 맵 데이터 그대로 넘겨 받기...
        //아니면 그외에 랜덤으로 만들어진 문제지들을 그대로 가져올 다른 방법이 있을까?
        //혹은 아예 가져오는 값을 문제와 답이 아니라, 정답과 사용자의 답으로 가져와버리는건 어떨까.
        //그런 경우에는 결과 반영은 쉬울지 몰라도 테스트 결과 페이지를 보이기가 쉽지않을것이다.
        //그렇다면 테스트 결과 페이지는 따로 만들지않고 그냥 서밋을 누르면 자바스크립트로 정답과 오답을 표시하게 한뒤에
        //확인버튼을 누르면 그때 결과가 반영되게...? 으음. 어렵다. 
        for(String key: requestParams.keySet()){
            System.out.println(key);
            System.out.println(requestParams.get(key));
        }

        for(WordListToWord key: questions.keySet()){
            System.out.println(key.getWord().getName());
        }

        for(List<String> answerList: questions.values()){
            System.out.println(Arrays.toString(answerList.toArray()));
        }
//        for(int i=0; i<requestParams.size(); i++){
//            requestParams.keySet().
//        }
        return "testResultPage";
    }
}