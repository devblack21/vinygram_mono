package br.com.app.vinygram.domains;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "notification")
public class Notification implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@OneToOne(cascade = CascadeType.ALL, fetch =  FetchType.EAGER)
	private User senderUser;
	@OneToOne(cascade = CascadeType.ALL, fetch =  FetchType.EAGER)
	private User recipientUser;
	@Enumerated(EnumType.STRING)
	@Column(name = "OPERATION", nullable = false)
	private Operation operation;
	@Column(name = "NOTIFICATION_DATE", nullable = false)
	private LocalDateTime date;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public User getSenderUser() {
		return senderUser;
	}
	public void setSenderUser(User senderUser) {
		this.senderUser = senderUser;
	}
	public User getRecipientUser() {
		return recipientUser;
	}
	public void setRecipientUser(User recipientUser) {
		this.recipientUser = recipientUser;
	}
	public Operation getOperation() {
		return operation;
	}
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Notification other = (Notification) obj;
		if (id != other.id)
			return false;
		return true;
	}

}