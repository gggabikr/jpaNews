package jpanews.jpaproject1.controller;

import jpanews.jpaproject1.service.WordListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final WordListService wordListService;
    //아래와 같이 로거를 뽑을 수 있지만 롬복을 쓰면 어노테이션으로 가능하다.
    //Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/")
    public String home(){
        log.info("home controller");
        return "home";
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/user/wordList")
    public String ToWordList(Model model){
        wordListService.findAllWordListByMember()
        model.addAttribute("wordList", );
        return "wordListPage";
    }

}