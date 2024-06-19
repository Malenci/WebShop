package org.example.web.Controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.web.Models.*;
import org.example.web.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
//import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

@Controller
@RequestMapping("/cart")
public class OrderController {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderItemRepository orderItemRep;
    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private IProductRepository productRepository;
    @GetMapping("/addOrderForm")
    public String showAddOrderForm(Model model, HttpServletRequest request) {

        List<ProductModel> productsId = getProductsFromCookie(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        UserModel user = userRepository.findByUsername(currentUserName);
        model.addAttribute("user", user);
        model.addAttribute("productsInCart", productsId);
        return "order";
    }

    @PostMapping("/addOrder")
    public String addOrder(@ModelAttribute OrderRequest orderRequest,
                           Model model,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        List<ProductModel> productsInCart = getProductsFromCookie(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        UserModel user = userRepository.findByUsername(currentUserName);
        List<OrderItem> existingItems = orderItemRep.findAllByIdIn(
                productsInCart.stream()
                        .map(ProductModel::getId)
                        .collect(Collectors.toList())
        );
        List<OrderItem> items = productsInCart.stream()
                .map(product -> {

                    OrderItem existingItem = existingItems.stream()
                            .filter(item -> item.getProduct().getId() == (product.getId()))
                            .findFirst()
                            .orElse(null);

                    OrderItem item = new OrderItem();
                    item.setProduct(product);
                    item.setQuantity(1);

                    if (existingItem != null) {
                        // Используем существующий ID OrderItem
                        item.setId(existingItem.getId());
                    }

                    return item;
                })
                .collect(Collectors.toList());
//        List<OrderItem> items = productsInCart.stream()
//                .map(product -> {
//                    OrderItem item = new OrderItem();
//                    item.setId(product.getId());
//                    item.setProduct(product);
//                    item.setQuantity(1);
//                    return item;
//                })
//                .collect(Collectors.toList());

        OrderModel order = new OrderModel();
        order.setUser(user);
        order.setOrderItems(items);
        order.setOrderDate(LocalDate.now());
        order.setAddress(orderRequest.getAddress());
        order.setCity(orderRequest.getCity());
        order.setZipCode(orderRequest.getZipCode());
        order.setCountry(orderRequest.getCountry());
        order.setPhoneNumber(orderRequest.getPhoneNumber());

        clearProductsFromCookie(request, response);

        OrderModel savedOrder = orderRepository.save(order);
        model.addAttribute("order", savedOrder);

        return "ordersuccess";
    }


    private List<ProductModel> getProductsFromCookie(HttpServletRequest request) {
        List<Long> productIds = getProductIdsFromCookie(request);
        List<ProductModel> productsInCart = new ArrayList<>();
        for (Long productId : productIds) {
            ProductModel product = productRepository.findById(productId).orElse(null);
            if (product != null) {
                productsInCart.add(product);
            }
        }
        return productsInCart;
    }

    private List<Long> getProductIdsFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        List<Long> productIds = new ArrayList<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cart")) {
                    String[] productIdStrArray = cookie.getValue().split(",");
                    for (String productIdStr : productIdStrArray) {
                        productIds.add(Long.parseLong(productIdStr.trim()));
                    }
                    break;
                }
            }
        }
        return productIds;
    }
    private void clearProductsFromCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cart")) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }

}



