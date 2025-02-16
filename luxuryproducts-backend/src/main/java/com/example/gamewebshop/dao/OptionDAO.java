package com.example.gamewebshop.dao;

import com.example.gamewebshop.dto.OptionDTO;
import com.example.gamewebshop.models.Options;
import com.example.gamewebshop.models.Variant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class OptionDAO {
    private final OptionsRepository optionsRepository;
    private final  VariantRepository variantRepository;

    public OptionDAO(OptionsRepository optionsRepository, VariantRepository variantRepository) {
        this.optionsRepository = optionsRepository;
        this.variantRepository = variantRepository;
    }

    public void createOption(OptionDTO optionDTO) {
        Optional<Variant> optionalVariant = this.variantRepository.findById(optionDTO.variantId);
        if (optionalVariant.isPresent()) {
            Variant variant = optionalVariant.get();
            Options option = new Options(optionDTO.name, optionDTO.priceAdded, variant);
            this.optionsRepository.save(option);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No varariants found with that variant id"
            );
        }
    }

    public void deleteById(Long id) {
        optionsRepository.deleteById(id);
    }
}
