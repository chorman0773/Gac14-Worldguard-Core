package github.chorman0773.gac14.worldguard.core.permission;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.util.ResourceLocation;

public class UnresolvedPrincipalProvider implements IPrincipalProvider {
	private ResourceLocation name;
	
	public UnresolvedPrincipalProvider(ResourceLocation loc) {
		this.name = loc;
	}

	@Override
	public ResourceLocation getProviderName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public IPrincipal load(DataInputStream strm) throws IOException {
		int len = strm.readUnsignedShort();
		byte[] alloc = new byte[len];
		strm.readFully(alloc);
		return new UnresolvedPrincipal(this,alloc);
	}

	@Override
	public int store(IPrincipal principal, DataOutputStream strm) throws IOException {
		strm.writeUTF(name.toString());
		UnresolvedPrincipal unresolved = (UnresolvedPrincipal)principal;
		byte[] bytes = unresolved.getBytes();
		strm.writeShort(bytes.length);
		strm.write(bytes);
		return bytes.length;
	}

	@Override
	public int getSize(IPrincipal principal) {
		UnresolvedPrincipal unresolved = (UnresolvedPrincipal)principal;
		return unresolved.getBytes().length;
	}

	@Override
	public IPrincipal resolve(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
