package com.fhdw.loeppe.service;

import com.fhdw.loeppe.dto.Customer;
import com.fhdw.loeppe.dto.Order;
import com.fhdw.loeppe.entity.CustomerEntity;
import com.fhdw.loeppe.entity.OrderEntity;
import com.fhdw.loeppe.helpers.SearchHelper;
import com.fhdw.loeppe.repo.CustomerRepository;
import com.fhdw.loeppe.repo.OrderRepository;
import com.fhdw.loeppe.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final Mapper mapper;

    public OrderEntity saveOrder(Order order){
        OrderEntity entity = mapper.map(order, OrderEntity.class);
        entity.setCustomerEntity(mapper.map(order.getCustomer(), CustomerEntity.class));

        return orderRepository.saveAndFlush(entity);
    }

    public void saveAllOrders(List<Order> orders){
        orderRepository.saveAllAndFlush(mapper.mapAll(orders, OrderEntity.class));
    }

    public List<Order> searchOrder(String orderID, String custID, Order order) {
        final List<Order> repoOrderID = SearchHelper.searchOrderID(getAllOrders(), orderID);
        final List<Order> repoStatus = SearchHelper.searchOrderStatus(repoOrderID, order.getOrderStatus());
        final List<Order> repoCustID = SearchHelper.searchOrderCustID(repoStatus, custID);
        final List<Order> repoCustFirstname = SearchHelper.searchOrderFirstname(repoCustID, order.getCustomer().getFirstname());
        return SearchHelper.searchOrderLastname(repoCustFirstname, order.getCustomer().getLastname());
    }

    public Order getOrder(UUID id) {
        Order order = mapper.map(orderRepository.findById(id), Order.class);
        order.setCustomer(mapper.map(customerRepository.findById(orderRepository.findById(id).get().getCustomerEntity().getId()), Customer.class));
        return order;
    }

    public List<Order> getAllOrders(){
        List<OrderEntity> orderEntities = orderRepository.findAll();
        List<Order> orders = new ArrayList<>();

        for(int i = 0; i < orderEntities.size(); i++) {
            Order order = mapper.map(orderEntities.get(i), Order.class);
            order.setCustomer(mapper.map(customerRepository.findById(orderEntities.get(i).getCustomerEntity().getId()), Customer.class));
            orders.add(order);
        }

        return orders;
    }

    public void updateOrder(Order order) {
        saveOrder(order);
    }

    public void deleteOrder(Order order) {
        orderRepository.delete(mapper.map(order, OrderEntity.class));
    }

    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }
}

//TODO: ServiceTest
