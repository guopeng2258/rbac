package com.example.rbac.infrastructure.persistence;

import com.example.rbac.core.domain.model.user.User;
import com.example.rbac.core.domain.repository.UserRepository;
import com.example.rbac.infrastructure.persistence.jpa.UserJpaRepository;
import com.example.rbac.infrastructure.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户仓储实现
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;
    
    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(userMapper::toDomain);
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username).map(userMapper::toDomain);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(userMapper::toDomain);
    }
    
    @Override
    public List<User> findByTenantId(Long tenantId) {
        return userJpaRepository.findByTenantId(tenantId).stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public User save(User user) {
        var entity = userMapper.toEntity(user);
        if (entity.getId() == null) {
            entity.setCreatedTime(LocalDateTime.now());
        }
        entity.setModifiedTime(LocalDateTime.now());
        var savedEntity = userJpaRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }
    
    @Override
    public void delete(Long id) {
        userJpaRepository.deleteById(id);
    }
}
