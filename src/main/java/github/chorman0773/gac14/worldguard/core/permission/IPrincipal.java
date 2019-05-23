package github.chorman0773.gac14.worldguard.core.permission;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import net.minecraft.util.ResourceLocation;

/**
 * 
 * Represents a principal, or something which can be given permissions in Worldguard.
 * 
 * A Principal can be a single user, a group, or or even something special such as a Faction.
 * Instances of this class are specific principals, that is, they have data associated with them.
 * For Providers see {@link IPrincipalProvider}
 * 
 * @author chorm
 *
 * @see IPrincipalProvider for the interface that provides Principals.
 */
public interface IPrincipal {
	/**
	 * Gets the name of the Principal.
	 * The name of a principal is implementation-defined
	 */
	public String getName();
	
	/**
	 * Gets the provider of this Principal.
	 * 
	 * The Provider defines how to obtain instances of a Principal, and what data the principal stores.
	 */
	public IPrincipalProvider getProvider();
	public boolean matches(UUID player);
	public default void store(DataOutputStream strm) throws IOException {
		strm.writeUTF(getProvider().getProviderName().toString());
		strm.writeShort(getProvider().getSize(this));
		getProvider().store(this, strm);
	}
}
