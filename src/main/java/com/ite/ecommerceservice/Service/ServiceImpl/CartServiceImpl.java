package com.ite.ecommerceservice.Service.ServiceImpl;

import com.ite.ecommerceservice.Entity.Cart;
import com.ite.ecommerceservice.Entity.CartItem;
import com.ite.ecommerceservice.Entity.Products;
import com.ite.ecommerceservice.Entity.User;
import com.ite.ecommerceservice.Repository.CartItemRepository;
import com.ite.ecommerceservice.Repository.CartRepository;
import com.ite.ecommerceservice.Repository.ProductRepository;
import com.ite.ecommerceservice.Repository.UserRepository;
import com.ite.ecommerceservice.Service.CartService;
import com.ite.ecommerceservice.config.SystemLogger;
import com.ite.ecommerceservice.constants.LogStepConstant;
import com.ite.ecommerceservice.constants.MessageConstant;
import com.ite.ecommerceservice.payload.request.AddToCartRequest;
import com.ite.ecommerceservice.payload.request.UpdateCartRequest;
import com.ite.ecommerceservice.payload.response.CartItemResponse;
import com.ite.ecommerceservice.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SystemLogger logger;

    @Override
    public ResponseEntity<?> addToCart(AddToCartRequest addToCartRequest, HttpServletRequest request) {
        try {
            String requestId = request.getHeader("requestId");
            logger.log(Thread.currentThread().getName(), requestId,"add to cart", LogStepConstant.BEGIN_PROCESS,request.getHeader("email")+ ";"+ addToCartRequest.toString());
            if(!userRepository.existsByEmail(request.getHeader("email"))){
                logger.log(Thread.currentThread().getName(), requestId,"add to cart", LogStepConstant.END_PROCESS,request.getHeader("email")+ ";" + MessageConstant.USER_NOT_EXIST);
                return ResponseUtil.getResponseEntity(MessageConstant.USER_NOT_EXIST, HttpStatus.BAD_REQUEST );
            }
            if(!productRepository.existsById(addToCartRequest.getProductId())){
                logger.log(Thread.currentThread().getName(), requestId,"add to cart", LogStepConstant.END_PROCESS,request.getHeader("email")+ ";" + MessageConstant.PRODUCT_NOT_EXIST + ";" + addToCartRequest.toString());
                return ResponseUtil.getResponseEntity(MessageConstant.PRODUCT_NOT_EXIST, HttpStatus.BAD_REQUEST );
            }

            Products products = productRepository.getProductsById(addToCartRequest.getProductId());

            if(addToCartRequest.getQuantity() > products.getQuanlity()){
                logger.log(Thread.currentThread().getName(), requestId,"add to cart", LogStepConstant.END_PROCESS,request.getHeader("email")+ ";" + MessageConstant.PRODUCT_QUANTITY_NOT_ENOUGH + ";" + addToCartRequest.toString());
                return ResponseUtil.getResponseEntity(MessageConstant.PRODUCT_QUANTITY_NOT_ENOUGH, HttpStatus.BAD_REQUEST );
            }
            logger.log(Thread.currentThread().getName(), requestId,"add to cart", LogStepConstant.BEGIN_CALL_DATABASE, "Get user by email: " +request.getHeader("email") );
            User user = userRepository.findUserByEmail(request.getHeader("email"));
            logger.log(Thread.currentThread().getName(), requestId,"add to cart", LogStepConstant.END_CALL_DATABASE, "Get user by email: " + user.toString() );


            logger.log(Thread.currentThread().getName(), requestId,"add to cart", LogStepConstant.BEGIN_CALL_DATABASE, "Get cart by user id: " + user.getUserId() );
            Cart cart = cartRepository.getCartByUserId(user.getUserId());
            logger.log(Thread.currentThread().getName(), requestId,"add to cart", LogStepConstant.END_CALL_DATABASE, "Get cart by user id: " +cart.toString() );

            Optional<CartItem> existingCartItemOptional = cartItemRepository.findByCartIdAndProductId(cart.getId(), addToCartRequest.getProductId());

            if(existingCartItemOptional.isPresent()) {
                CartItem existingCartItem = existingCartItemOptional.get();
                existingCartItem.setQuantity(existingCartItem.getQuantity() + addToCartRequest.getQuantity());
                logger.log(Thread.currentThread().getName(), requestId,"add to cart", LogStepConstant.BEGIN_CALL_DATABASE, "Update cart item: " + existingCartItem.toString() );
                cartItemRepository.save(existingCartItem);
                logger.log(Thread.currentThread().getName(), requestId,"add to cart", LogStepConstant.END_CALL_DATABASE, "Update cart item successfully" );
            } else {
                CartItem cartItem = CartItem.builder()
                        .cartId(cart.getId())
                        .productId(addToCartRequest.getProductId())
                        .quantity(addToCartRequest.getQuantity())
                        .createdAt(Instant.now())
                        .build();
                logger.log(Thread.currentThread().getName(), requestId,"add to cart", LogStepConstant.BEGIN_CALL_DATABASE, "Save cart item: " + cartItem.toString() );
                cartItemRepository.save(cartItem);
                logger.log(Thread.currentThread().getName(), requestId,"add to cart", LogStepConstant.END_CALL_DATABASE, "Save cart item successfully" );
            }

            logger.log(Thread.currentThread().getName(), requestId,"add to cart", LogStepConstant.END_PROCESS,request.getHeader("email")+ ";" + MessageConstant.ADD_TO_CART_SUCCESSFULLY + ";" + addToCartRequest.toString());
            return ResponseUtil.getResponseEntity("00",MessageConstant.ADD_TO_CART_SUCCESSFULLY,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"add to cart", LogStepConstant.END_PROCESS,MessageConstant.SOMETHING_WENT_WRONG + ":" + e.getMessage());
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getCartDetail(HttpServletRequest request) {
        try{
            String requestId = request.getHeader("requestId");
            logger.log(Thread.currentThread().getName(), requestId,"Get cart detail", LogStepConstant.BEGIN_PROCESS,request.getHeader("email"));

            logger.log(Thread.currentThread().getName(), requestId,"Get cart detail", LogStepConstant.BEGIN_CALL_DATABASE, "Get user by email: " +request.getHeader("email") );
            User user = userRepository.findUserByEmail(request.getHeader("email"));
            logger.log(Thread.currentThread().getName(), requestId,"Get cart detail", LogStepConstant.END_CALL_DATABASE, "Get user by email: " + user.toString() );

            logger.log(Thread.currentThread().getName(), requestId,"Get cart detail", LogStepConstant.BEGIN_CALL_DATABASE, "Get cart by user id: " + user.getUserId() );
            Cart cart = cartRepository.getCartByUserId(user.getUserId());
            logger.log(Thread.currentThread().getName(), requestId,"Get cart detail", LogStepConstant.END_CALL_DATABASE, "Get cart by user id: " +cart.toString() );

            if (!Objects.isNull(cart)) {
                logger.log(Thread.currentThread().getName(), requestId,"Get cart detail", LogStepConstant.BEGIN_CALL_DATABASE, "Get all item in cart by cartId: " + cart.getId());
                List<CartItem> cartItems = cartItemRepository.getAllByCartId(cart.getId());
                logger.log(Thread.currentThread().getName(), requestId,"Get cart detail", LogStepConstant.END_CALL_DATABASE, "Get all item in cart by cartId: " + cartItems.size() + " item");

                List<CartItemResponse> cartItemResponses = cartItems.stream().map(item -> {
                            Products products = productRepository.getProductsById(item.getProductId());
                            if (products != null) {
                                return CartItemResponse.builder()
                                        .productId(products.getId())
                                        .productName(products.getProductName())
                                        .categoryId(products.getCategoryId())
                                        .productBrand(products.getProductBrand())
                                        .urlImage(products.getUrlImage().get(0))
                                        .price(products.getPrice())
                                        .quantity(item.getQuantity())
                                        .build();
                            } else {
                                return null;
                            }
                        }).filter(Objects::nonNull)
                        .collect(Collectors.toList());
                logger.log(Thread.currentThread().getName(), requestId,"Get cart detail", LogStepConstant.END_PROCESS, "Get all item in cart by cartId: " + MessageConstant.SUCCESSFULLY);
                return ResponseEntity.ok(cartItemResponses);
            } else {
                logger.log(Thread.currentThread().getName(), requestId,"Get cart detail", LogStepConstant.END_PROCESS, "Get all item in cart by cartId: " + MessageConstant.CART_NOT_EXIST);
                return ResponseUtil.getResponseEntity(MessageConstant.CART_NOT_EXIST,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.log(Thread.currentThread().getName(),  request.getHeader("requestId"),"Get cart detail", LogStepConstant.END_PROCESS, "Get all item in cart by cartId: " + e.getMessage());
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> deleteProduct(String productId,HttpServletRequest request) {

        String requestId = request.getHeader("requestId");
        try{
            logger.log(Thread.currentThread().getName(), requestId,"Delete product in cart", LogStepConstant.BEGIN_PROCESS,request.getHeader("email")+ ";"+ "productId: " + productId);

            logger.log(Thread.currentThread().getName(), requestId,"Delete product in cart", LogStepConstant.BEGIN_CALL_DATABASE, "Get user by email: " +request.getHeader("email") );
            User user = userRepository.findUserByEmail(request.getHeader("email"));
            logger.log(Thread.currentThread().getName(), requestId,"Delete product in cart", LogStepConstant.END_CALL_DATABASE, "Get user by email: " + user.toString() );

            logger.log(Thread.currentThread().getName(), requestId,"Delete product in cart", LogStepConstant.BEGIN_CALL_DATABASE, "Get cart by user id: " + user.getUserId() );
            Cart cart = cartRepository.getCartByUserId(user.getUserId());
            logger.log(Thread.currentThread().getName(), requestId,"Delete product in cart", LogStepConstant.END_CALL_DATABASE, "Get cart by user id: " +cart.toString() );

            if(cartItemRepository.existsByCartIdAndProductId(cart.getId(), productId)){
                logger.log(Thread.currentThread().getName(), requestId,"Delete product in cart", LogStepConstant.BEGIN_CALL_DATABASE, "Delete product in cart by cartId and productId: " + user.getUserId()+"/"+ productId );
                cartItemRepository.deleteByCartIdAndProductId(cart.getId(), productId);
                logger.log(Thread.currentThread().getName(), requestId,"Delete product in cart", LogStepConstant.END_CALL_DATABASE, "Delete product in cart by cartId and productId: " + MessageConstant.DELETE_PRODUCT_SUCCESSFULLY );

                logger.log(Thread.currentThread().getName(), requestId,"Delete product in cart", LogStepConstant.END_PROCESS, "Delete product in cart by cartId and productId: " + MessageConstant.DELETE_PRODUCT_SUCCESSFULLY );
                return ResponseUtil.getResponseEntity(MessageConstant.DELETE_PRODUCT_SUCCESSFULLY, HttpStatus.OK);
            }else {
                logger.log(Thread.currentThread().getName(), requestId,"Delete product in cart", LogStepConstant.END_PROCESS,  MessageConstant.DELETE_PRODUCT_FAILED );
                return ResponseUtil.getResponseEntity(MessageConstant.PRODUCT_NOT_EXIST,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.log(Thread.currentThread().getName(), requestId,"Delete product in cart", LogStepConstant.END_PROCESS,  MessageConstant.DELETE_PRODUCT_FAILED + ":"+e.getMessage() );
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> updateCart(UpdateCartRequest updateCartRequest,HttpServletRequest request) {
        try {

            String requestId = request.getHeader("requestId");
            logger.log(Thread.currentThread().getName(), requestId,"Update product in cart", LogStepConstant.BEGIN_PROCESS,request.getHeader("email")+ ";"+ updateCartRequest.toString());
            if(!productRepository.existsById(updateCartRequest.getProductId())){
                logger.log(Thread.currentThread().getName(), requestId,"Update product in cart", LogStepConstant.END_PROCESS,request.getHeader("email")+ ";" + MessageConstant.PRODUCT_NOT_EXIST + ";" + updateCartRequest.toString());
                return ResponseUtil.getResponseEntity(MessageConstant.PRODUCT_NOT_EXIST, HttpStatus.BAD_REQUEST );
            }

            Products products = productRepository.getProductsById(updateCartRequest.getProductId());

            if(updateCartRequest.getQuantity() > products.getQuanlity()){
                logger.log(Thread.currentThread().getName(), requestId,"Update product in cart", LogStepConstant.END_PROCESS,request.getHeader("email")+ ";" + MessageConstant.PRODUCT_QUANTITY_NOT_ENOUGH + ";" + updateCartRequest.toString());
                return ResponseUtil.getResponseEntity(MessageConstant.PRODUCT_QUANTITY_NOT_ENOUGH, HttpStatus.BAD_REQUEST );
            }
            logger.log(Thread.currentThread().getName(), requestId,"Update product in cart", LogStepConstant.BEGIN_CALL_DATABASE, "Get user by email: " +request.getHeader("email") );
            User user = userRepository.findUserByEmail(request.getHeader("email"));
            logger.log(Thread.currentThread().getName(), requestId,"Update product in cart", LogStepConstant.END_CALL_DATABASE, "Get user by email: " + user.toString() );


            logger.log(Thread.currentThread().getName(), requestId,"Update product in cart", LogStepConstant.BEGIN_CALL_DATABASE, "Get cart by user id: " + user.getUserId() );
            Cart cart = cartRepository.getCartByUserId(user.getUserId());
            logger.log(Thread.currentThread().getName(), requestId,"Update product in cart", LogStepConstant.END_CALL_DATABASE, "Get cart by user id: " +cart.toString() );

            Optional<CartItem> existingCartItemOptional = cartItemRepository.findByCartIdAndProductId(cart.getId(), updateCartRequest.getProductId());

            if(existingCartItemOptional.isPresent()) {
                CartItem existingCartItem = existingCartItemOptional.get();
                existingCartItem.setQuantity(updateCartRequest.getQuantity());
                logger.log(Thread.currentThread().getName(), requestId,"Update product in cart", LogStepConstant.BEGIN_CALL_DATABASE, "Update cart item: " + existingCartItem.toString() );
                cartItemRepository.save(existingCartItem);
                logger.log(Thread.currentThread().getName(), requestId,"Update product in cart", LogStepConstant.END_CALL_DATABASE, "Update cart item successfully" );
            } else {
                logger.log(Thread.currentThread().getName(), requestId,"Update product in cart", LogStepConstant.END_PROCESS,request.getHeader("email")+ ";" + MessageConstant.ADD_TO_CART_SUCCESSFULLY + ";" + updateCartRequest.toString());
                return ResponseUtil.getResponseEntity("00",MessageConstant.PRODUCT_NOT_EXIST_IN_CART,HttpStatus.OK);
            }

            logger.log(Thread.currentThread().getName(), requestId,"Update product in cart", LogStepConstant.END_PROCESS,request.getHeader("email")+ ";" + MessageConstant.ADD_TO_CART_SUCCESSFULLY + ";" + updateCartRequest.toString());
            return ResponseUtil.getResponseEntity("00",MessageConstant.ADD_TO_CART_SUCCESSFULLY,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Update product in cart", LogStepConstant.END_PROCESS,MessageConstant.SOMETHING_WENT_WRONG + ":" + e.getMessage());
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteItemInCart(String userId, String productId) {
        try {
            Cart cart = cartRepository.getCartByUserId(userId);
            cartItemRepository.deleteByCartIdAndProductId(cart.getId(),productId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
