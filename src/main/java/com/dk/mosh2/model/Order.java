package com.dk.mosh2.model;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import com.dk.mosh2.enums.OrderStatus;
import com.dk.mosh2.service.jwt.AuthService;

import java.math.BigInteger;
import java.time.LocalDateTime;


/**
 * The persistent class for the orders database table.
 * 
 */
@Entity
@Table(name="orders")
@ToString
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name="created_at")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Column(name="total_price")
	private BigDecimal totalPrice;

	//bi-directional one-to-one association to OrderItem
	@OneToMany(mappedBy="order" , cascade = CascadeType.ALL)
	@ToString.Exclude
	private List<OrderItem> orderItem = new ArrayList<OrderItem>();

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="customer_id", referencedColumnName="id")
	private Users user;

	public Order() {
	}


	public BigDecimal getTotalPrice() {
		return this.totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	

	public List<OrderItem> getOrderItem() {
		return orderItem;
	}


	public void setOrderItem(List<OrderItem> orderItem) {
		this.orderItem = orderItem;
	}


	public BigInteger getId() {
		return id;
	}


	public void setId(BigInteger id) {
		this.id = id;
	}


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}


	public OrderStatus getStatus() {
		return status;
	}


	public void setStatus(OrderStatus status) {
		this.status = status;
	}


	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
	public static Order placeOrderFromCart(Cart cart , Users user) {
		
	
		Order order =  new Order();
		order.setStatus(OrderStatus.PENDING);
		order.setTotalPrice(cart.getTotalPrice());
	
		order.setUser(user);
		
		cart.getCartItems()
		.forEach(cartitem ->{
			OrderItem orderItem  = new OrderItem(order,cartitem.getProduct(),cartitem.getQuantity());
//			orderItem.setOrder(order);
//			orderItem.setProduct(cartitem.getProduct());
//			orderItem.setQuantity(cartitem.getQuantity());
			
			// product know the unit price 
//			orderItem.setUnitPrice(cartitem.getProduct().getPrice());
			
			// added cartItem(item) know total price of item + total quantity 
//			orderItem.setTotalPrice(cartitem.getTotalPrice());

			// order know about order Item
			// follow principle of expert
			order.getOrderItem().add(orderItem);
		});
		
		return order;
	}

	public boolean isPlacedBy(Users user){
	return this.user.equals(user);
	}

	

}