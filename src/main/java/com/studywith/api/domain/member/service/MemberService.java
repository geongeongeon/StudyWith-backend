package com.studywith.api.domain.member.service;

import com.studywith.api.domain.member.dto.*;
import com.studywith.api.domain.member.entity.Member;
import com.studywith.api.domain.member.enums.AccountType;
import com.studywith.api.domain.member.exception.MemberNicknameAlreadyInUseException;
import com.studywith.api.domain.member.exception.MemberNoChangesException;
import com.studywith.api.domain.member.exception.MemberNotFoundException;
import com.studywith.api.domain.member.exception.MemberTempNotFoundException;
import com.studywith.api.domain.member.mapper.MemberMapper;
import com.studywith.api.domain.member.repository.MemberRepository;
import com.studywith.api.global.external.OAuth2UserInfo;
import com.studywith.api.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final RedisService redisService;

    @Transactional
    public MemberCreateDTO createMember(MemberCreateDTO memberCreateDTO, String uuid) {
        setDefaultValues(memberCreateDTO);

        OAuth2UserInfo info = redisService.getUserInfo(uuid);
        if (info == null) {
            throw new MemberTempNotFoundException("임시 회원 정보가 만료되었습니다.");
        }
        Member member = memberMapper.toCreateEntity(memberCreateDTO, info.getLoginId(), info.getEmail(), AccountType.valueOf(info.getAccountType()));
        redisService.deleteUserInfo(uuid);

        return memberMapper.toCreateDTO(memberRepository.save(member));
    }

    public MemberNicknameDTO isNicknameExists(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new MemberNicknameAlreadyInUseException("이미 사용 중인 별명입니다.");
        }

        return memberMapper.toNicknameDTO(nickname);
    }

    @Cacheable(value = "member", key = "#id")
    public MemberDetailDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        return memberMapper.toDetailDTO(member);
    }

    public Optional<Member> getMemberByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    public List<MemberSummaryDTO> getMembers() {
        List<Member> members = memberRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        return members.stream()
                .map(memberMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "member", key = "#id")
    public MemberUpdateDTO updateMember(Long id, MemberUpdateDTO memberUpdateDTO) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        if (!updateFieldsIfChanged(member, memberUpdateDTO)) {
            throw new MemberNoChangesException("변경된 정보가 없습니다.");
        }

        return memberMapper.toUpdateDTO(memberRepository.save(member));
    }

    @Transactional
    @CacheEvict(value = "member", key = "#id")
    public void deleteMember(Long id) {
        memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        memberRepository.deleteById(id);
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

    private boolean updateFieldsIfChanged(Member member, MemberUpdateDTO memberUpdateDTO) {
        boolean isChanged = false;

        if (!member.getNickname().equals(memberUpdateDTO.getNickname())) {
            member.setNickname(memberUpdateDTO.getNickname());
            isChanged = true;
        }

        if (!member.getRegion().equals(memberUpdateDTO.getRegion())) {
            member.setRegion(memberUpdateDTO.getRegion());
            isChanged = true;
        }

        if (!member.getProfileImage().equals(memberUpdateDTO.getProfileImage())) {
            member.setProfileImage(memberUpdateDTO.getProfileImage());
            isChanged = true;
        }

        if (!member.getBio().equals(memberUpdateDTO.getBio())) {
            member.setBio(memberUpdateDTO.getBio());
            isChanged = true;
        }

        return isChanged;
    }

}
