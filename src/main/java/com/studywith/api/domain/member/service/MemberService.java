package com.studywith.api.domain.member.service;

import com.studywith.api.domain.member.dto.MemberCreateDTO;
import com.studywith.api.domain.member.dto.MemberDetailDTO;
import com.studywith.api.domain.member.dto.MemberSummaryDTO;
import com.studywith.api.domain.member.dto.MemberNicknameDTO;
import com.studywith.api.domain.member.entity.Member;
import com.studywith.api.domain.member.enums.AccountType;
import com.studywith.api.domain.member.mapper.MemberMapper;
import com.studywith.api.domain.member.repository.MemberRepository;
import com.studywith.api.global.exception.NicknameAlreadyInUseException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Transactional
    public MemberCreateDTO createMember(MemberCreateDTO memberCreateDTO, String loginId, String email, String accountType) {
        setDefaultValues(memberCreateDTO);

        Member member = memberMapper.toCreateEntity(memberCreateDTO, loginId, email, AccountType.valueOf(accountType));

        return memberMapper.toCreateDTO(memberRepository.save(member));
    }

    public MemberNicknameDTO isNicknameExists(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new NicknameAlreadyInUseException("이미 사용 중인 별명입니다.");
        }

        return memberMapper.toNicknameDTO(nickname);
    }

    public MemberDetailDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        return memberMapper.toDetailDTO(member);
    }

    public List<MemberSummaryDTO> getMembers() {
        List<Member> members = memberRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        return members.stream()
                .map(memberMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    private void setDefaultValues(MemberCreateDTO memberCreateDTO) {
        if (memberCreateDTO.getProfileImage() == null) {
            if ("M".equalsIgnoreCase(memberCreateDTO.getGender())) {
                memberCreateDTO.setProfileImage("/images/profile/default/male.png");
            } else if ("F".equalsIgnoreCase(memberCreateDTO.getGender())) {
                memberCreateDTO.setProfileImage("/images/profile/default/female.png");
            }
        }

        if (memberCreateDTO.getBio() == null) {
            memberCreateDTO.setBio("안녕하세요. 만나서 반가워요!");
        }
    }

}
