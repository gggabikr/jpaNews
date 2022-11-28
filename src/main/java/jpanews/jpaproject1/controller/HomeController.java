package jpanews.jpaproject1.controller;

import jpanews.jpaproject1.domain.Member;
import jpanews.jpaproject1.service.MemberService;
import jpanews.jpaproject1.service.WordListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final WordListService wordListService;
    private final MemberService memberService;


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
//        wordListService.findAllWordListByMember();
//        model.addAttribute("wordList", );
        return "wordListPage";
    }

}