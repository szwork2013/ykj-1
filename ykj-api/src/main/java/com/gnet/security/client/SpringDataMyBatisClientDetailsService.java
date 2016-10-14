package com.gnet.security.client;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

import com.gnet.app.authentication.client.Client;
import com.gnet.app.authentication.client.ClientMapper;

@Component
public class SpringDataMyBatisClientDetailsService implements ClientDetailsService {

	@Autowired
	private ClientMapper clientMapper;
	
	@Override
	public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
		Client client = new Client();
		client.setClientId(s);
		client = clientMapper.selectOne(client);
		
		if (client == null) {
			throw new ClientRegistrationException("Given client ID does not match authenticated client");
		}
		
		BaseClientDetails baseClientDetails = new BaseClientDetails(client.getClientId(), StringUtils.join(client.getResourceIds(), ","), StringUtils.join(client.getScopes(), ","), StringUtils.join(client.getGrantTypes(), ","), StringUtils.join(client.getAuthorities(), ","));
		baseClientDetails.setClientSecret(client.getClientSecret());
		return baseClientDetails;
	}

}
