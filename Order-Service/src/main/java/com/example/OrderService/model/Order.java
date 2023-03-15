package com.example.OrderService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="t_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
     private Long id;
     private String orderNumber;
     @OneToMany(cascade = CascadeType.ALL)
     @JoinColumn(name="oderId",referencedColumnName = "id")
     private List<OrderLineItems> orderLineItemsList;
}
