package com.example.gamewebshop.dao;

import com.example.gamewebshop.dto.OptionDTO;
import com.example.gamewebshop.dto.ProductDTO;
import com.example.gamewebshop.dto.VariantDTO;
import com.example.gamewebshop.models.Category;
import com.example.gamewebshop.models.Options;
import com.example.gamewebshop.models.Product;
import com.example.gamewebshop.models.Variant;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class ProductDAO {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final VariantRepository variantRepository;
    private final  OptionsRepository optionsRepository;

    public ProductDAO(ProductRepository repository, CategoryRepository category, VariantRepository variantRepository, OptionsRepository optionsRepository) {
        this.productRepository = repository;
        this.categoryRepository = category;
        this.variantRepository = variantRepository;
        this.optionsRepository = optionsRepository;
    }

    public List<Product> getAllProducts(){
        return this.productRepository.findAll();
    }

    public Product getProductById(long id){
        Optional<Product> product = this.productRepository.findById(id);

        return product.orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No product found with that id"
        ));
    }

    public List<Product> getAllProductsByCategory(long id){
        Optional<List<Product>> products =this.productRepository.findByCategoryId(id);

        if (products.get().isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No products found with that category id"
            );
        }

        return products.get();
    }


    @Transactional
    public void createProduct(ProductDTO productDTO){
        Category category;
        Optional<Category> optionalCategory = categoryRepository.findByName(productDTO.category);
        if (optionalCategory.isPresent()){
            category = optionalCategory.get();
        } else {
            category = new Category(productDTO.category);
            this.categoryRepository.save(category);
        }
        Product product = new Product(productDTO.name, productDTO.description, productDTO.price, productDTO.imgURL, productDTO.specifications, productDTO.releaseDate, productDTO.publisher, productDTO.stock, category, productDTO.type);
        this.productRepository.save(product);
        for (VariantDTO variantDTO: productDTO.variants){
            Variant variant = new Variant(variantDTO.getName(), product);
            this.variantRepository.save(variant);
            for (OptionDTO optionDTO: variantDTO.getOptions()){
                Options option = new Options(optionDTO.name, optionDTO.priceAdded, variant);
                this.optionsRepository.save(option);
            }
        }
    }

    public void updateProduct(ProductDTO productDTO, Long id){
        Optional<Product> product = this.productRepository.findById(id);

        if (product.isPresent()){
            product.get().setName(productDTO.name);
            product.get().setDescription(productDTO.description);
            product.get().setPrice(productDTO.price);
            product.get().setImgURL(productDTO.imgURL);
            product.get().setSpecifications(productDTO.specifications);
            product.get().setReleaseDate(productDTO.releaseDate);
            product.get().setPublisher(productDTO.publisher);
            product.get().setStock(productDTO.stock);
            product.get().setType(productDTO.type);
            this.productRepository.save(product.get());
        }
    }

    public void deleteProductById(Long id) {
        Optional<Product> optionalProduct = this.productRepository.findById(id);
        if (optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            for (Variant variant: product.getVariants()){
                for (Options option: variant.getOptions()){
                    this.optionsRepository.deleteById(option.getId());
                }
                this.variantRepository.deleteById(variant.getId());
            }
            this.productRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No products found with that product id"
            );
        }
    }
}
