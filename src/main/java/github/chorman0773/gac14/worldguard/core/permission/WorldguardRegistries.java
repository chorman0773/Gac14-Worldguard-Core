package github.chorman0773.gac14.worldguard.core.permission;

import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public final class WorldguardRegistries {

	public static final IForgeRegistry<IPrincipalProvider> PrincipalProviders;
	public static final IForgeRegistry<IWorldguardPermission> Permissions;
	
	static {
		PrincipalProviders = new RegistryBuilder<IPrincipalProvider>()
				.setType(IPrincipalProvider.class)
				.set((name,_dummy)->new UnresolvedPrincipalProvider(name))
				.create();
		Permissions = new RegistryBuilder<IWorldguardPermission>()
				.setType(IWorldguardPermission.class)
				.set((name,_dummy)->new UnresolvedPermission(name))
				.create();
	}
	
}
