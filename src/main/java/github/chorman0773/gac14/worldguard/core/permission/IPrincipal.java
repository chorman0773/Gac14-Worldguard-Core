package github.chorman0773.gac14.worldguard.core.permission;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import net.minecraft.util.ResourceLocation;

public interface IPrincipal {
	public String getName();
	public IPrincipalProvider getProvider();
	public boolean matches(UUID player);
	public default void store(DataOutputStream strm) throws IOException {
		strm.writeUTF(getProvider().getProviderName().toString());
		strm.writeShort(getProvider().getSize(this));
		getProvider().store(this, strm);
	}
}
