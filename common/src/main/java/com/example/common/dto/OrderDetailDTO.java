package com.example.common.dto;

import com.example.common.dto.OrderDetailId;
import jakarta.persistence.EmbeddedId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @EmbeddedId
    private OrderDetailId id = new OrderDetailId();
    private Integer quantity;
    private BigDecimal unitPrice;
}
