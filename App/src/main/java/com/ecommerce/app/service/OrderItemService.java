package com.ecommerce.app.service;

import com.ecommerce.app.dto.OrderItemDTO;
import com.ecommerce.app.exception.ResourceNotFoundException;
import com.ecommerce.app.model.OrderItem;
import com.ecommerce.app.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    // CREATE (DTO)
    public OrderItemDTO saveOrderItem(OrderItem orderItem) {
        OrderItem savedItem = orderItemRepository.save(orderItem);
        return convertToDTO(savedItem);
    }

    // READ - Tüm orderItem'ları getir (DTO)
    public List<OrderItemDTO> getAllOrderItems() {
        return orderItemRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // READ - ID ile orderItem getir (DTO)
    public Optional<OrderItemDTO> getOrderItemById(Long id) {
        return orderItemRepository.findById(id)
                .map(this::convertToDTO);
    }

    // DELETE - OrderItem sil
    public void deleteOrderItem(Long id) {
        orderItemRepository.deleteById(id);
    }

    // UPDATE - OrderItem güncelle
    public OrderItemDTO updateOrderItem(Long id, OrderItem updatedItem) {
        OrderItem existing = orderItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("OrderItem bulunamadı: " + id));

        existing.setQuantity(updatedItem.getQuantity());
        existing.setPrice(updatedItem.getPrice());

        // İlişkili Product değişecekse:
        if (updatedItem.getProduct() != null) {
            existing.setProduct(updatedItem.getProduct());
        }

        OrderItem saved = orderItemRepository.save(existing);
        return convertToDTO(saved);
    }


    // KOD TEKRARINDAN KAÇMAK İÇİN CONVERTTODTO METHODU KURULDU!!!
    private OrderItemDTO convertToDTO(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderItem.getId());
        dto.setOrderId(orderItem.getOrder().getId());
        dto.setProductId(orderItem.getProduct().getId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        return dto;
    }
}
