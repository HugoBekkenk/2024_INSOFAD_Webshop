package com.example.gamewebshop.dao;

import com.example.gamewebshop.dto.OptionDTO;
import com.example.gamewebshop.dto.VariantDTO;
import com.example.gamewebshop.models.Options;
import com.example.gamewebshop.models.Product;
import com.example.gamewebshop.models.Variant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class VariantDAO {
    private final VariantRepository variantRepository;
    private final ProductRepository productRepository;
    private final OptionsRepository optionsRepository;

    public VariantDAO(VariantRepository variantRepository, ProductRepository productRepository, OptionsRepository optionsRepository) {
        this.variantRepository = variantRepository;
        this.productRepository = productRepository;
        this.optionsRepository = optionsRepository;
    }

    public void createVariant(VariantDTO variantDTO) {
        Optional<Product> optionalProduct = this.productRepository.findById(variantDTO.getProductId());
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            Variant variant = new Variant(variantDTO.getName(), product);
            this.variantRepository.save(variant);
            for (OptionDTO optionDTO: variantDTO.getOptions()){
                Options option = new Options(optionDTO.name, optionDTO.priceAdded, variant);
                this.optionsRepository.save(option);
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No products found with that product id"
            );
        }
    }

    public void deleteVariantById(Long id) {
        Optional<Variant> optionalvariant = this.variantRepository.findById(id);
        if (optionalvariant.isPresent()){
            Variant variant = optionalvariant.get();
            for (Options option: variant.getOptions()){
                this.optionsRepository.deleteById(option.getId());
            }
            this.variantRepository.deleteById(variant.getId());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No variant found with that id"
            );
        }
    }
}
