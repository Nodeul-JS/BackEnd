package com.group.commitapp.service;

import com.group.commitapp.repository.BadgeHistoryRepository;
import com.group.commitapp.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BadgeService {
private final BadgeHistoryRepository badgeHistoryRepository;
private final BadgeRepository badgeRepository;


}
