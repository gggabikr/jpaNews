package jpanews.jpaproject1.controller;

import jpanews.jpaproject1.domain.MemberRole;
import jpanews.jpaproject1.service.MemberInfoDto;
import jpanews.jpaproject1.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/sign-up")
    public String signUp(Model model) throws Exception {
        model.addAttribute("MemberInfoDto", new MemberInfoDto());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@Valid MemberInfoDto infoDto, BindingResult result, Model model) throws Exception { // 회원 추가
        if(result.hasErrors()){
            return "/sign-up";
        }
        System.out.println("MemberRole value: "+MemberRole.valueOf(infoDto.getRole()));
        try{memberService.
                join(infoDto.getUsername(),
                        infoDto.getPassword(),
                        MemberRole.valueOf(infoDto.getRole()));}
        catch (Exception e) {
            return "redirect:/ErrorPage";
        }
        return "redirect:/sign-in";
    }

    @GetMapping(value = "/sign-in")
    public String signIn(){
        return "sign-in";
    }

    @GetMapping(value = "/sign-out")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler()
                .logout(request, response,
                        SecurityContextHolder.getContext()
                                .getAuthentication());
        return "redirect:/sign-in";
    }

    @GetMapping(value = "/ErrorPage")
    public String errorPage(){
        return "/errorPage";
    }
}