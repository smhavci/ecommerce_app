package com.ecommerce.app.init;

import com.ecommerce.app.dto.CartDTO;
import com.ecommerce.app.dto.OrderDTO;
import com.ecommerce.app.dto.ProductDTO;
import com.ecommerce.app.dto.UserDTO;
import com.ecommerce.app.model.*;
import com.ecommerce.app.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ecommerce.app.dto.OrderRequest;
import com.ecommerce.app.dto.OrderItemRequest;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor // Constructor'ı otomatik generate ediyor (Lombok)
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final PaymentService paymentService;
    private final ReviewService reviewService;
    
    private final PasswordEncoder passwordEncoder;



    @Override
    public void run(String... args) throws Exception {
        initializeUsers();
        initializeCategories();
        initializeProducts();
        initializeCart();
        initializeOrder();
        initializePayment();
        initializeReview();
    }

    private void initializeUsers() {
        // ✅ Admin kullanıcı
        if (!userService.userExistsByEmail("shadowrisingadmin@example.com")) {
            User admin = new User();
            admin.setUsername("shadowrisingadmin");
            admin.setEmail("shadowrisingadmin@example.com");
            admin.setPassword(passwordEncoder.encode("shadowrisingadmin"));
            admin.setRole(Role.ROLE_ADMIN);
            admin.setBalance(BigDecimal.valueOf(75000));
            userService.saveUser(admin);
        } else {
            User admin = userService.findByEmail("shadowrisingadmin@example.com");
            admin.setBalance(BigDecimal.valueOf(75000));
            userService.saveUser(admin);
        }
    
        // ✅ Normal kullanıcı
        if (!userService.userExistsByEmail("user@example.com")) {
            User normalUser = new User();
            normalUser.setUsername("user");
            normalUser.setEmail("user@example.com");
            normalUser.setPassword(passwordEncoder.encode("user123"));
            normalUser.setRole(Role.ROLE_USER);
            normalUser.setBalance(BigDecimal.valueOf(75000));
            userService.saveUser(normalUser);
        } else {
            User user = userService.findByEmail("user@example.com");
            user.setBalance(BigDecimal.valueOf(75000));
            userService.saveUser(user);
        }
    
        // ✅ Satıcı kullanıcı
        if (!userService.userExistsByEmail("seller@example.com")) {
            User seller = new User();
            seller.setUsername("semihseller");
            seller.setEmail("seller@example.com");
            seller.setPassword(passwordEncoder.encode("seller123"));
            seller.setRole(Role.ROLE_SELLER);
            seller.setBalance(BigDecimal.valueOf(75000));
            userService.saveUser(seller);
        } else {
            User seller = userService.findByEmail("seller@example.com");
            seller.setBalance(BigDecimal.valueOf(75000));
            userService.saveUser(seller);
        }
    
        System.out.println("Admin, kullanıcı ve satıcı kontrolü tamamlandı.");
    }
    
    
    
    
    

    private void initializeCategories() {
        if (categoryService.getAllCategories().isEmpty()) {
            Category electronics = new Category();
            electronics.setName("Elektronik");
            electronics.setDescription("Elektronik cihazlar ve aksesuarlar");
    
            Category clothing = new Category();
            clothing.setName("Giyim");
            clothing.setDescription("Moda ve giyim ürünleri");
    
            Category books = new Category();
            books.setName("Kitap");
            books.setDescription("Kitaplar ve basılı yayınlar");
    
            Category accessories = new Category();
            accessories.setName("Aksesuar");
            accessories.setDescription("Yan ürünler ve aksesuarlar");
    
            categoryService.saveCategory(electronics);
            categoryService.saveCategory(clothing);
            categoryService.saveCategory(books);
            categoryService.saveCategory(accessories);
    
            System.out.println("Sample kategoriler başarıyla eklendi.");
        } else {
            System.out.println("Kategoriler zaten mevcut, yeniden eklenmedi.");
        }
    }

    private void initializeProducts() {
    if (productService.getAllProducts().isEmpty()) { // Eğer ürün yoksa ekle
            Product laptop = new Product();
            laptop.setName("Laptop");
            laptop.setDescription("Yüksek performanslı dizüstü bilgisayar");
            laptop.setPrice(new BigDecimal("25000"));
            laptop.setStock(10);

            Product tshirt = new Product();
            tshirt.setName("T-Shirt");
            tshirt.setDescription("Rahat pamuklu t-shirt");
            tshirt.setPrice(new BigDecimal("350"));
            tshirt.setStock(50);

            Product novel = new Product();
            novel.setName("Roman Kitap");
            novel.setDescription("Popüler bir roman kitabı");
            novel.setPrice(new BigDecimal("150"));
            novel.setStock(30);

            Product headset = new Product();
            headset.setName("Bluetooth Kulaklık");
            headset.setDescription("Kablosuz bluetooth kulaklık");
            headset.setPrice(new BigDecimal("750"));
            headset.setStock(20);

            productService.saveProduct(laptop);
            productService.saveProduct(tshirt);
            productService.saveProduct(novel);
            productService.saveProduct(headset);

            System.out.println("Sample ürünler başarıyla eklendi.");
        } else {
            System.out.println("Ürünler zaten mevcut, yeniden eklenmedi.");
        }
    }

    private void initializeCart() {
    if (cartService.getAllCarts().isEmpty()) {
        // Kullanıcıyı getir
        List<UserDTO> users = userService.getAllUsers();
        UserDTO normalUser = users.stream()
                .filter(u -> "user@example.com".equals(u.getEmail()))
                .findFirst()
                .orElse(null);

        if (normalUser != null) {
            // Sepet oluştur
            Cart cart = new Cart();
            cart.setUser(new User(normalUser.getId())); // sadece ID setliyoruz
            CartDTO savedCart = cartService.saveCart(cart); // kaydediyoruz (DTO döner)

            // Ürünleri getir
            List<ProductDTO> products = productService.getAllProducts();

            ProductDTO laptop = products.stream()
                    .filter(p -> "Laptop".equals(p.getName()))
                    .findFirst()
                    .orElse(null);

            ProductDTO tshirt = products.stream()
                    .filter(p -> "T-Shirt".equals(p.getName()))
                    .findFirst()
                    .orElse(null);

            if (laptop != null && tshirt != null) {
                // 1 adet Laptop CartItem
                CartItem laptopItem = new CartItem();
                laptopItem.setCart(new Cart(savedCart.getId())); // sadece ID
                laptopItem.setProduct(new Product(laptop.getId()));
                laptopItem.setQuantity(1);
                cartItemService.saveCartItem(laptopItem);

                // 2 adet T-Shirt CartItem
                CartItem tshirtItem = new CartItem();
                tshirtItem.setCart(new Cart(savedCart.getId()));
                tshirtItem.setProduct(new Product(tshirt.getId()));
                tshirtItem.setQuantity(2);
                cartItemService.saveCartItem(tshirtItem);

                System.out.println("Sample sepet ve ürünleri başarıyla eklendi.");
            } else {
                System.out.println("Laptop veya T-Shirt ürünü bulunamadı, sepet ürünleri eklenemedi.");
            }
        } else {
            System.out.println("Normal kullanıcı bulunamadı, sepet oluşturulamadı.");
        }
    } else {
        System.out.println("Sepet zaten mevcut, yeniden eklenmedi.");
    }
}


    private void initializeOrder() {
    if (orderService.getAllOrders().isEmpty()) {
        // Kullanıcıyı getir
        List<UserDTO> users = userService.getAllUsers();
        UserDTO normalUser = users.stream()
                .filter(u -> "user@example.com".equals(u.getEmail()))
                .findFirst()
                .orElse(null);

        // Ödeme bilgisini getir
        List<Payment> payments = paymentService.getAllPayments();
        Payment payment = payments.stream().findFirst().orElse(null); // İlk ödeme kaydını alıyoruz

        if (normalUser != null && payment != null) {
            // Ürünleri getir
            List<ProductDTO> products = productService.getAllProducts();

            ProductDTO laptopProduct = products.stream()
                    .filter(p -> "Laptop".equals(p.getName()))
                    .findFirst()
                    .orElse(null);

            ProductDTO tshirtProduct = products.stream()
                    .filter(p -> "T-Shirt".equals(p.getName()))
                    .findFirst()
                    .orElse(null);

            if (laptopProduct != null && tshirtProduct != null) {
                // OrderRequest hazırla
                OrderRequest orderRequest = new OrderRequest();
                orderRequest.setPaymentId(payment.getId());

                List<OrderItemRequest> items = new ArrayList<>();

                // 1 adet laptop
                OrderItemRequest item1 = new OrderItemRequest();
                item1.setProductId(laptopProduct.getId());
                item1.setQuantity(1);
                item1.setPrice(laptopProduct.getPrice().doubleValue());

                // 2 adet T-shirt
                OrderItemRequest item2 = new OrderItemRequest();
                item2.setProductId(tshirtProduct.getId());
                item2.setQuantity(2);
                item2.setPrice(tshirtProduct.getPrice().doubleValue());
                items.add(item1);
                items.add(item2);

                orderRequest.setItems(items);

                // Kullanıcıyı entity olarak ver
                User user = new User(normalUser.getId());

                // Siparişi kaydet
                OrderDTO savedOrderDTO = orderService.saveOrder(orderRequest, user);

                System.out.println("Sample sipariş başarıyla oluşturuldu.");
            } else {
                System.out.println("Laptop veya T-Shirt ürünü bulunamadı, sipariş oluşturulamadı.");
            }
        } else {
            System.out.println("Normal kullanıcı veya ödeme kaydı bulunamadı, sipariş eklenemedi.");
        }
    } else {
        System.out.println("Siparişler zaten mevcut, yeniden eklenmedi.");
    }
}


    private void initializePayment() {
        if (paymentService.getAllPayments().isEmpty()) {
            Payment payment1 = new Payment();
            payment1.setPaymentType("CARD");
            payment1.setPaymentStatus("SUCCESS");
    
            Payment payment2 = new Payment();
            payment2.setPaymentType("PAYPAL");
            payment2.setPaymentStatus("FAILED");
    
            paymentService.savePayment(payment1);
            paymentService.savePayment(payment2);
    
            System.out.println("Sample ödeme kayıtları başarıyla eklendi.");
        } else {
            System.out.println("Ödeme kayıtları zaten mevcut, yeniden eklenmedi.");
        }
    }

    private void initializeReview() {
    if (reviewService.getAllReviews().isEmpty()) {
        // Tüm kullanıcıları getir
        List<UserDTO> users = userService.getAllUsers();
        UserDTO normalUser = users.stream()
                .filter(u -> "user@example.com".equals(u.getEmail()))
                .findFirst()
                .orElse(null);

        // Tüm ürünleri getir
        List<ProductDTO> products = productService.getAllProducts();
        ProductDTO laptopProduct = products.stream()
                .filter(p -> "Laptop".equals(p.getName()))
                .findFirst()
                .orElse(null);

        if (normalUser != null && laptopProduct != null) {
            // Review oluştur
            Review review = new Review();
            review.setUser(new User(normalUser.getId()));  // Sadece ID veriyoruz
            review.setProduct(new Product(laptopProduct.getId()));  // Sadece ID veriyoruz
            review.setRating(5);  // 5 yıldız verdik
            review.setComment("Ürün gerçekten harika, tavsiye ederim!");

            reviewService.saveReview(review);

            System.out.println("Sample yorum başarıyla eklendi.");
        } else {
            System.out.println("Normal kullanıcı veya Laptop ürünü bulunamadı, yorum eklenemedi.");
        }
        } else {
            System.out.println("Yorumlar zaten mevcut, yeniden eklenmedi.");
        }
    }
}
