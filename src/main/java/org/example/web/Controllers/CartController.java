package org.example.web.Controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.web.Models.CartModel;
import org.example.web.Models.ProductModel;
import org.example.web.Repo.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class CartController {

    @Autowired
    private IProductRepository productRepository;

    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session, HttpServletResponse response, Model model) {
        Optional<ProductModel> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            ProductModel product = productOptional.get();
            CartModel cart = (CartModel) session.getAttribute("cart");
            if (cart == null) {
                cart = new CartModel();
            }
            cart.getProducts().add(product);
            updateCartCookie(response, cart);
            session.setAttribute("cart", cart);
            model.addAttribute("cart", cart);
            model.addAttribute("product", product);

            return "cart";
        }

        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String viewCart(HttpServletRequest request, Model model) {
        CartModel cart = getOrCreateCartFromSession(request);

        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(HttpSession session, HttpServletRequest request, HttpServletResponse response, Model model) {
        String productId = request.getParameter("productId");
        Long productIdLong = Long.parseLong(productId);
        CartModel cart = getOrCreateCartFromSession(request);
        if (cart != null) {
            cart.getProducts().removeIf(product -> product.getId() == productIdLong);
            updateCartCookie(response, cart);
            model.addAttribute("cart", cart);
        }

        return "cart";
    }

    private CartModel getOrCreateCartFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        CartModel cart = (CartModel) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartModel();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private void updateCartCookie(HttpServletResponse response, CartModel cart) {
        String productIdsStr = cart.getProducts().stream()
                .map(p -> String.valueOf(p.getId()))
                .collect(Collectors.joining(","));

        Cookie cartCookie = new Cookie("cart", productIdsStr);
        cartCookie.setMaxAge(30 * 24 * 60 * 60);
        cartCookie.setPath("/");
        response.addCookie(cartCookie);
    }
}
