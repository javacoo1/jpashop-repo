package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.reopsitory.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 会員登録() throws Exception {
        //given
        Member member = new Member();
        member.setName("javacoo");

        //when
        Long saveId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findById(saveId).get());
    }

    @Test
    public void 重複会員除外() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("javacuu");

        Member member2 = new Member();
        member2.setName("javacuu");

        //when
        memberService.join(member1);
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });
    }
}