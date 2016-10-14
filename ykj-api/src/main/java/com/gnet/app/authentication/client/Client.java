package com.gnet.app.authentication.client;

import javax.persistence.Table;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import com.gnet.mybatis.BaseEntity;
import com.gnet.mybatis.typeHanler.StringArrayTypeHandler;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tk.mybatis.mapper.annotation.ColumnType;

@Getter
@Setter
@ToString(exclude = "clientSecret")
@NoArgsConstructor
@Table(name = "sys_client")
public class Client extends BaseEntity {
	
	private static final long serialVersionUID = 8724191983666747281L;

	public static final PasswordEncoder PASSWORD_ENCODER = new StandardPasswordEncoder();
	
	private String clientId;
	private String clientSecret;
	private @ColumnType(typeHandler = StringArrayTypeHandler.class) String[] grantTypes;
	private @ColumnType(typeHandler = StringArrayTypeHandler.class) String[] scopes;
	private @ColumnType(typeHandler = StringArrayTypeHandler.class) String[] resourceIds;
	private @ColumnType(typeHandler = StringArrayTypeHandler.class) String[] authorities;
	
	public Client(String clientSecret, String... grantTypes) {
		this.clientSecret = clientSecret;
		this.grantTypes = grantTypes;
	}

	
}
