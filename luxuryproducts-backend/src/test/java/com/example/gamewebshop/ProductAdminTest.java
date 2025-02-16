package com.example.gamewebshop;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.gamewebshop.dao.*;
import com.example.gamewebshop.dto.OptionDTO;
import com.example.gamewebshop.dto.ProductDTO;
import com.example.gamewebshop.dto.VariantDTO;
import com.example.gamewebshop.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ProductAdminTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private VariantRepository variantRepository;

    @Mock
    private OptionsRepository optionsRepository;

    private ProductDAO SUTProduct;
    private VariantDAO SUTVariant;
    private OptionDAO SUTOption;
    private OptionDTO validOptionDTO;
    private Variant variant;
    private VariantDTO validVariantDTO;
    private Product product;
    private ProductDTO productDTO;
    private Category category;
    private Variant variant1;
    private Variant variant2;
    private Options option1;
    private Options option2;

    @BeforeEach
    public void setup() {
        this.SUTProduct = new ProductDAO(this.productRepository, this.categoryRepository, this.variantRepository, this.optionsRepository);
        this.SUTVariant = new VariantDAO(this.variantRepository, this.productRepository, this.optionsRepository);
        this.SUTOption = new OptionDAO(this.optionsRepository, this.variantRepository);

        validOptionDTO = new OptionDTO("Option Name", 10, 1L);

        variant = new Variant();
        variant.setId(1L);

        product = new Product();
        product.setId(1L);

        OptionDTO[] options;

        options = new OptionDTO[2];

        options[0] = new OptionDTO("Option 1", 10, 1L);
        options[1] = new OptionDTO("Option 2", 20, 1L);

        validVariantDTO = new VariantDTO("Variant Name", options, 1L);

        category = new Category("Gacha");

        VariantDTO[] variants;
        variants = new VariantDTO[1];
        variants[0] = new VariantDTO("Variant 1", options, 1L);

        productDTO = new ProductDTO("Genshin Impact", "Description", 19.99, "http://image.url", "Specifications", "2020-09-28", "Hoyoverse", 20, category.getName(), "PRODUCT", variants);

        option1 = new Options();
        option1.setId(1L);

        option2 = new Options();
        option2.setId(2L);

        Set<Options> options1 = new HashSet<>();
        options1.add(option1);

        Set<Options> options2 = new HashSet<>();
        options2.add(option2);

        variant1 = new Variant();
        variant1.setId(1L);
        variant1.setOptions(options1);

        variant2 = new Variant();
        variant2.setId(2L);
        variant2.setOptions(options2);

        Set<Variant> variantsSet = new HashSet<>();
        variantsSet.add(variant1);
        variantsSet.add(variant2);

        product.setVariants(variantsSet);
    }

    @Test
    public void testCreateOption_Success() {
        when(variantRepository.findById(validOptionDTO.variantId)).thenReturn(Optional.of(variant));

        SUTOption.createOption(validOptionDTO);

        verify(optionsRepository, times(1)).save(any(Options.class));
    }

    @Test
    public void testCreateOption_VariantNotFound() {
        validOptionDTO.variantId = 0L;
        when(variantRepository.findById(validOptionDTO.variantId)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> {
            SUTOption.createOption(validOptionDTO);
        });
        verify(optionsRepository, never()).save(any(Options.class));
    }

    @Test
    public void testDeleteOptionById_Success() {
        Long optionId = 1L;

        SUTOption.deleteById(optionId);

        verify(optionsRepository, times(1)).deleteById(optionId);
    }

    @Test
    public void testCreateVariant_Success() {
        when(productRepository.findById(validVariantDTO.getProductId())).thenReturn(Optional.of(product));

        SUTVariant.createVariant(validVariantDTO);

        verify(variantRepository, times(1)).save(any(Variant.class));
        verify(optionsRepository, times(2)).save(any(Options.class));
    }

    @Test
    public void testCreateVariant_ProductNotFound() {
        when(productRepository.findById(validVariantDTO.getProductId())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            SUTVariant.createVariant(validVariantDTO);
        });

        verify(variantRepository, never()).save(any(Variant.class));
        verify(optionsRepository, never()).save(any(Options.class));
    }

    @Test
    public void testCreateProduct_Success() {
        when(categoryRepository.findByName(productDTO.category)).thenReturn(Optional.of(category));

        SUTProduct.createProduct(productDTO);

        verify(categoryRepository, never()).save(any(Category.class));
        verify(productRepository, times(1)).save(any(Product.class));
        verify(variantRepository, times(1)).save(any(Variant.class));
        verify(optionsRepository, times(2)).save(any(Options.class));
    }

    @Test
    public void testCreateProduct_CategoryDoesNotExist() {
        when(categoryRepository.findByName(productDTO.category)).thenReturn(Optional.empty());

        SUTProduct.createProduct(productDTO);

        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(productRepository, times(1)).save(any(Product.class));
        verify(variantRepository, times(1)).save(any(Variant.class));
        verify(optionsRepository, times(2)).save(any(Options.class));
    }
}
