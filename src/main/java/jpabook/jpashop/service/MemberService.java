package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.reopsitory.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //会員登録
    @Transactional
    public Long join(Member member) {

        validateDuplicateMember(member); //重複会員検証
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("もう使用されているIDです。");
        }
    }

    //会員照会
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //一件のみ照会
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
