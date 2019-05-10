package github.chorman0773.gac14.worldguard.core.permission;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import github.chorman0773.gac14.Gac14Core;
import github.chorman0773.gac14.Patterns;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

public final class UserPrincipalProvider implements IPrincipalProvider {

	public UserPrincipalProvider() {
		// TODO Auto-generated constructor stub
	}
	private static final ResourceLocation name = ResourceLocation.makeResourceLocation("gac14:worldguard/player");

	@Override
	public ResourceLocation getProviderName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public IPrincipal load(DataInputStream strm) throws IOException {
		long most, least;
		most = strm.readLong();
		least = strm.readLong();
		
		return new PlayerPrincipal(new UUID(most,least),this);
	}

	@Override
	public int store(IPrincipal principal, DataOutputStream strm) throws IOException {
		UUID id = ((PlayerPrincipal)principal).getId();
		strm.writeLong(id.getMostSignificantBits());
		strm.writeLong(id.getLeastSignificantBits());
		return 16;
	}
	

	@Override
	public IPrincipal resolve(String name) {
		if(!name.startsWith("U:"))
			return null;
		name = name.substring(2);
		if(name.matches(Patterns.uuid))
			return new PlayerPrincipal(UUID.fromString(name),this);
		MinecraftServer server = Gac14Core.getInstance().getServer();
		UUID id = server.getPlayerProfileCache().getGameProfileForUsername(name).getId();
		return new PlayerPrincipal(id,this);
	}

	@Override
	public int getSize(IPrincipal principal) {
		// TODO Auto-generated method stub
		return 16;
	}

}
