package jpanews.jpaproject1.controller;

import jpanews.jpaproject1.domain.Member;
import jpanews.jpaproject1.domain.WordClass;
import jpanews.jpaproject1.domain.Words.EngWord;
import jpanews.jpaproject1.domain.Words.KorWord;
import jpanews.jpaproject1.domain.Words.Word;
import jpanews.jpaproject1.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final WordListService wordListService;
    private final MemberService memberService;
    private final WordService wordService;


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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName()); // == username
        Member member = memberService.findByUsername(authentication.getName()).get(0);
        System.out.println("member's name: " + member.getUsername());
        System.out.println("member's role: " + member.getRole());
        System.out.println("member's id: " + member.getId());

        model.addAttribute("member", member);
        model.addAttribute("wordLists", wordListService.findAllWordListByMember(member.getId()));
        return "wordListPage";
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
        Word word;
        if (Dto.getWordLanguage().equals("Korean")){
            word = new KorWord();
        } else {
            word = new EngWord();
        }
        word.setName(Dto.getWordName());
        word.setWordClass(WordClass.valueOf(Dto.getWordClass()));
        word.setMeaning(Dto.getWordMeaning());
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
}