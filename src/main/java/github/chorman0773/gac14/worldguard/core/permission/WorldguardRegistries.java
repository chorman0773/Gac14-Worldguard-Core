package github.chorman0773.gac14.worldguard.core.permission;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber
public final class WorldguardRegistries {

	public static final IForgeRegistry<IPrincipalProvider> PrincipalProviders;
	public static final IForgeRegistry<IWorldguardPermission> Permissions;
	
	static {
		PrincipalProviders = new RegistryBuilder<IPrincipalProvider>()
				.setType(IPrincipalProvider.class)
				.setName(new ResourceLocation("gac14:worldguard/principals"))
				.set((name,_dummy)->new UnresolvedPrincipalProvider(name))
				.disableSync()
				.create();
		Permissions = new RegistryBuilder<IWorldguardPermission>()
				.setType(IWorldguardPermission.class)
				.setName(new ResourceLocation("gac14:worldguard/permissions"))
				.set((name,_dummy)->new UnresolvedPermission(name))
				.disableSync()
				.create();
	}
	
	public static IPrincipal resolve(String name) {
		IPrincipal ret = null;
		for(IPrincipalProvider provider:PrincipalProviders)
			if((ret = provider.resolve(name))!=null)
				return ret;
		return null;
	}
	
	public static IPrincipal readPrincipal(DataInputStream strm) throws IOException {
		String ptype = strm.readUTF();
		ResourceLocation loc = new ResourceLocation(ptype);
		IPrincipalProvider provider = WorldguardRegistries.PrincipalProviders.getValue(loc);
		return provider.load(strm);
	}
	
	@SubscribeEvent
	public static void addRegistries(RegistryEvent.NewRegistry registry) {
	}
	
	@SubscribeEvent
	public static void registerPrincipals(RegistryEvent.Register<IPrincipalProvider> register) {
		PrincipalProviders.register(new UserPrincipalProvider());
	}
	
	
}
