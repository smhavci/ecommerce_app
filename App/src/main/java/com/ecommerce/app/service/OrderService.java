package com.ecommerce.app.service;

import com.ecommerce.app.dto.OrderDTO;
import com.ecommerce.app.dto.OrderItemRequest;
import com.ecommerce.app.dto.OrderRequest;
import com.ecommerce.app.dto.UserDTO;
import com.ecommerce.app.enums.OrderStatus;
import com.ecommerce.app.enums.ShippingStatus;
import com.ecommerce.app.exception.ResourceNotFoundException;
import com.ecommerce.app.model.Order;
import com.ecommerce.app.model.OrderItem;
import com.ecommerce.app.model.User;
import com.ecommerce.app.repository.OrderRepository;
import com.ecommerce.app.repository.UserRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    public OrderService(OrderRepository orderRepository, ProductService productService, UserService userService, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // CREATE - Sipariş kaydet (OrderDTO)
public OrderDTO saveOrder(OrderRequest request, User user) {
    double totalAmount = calculateTotal(request.getItems());

    // 💸 Bakiye kontrolü
    if (user.getBalance().doubleValue() < totalAmount) {
        throw new IllegalArgumentException("Bakiyeniz siparişi tamamlamak için yeterli değil.");
    }

    // 🛒 Sipariş nesnesi oluştur
    Order order = new Order();
    order.setUser(user);
    order.setStatus(OrderStatus.PENDING); // ✅ Enum olarak set edildi
    order.setShippingStatus(ShippingStatus.WAITING); // ✅ Kargo süreci başlangıcı
    order.setOrderDate(LocalDateTime.now());
    order.setTotalAmount(totalAmount);

    List<OrderItem> orderItems = request.getItems().stream().map(itemReq -> {
        OrderItem item = new OrderItem();
        item.setProduct(productService.getProductEntityById(itemReq.getProductId()));
        item.setQuantity(itemReq.getQuantity());
        item.setPrice(itemReq.getPrice());
        item.setOrder(order);
        return item;
    }).collect(Collectors.toList());

    order.setOrderItems(orderItems);

    // 💸 Bakiyeden düş
    user.setBalance(user.getBalance().subtract(BigDecimal.valueOf(totalAmount)));
    userService.saveUser(user); // Kullanıcıyı güncelle

    Order saved = orderRepository.save(order);
    return new OrderDTO(saved);
}


    // READ - Tüm siparişleri getir (OrderDTO)
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
        .stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
    }

    // READ - ID ile siparişi getir (OrderDTO)
    public Optional<OrderDTO> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToDTO);
    }

    // DELETE - Siparişi sil
    public void deleteOrder(Long id) {
        boolean exists = orderRepository.existsById(id);
        if (!exists) {
            throw new ResourceNotFoundException("Silinmek istenen sipariş bulunamadı: " + id);
        }

        orderRepository.deleteById(id);
    }

    // UPDATE - Siparişi Güncelle
    public OrderDTO updateOrder(Long id, Order updatedOrder) {
        Order existing = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sipariş bulunamadı: " + id));
    
        existing.setStatus(updatedOrder.getStatus());
        existing.setTotalAmount(updatedOrder.getTotalAmount());
    
        // Eğer payment güncellenecekse:
        if (updatedOrder.getPayment() != null) {
            existing.setPayment(updatedOrder.getPayment());
        }
    
        Order saved = orderRepository.save(existing);
        return convertToDTO(saved);
    }
    

    // KOD TEKRARI OLMAMASI İÇİN CONVERTTODTO METHODU KURULDU!!!
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
    
        if (order.getPayment() != null) {
            dto.setPaymentId(order.getPayment().getId());
        }
    
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
    
        // ✅ User bilgilerini DTO olarak da setle (kullanıcı adı vs. için)
        User user = order.getUser();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setStoreName(user.getStoreName()); // 🎯 Mağaza ismini eklemeyi unutma!
    
        dto.setUser(userDTO);
    
        return dto;
    }
    


    private Double calculateTotal(List<OrderItemRequest> items) {
        return items.stream()
                    .mapToDouble(i -> i.getPrice() * i.getQuantity())
                    .sum();
    }
        
    public List<OrderDTO> getOrdersByUser(User user) {
        return orderRepository.findByUser(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Sipariş bulunamadı: " + orderId));
    
        order.setStatus(OrderStatus.PENDING);
         // Yeni durumu set et
        Order updatedOrder = orderRepository.save(order);
    
        return convertToDTO(updatedOrder); // Entity → DTO dönüşümü
    }
    

    public void cancelOrderBySeller(Long orderId, User seller) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı."));
    
        // Siparişi iptal etme yetkisi: sadece admin ya da o ürünü satan kişi
        if (!seller.getRole().equals("ROLE_ADMIN") && !seller.getId().equals(order.getUser().getId())) {
            throw new RuntimeException("Bu siparişi iptal etmeye yetkiniz yok.");
        }
    
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Sipariş zaten iptal edilmiş.");
        }
    
        // 📦 Siparişi iptal et
        order.setStatus(OrderStatus.CANCELLED);
        order.setShippingStatus(ShippingStatus.CANCELLED);
    
        // 💸 Kullanıcıya para iadesi
        User buyer = order.getUser();
        BigDecimal currentBalance = buyer.getBalance();
        buyer.setBalance(currentBalance.add(BigDecimal.valueOf(order.getTotalAmount())));
        userRepository.save(buyer);
    
        orderRepository.save(order);
    }

    public List<Order> getOrdersBySeller(User seller) {
        return orderRepository.findByUser(seller); // Satıcının siparişlerini getir
    }

}   
