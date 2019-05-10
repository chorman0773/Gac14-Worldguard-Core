package github.chorman0773.gac14.worldguard.core.permission;

import java.util.Base64;
import java.util.UUID;

public final class UnresolvedPrincipal implements IPrincipal {
	private UnresolvedPrincipalProvider provider;
	private byte[] bytes;
	public UnresolvedPrincipal(UnresolvedPrincipalProvider provider,byte[] bytes) {
		this.provider = provider;
		this.bytes = bytes;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Unresolved:"+Base64.getEncoder().encodeToString(bytes);
	}

	@Override
	public IPrincipalProvider getProvider() {
		// TODO Auto-generated method stub
		return provider;
	}

	@Override
	public boolean matches(UUID player) {
		// TODO Auto-generated method stub
		return false;
	}

	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return bytes;
	}

}
