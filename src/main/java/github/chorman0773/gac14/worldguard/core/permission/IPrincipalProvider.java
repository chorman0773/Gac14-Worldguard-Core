package github.chorman0773.gac14.worldguard.core.permission;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IPrincipalProvider extends IForgeRegistryEntry<IPrincipalProvider> {
	public ResourceLocation getProviderName();
	public IPrincipal load(DataInputStream strm)throws IOException;
	public int store(IPrincipal principal,DataOutputStream strm) throws IOException;
	public int getSize(IPrincipal principal);
	public IPrincipal resolve(String name);
	@Override
	default IPrincipalProvider setRegistryName(ResourceLocation name) {
		throw new UnsupportedOperationException("Cannot modify the name of a Principal Provider");
	}
	@Override
	default ResourceLocation getRegistryName() {
		// TODO Auto-generated method stub
		return getProviderName();
	}
	@Override
	default Class<IPrincipalProvider> getRegistryType() {
		// TODO Auto-generated method stub
		return IPrincipalProvider.class;
	}
}
