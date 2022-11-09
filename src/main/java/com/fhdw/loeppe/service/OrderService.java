package com.fhdw.loeppe.service;

import com.fhdw.loeppe.dto.Customer;
import com.fhdw.loeppe.entity.CustomerEntity;
import com.fhdw.loeppe.entity.OrderEntity;
import com.fhdw.loeppe.dto.Order;
import com.fhdw.loeppe.repo.CustomerRepository;
import com.fhdw.loeppe.repo.OrderRepository;
import com.fhdw.loeppe.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository repository;
    private final Mapper mapper;

    public void saveOrder(Order order){
        OrderEntity entity = mapper.map(order, OrderEntity.class);
        entity.setCustomerEntity(mapper.map(order.getCustomer(), CustomerEntity.class));
        repository.saveAndFlush(entity);
    }

    public void saveAllOrders(List<Order> orders){
        repository.saveAllAndFlush(mapper.mapAll(orders, OrderEntity.class));
    }

    public Order getOrder(long id) {
        Order order = mapper.map(repository.findById(id), Order.class);
        order.setCustomer(mapper.map(customerRepository.findById(repository.findById(id).get().getCustomerEntity().getId()), Customer.class));
        return order;
    }

    public List<Order> getAllOrders(){
        List<OrderEntity> orderEntities = repository.findAll();
        List<Order> orders = new ArrayList<>();

        for(int i = 0; i < orderEntities.size(); i++) {
            Order order = mapper.map(orderEntities.get(i), Order.class);
            order.setCustomer(mapper.map(customerRepository.findById(orderEntities.get(i).getId()), Customer.class));
            orders.add(order);
        }

        return orders;
    }

    public void updateOrder(Order order) {
        saveOrder(order);
    }

    public void deleteOrder(long id) {
        repository.deleteById(id);
    }

    public void deleteAllOrders() {
        repository.deleteAll();
    }
}

//TODO: ServiceTest
