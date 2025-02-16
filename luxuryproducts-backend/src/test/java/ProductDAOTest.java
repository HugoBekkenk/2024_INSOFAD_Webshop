import com.example.gamewebshop.dao.*;
import com.example.gamewebshop.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;


@ExtendWith(MockitoExtension.class)
public class ProductDAOTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private VariantRepository variantRepository;

    @Mock
    private OptionsRepository optionsRepository;

    private ProductDAO SUT;

    @BeforeEach
    public void setup() {
        this.SUT = new ProductDAO(this.productRepository, this.categoryRepository, this.variantRepository, this.optionsRepository);
    }

    @Test
    public void testGetAllProducts_Success() {
        List<Product> dummyProducts = new ArrayList<>();
        when(this.productRepository.findAll()).thenReturn(dummyProducts);

        List<Product> actualProducts = this.SUT.getAllProducts();

        assertThat(actualProducts, is(dummyProducts));
        Mockito.verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductById_Success() {
        Product dummyProduct = new Product();
        when(this.productRepository.findById(anyLong())).thenReturn(Optional.of(dummyProduct));
        int dummyId = 1;

        Product actualProduct = this.SUT.getProductById(dummyId);

        assertThat(actualProduct, is(dummyProduct));
        Mockito.verify(productRepository, times(1)).findById(anyLong());

    }

    @Test
    public void testGetProductById_NotFoundException() {
        when(this.productRepository.findById(anyLong())).thenReturn(Optional.empty());
        int dummyId = 1;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> this.SUT.getProductById(dummyId));

        assertThat(exception.getMessage(), is("404 NOT_FOUND \"No product found with that id\""));
    }
}
