package com.example.gamewebshop.services;
import com.example.gamewebshop.dao.ProductRepository;
import com.example.gamewebshop.dao.PromoCodeRepository;
import com.example.gamewebshop.models.PromoCode;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;
    private final ProductRepository productRepository;
    public PromoCodeService(PromoCodeRepository promoCodeRepository, ProductRepository productRepository) {
        this.promoCodeRepository = promoCodeRepository;
        this.productRepository = productRepository;
    }
    public List<PromoCode> retrieveAllPromoCodes() {
        return promoCodeRepository.findAll();
    }
    public PromoCode createPromoCode(PromoCode promoCode) {
        return promoCodeRepository.save(promoCode);
    }
    public PromoCode modifyPromoCode(Long id, PromoCode promoCodeDetails) {
        Optional<PromoCode> promoCodeOptional = promoCodeRepository.findById(id);
        if (promoCodeOptional.isPresent()) {
            PromoCode existingPromoCode = promoCodeOptional.get();
            existingPromoCode.setCode(promoCodeDetails.getCode());
            existingPromoCode.setDiscount(promoCodeDetails.getDiscount());
            existingPromoCode.setExpiryDate(promoCodeDetails.getExpiryDate());
            existingPromoCode.setMaxUsageCount(promoCodeDetails.getMaxUsageCount());
            return promoCodeRepository.save(existingPromoCode);
        } else {
            return null;
        }
    }
    public void removePromoCode(Long id) {
        promoCodeRepository.deleteById(id);
    }
    public boolean checkPromoCodeExistsById(Long id) {
        return promoCodeRepository.existsById(id);
    }
    public Optional<PromoCode> fetchPromoCodeByCode(String code) {
        return promoCodeRepository.findByCode(code);
    }
    public Optional<PromoCode> fetchPromoCodeById(Long id) {
        return promoCodeRepository.findById(id);
    }
    public boolean validatePromoCode(String code) {
        Optional<PromoCode> promoCodeOptional = fetchPromoCodeByCode(code);
        return promoCodeOptional.isPresent() && promoCodeOptional.get().getExpiryDate().isAfter(LocalDateTime.now()) && promoCodeOptional.get().getMaxUsageCount() > 0;
    }
}
 