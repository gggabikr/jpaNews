package jpanews.jpaproject1.controller;

import jpanews.jpaproject1.service.MemberInfoDto;
import jpanews.jpaproject1.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public String signup(MemberInfoDto infoDto) throws Exception { // 회원 추가
        memberService.join(infoDto.getUsername(), infoDto.getPassword());
        return "redirect:/login";
    }

    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler()
                .logout(request, response,
                        SecurityContextHolder.getContext()
                                .getAuthentication());
        return "redirect:/login";
    }
}