package github.chorman0773.gac14.worldguard.core.permission;

import java.util.UUID;

public final class PlayerPrincipal implements IPrincipal {
	private UUID id;
	private UserPrincipalProvider provider;
	
	public PlayerPrincipal(UUID uuid, UserPrincipalProvider provider) {
		this.id = uuid;
		this.provider = provider;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return id.toString();
	}
	
	public UUID getId() {
		return id;
	}

	@Override
	public IPrincipalProvider getProvider() {
		// TODO Auto-generated method stub
		return provider;
	}

	@Override
	public boolean matches(UUID player) {
		// TODO Auto-generated method stub
		return player.equals(id);
	}

}
