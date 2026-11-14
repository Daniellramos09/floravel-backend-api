package br.com.floravel.floravel.domain.onboarding;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OnboardingMaeRepository extends JpaRepository<OnboardingMae, String> {
    Optional<OnboardingMae> findByMaeId(String maeId);
    boolean existsByMaeId(String maeId);
}
