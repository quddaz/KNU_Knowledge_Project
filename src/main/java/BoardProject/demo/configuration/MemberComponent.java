package BoardProject.demo.configuration;

import BoardProject.demo.domain.Member;
import BoardProject.demo.service.MemberService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MemberComponent implements UserDetailsService {
    private final MemberService memberService;

    public MemberComponent(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberService.findMemberById(id);

        if (member == null) {
            throw new UsernameNotFoundException("없는 회원입니다");
        }

        return User.builder()
                .username(member.getId())
                .password(member.getPw())
                .build();
    }
}
