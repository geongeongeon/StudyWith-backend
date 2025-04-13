package com.studywith.api.domain.member.service;

import com.studywith.api.domain.member.dto.MemberCreateDTO;
import com.studywith.api.domain.member.entity.Member;
import com.studywith.api.domain.member.enums.AccountType;
import com.studywith.api.domain.member.mapper.MemberMapper;
import com.studywith.api.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Transactional
    public MemberCreateDTO createMember(MemberCreateDTO memberCreateDTO, String loginId, String email, AccountType accountType) {
        if (isDuplicateNickname(memberCreateDTO.getNickname())) {
            throw new DataIntegrityViolationException("이미 사용 중인 별명입니다.");
        }

        setDefaultProfileImage(memberCreateDTO);
        setDefaultBio(memberCreateDTO);

        Member member = memberMapper.toEntity(memberCreateDTO, loginId, email, accountType);

        return memberMapper.toCreateDTO(memberRepository.save(member));
    }

    public boolean isDuplicateNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    private void setDefaultProfileImage(MemberCreateDTO memberCreateDTO) {
        if (memberCreateDTO.getProfileImage() == null) {
            if ("M".equalsIgnoreCase(memberCreateDTO.getGender())) {
                memberCreateDTO.setProfileImage("/images/male.png");
            } else if ("F".equalsIgnoreCase(memberCreateDTO.getGender())) {
                memberCreateDTO.setProfileImage("/images/female.png");
            }
        }
    }

    private void setDefaultBio(MemberCreateDTO memberCreateDTO) {
        if (memberCreateDTO.getBio() == null) {
            memberCreateDTO.setBio("안녕하세요. 만나서 반가워요!");
        }
    }

}
