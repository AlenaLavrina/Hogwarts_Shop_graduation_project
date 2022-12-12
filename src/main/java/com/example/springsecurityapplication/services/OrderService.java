package com.example.springsecurityapplication.services;

import com.example.springsecurityapplication.enumm.Status;
import com.example.springsecurityapplication.models.Order;
import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    //Данный метод позволяет вернуть все товары
    public List<Order> getAllOrder(){
        return orderRepository.findAll();
    }

    //Данный метод позволяет вернуть товар по number
    public Order getOrderId(int id){
        Optional<Order> orderList = orderRepository.findById(id);
        return orderList.orElse(null);
    }

    //данный метод позволяет сохранить товар
    @Transactional
    public void saveOrder(Order order){
        orderRepository.save(order);
    }

    //Данный метод позволяет обновить товар
    @Transactional
    public void updateOrderStatus(int id, Order order){
        order.setId(id);
        orderRepository.save(order);
    }
}
