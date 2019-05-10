package github.chorman0773.gac14.worldguard.core.zone;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.core.util.UuidUtil;

import github.chorman0773.gac14.Gac14Core;
import github.chorman0773.gac14.Version;
import github.chorman0773.gac14.worldguard.core.permission.IPrincipal;
import github.chorman0773.gac14.worldguard.core.permission.PermissionEntry;
import github.chorman0773.gac14.worldguard.core.permission.WorldguardRegistries;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;

public class Zone {
	private Chunk affectedChunk;
	private World world;
	private List<PermissionEntry> permissionEntries;
	private IPrincipal owner;
	private UUID zoneId;
	private ITextComponent title;
	private ITextComponent subtitle;
	private ResourceLocation origin;
	
	public static final Version specVersion = new Version("1.0");
	public static int MAGIC = 0xFF475744;
	
	public Zone(Chunk affectedChunk,List<PermissionEntry> permissions) {
		this(affectedChunk,permissions,WorldguardRegistries.resolve("U:00000000-0000-0000-0000-000000000000"),"gac14:worldguard/core");
	}
	public Zone(Chunk affectedChunk,List<PermissionEntry> permissions,String origin) {
		this(affectedChunk,permissions,WorldguardRegistries.resolve("U:00000000-0000-0000-0000-000000000000"),origin);
	}
	public Zone(Chunk affectedChunk,List<PermissionEntry> permissions,IPrincipal owner) {
		this(affectedChunk,permissions,owner,"gac14:worldguard/core");
	}
	public Zone(Chunk affectedChunk,List<PermissionEntry> permissions,IPrincipal owner,String origin) {
		this(affectedChunk,permissions,owner,new TextComponentString(""),new TextComponentString(""),origin);
	}
	
	public Zone(Chunk affectedChunk,List<PermissionEntry> permissions,ITextComponent title,ITextComponent subtitle) {
		this(affectedChunk,permissions,WorldguardRegistries.resolve("U:00000000-0000-0000-0000-000000000000"),title,subtitle,"gac14:worldguard/core");
	}
	public Zone(Chunk affectedChunk,List<PermissionEntry> permissions,ITextComponent title,ITextComponent subtitle,String origin) {
		this(affectedChunk,permissions,WorldguardRegistries.resolve("U:00000000-0000-0000-0000-000000000000"),title,subtitle,origin);
	}
	public Zone(Chunk affectedChunk,List<PermissionEntry> permissions,IPrincipal owner,ITextComponent title,ITextComponent subtitle) {
		this(affectedChunk,permissions,owner,title,subtitle,"gac14:worldguard/core");
	}
	public Zone(Chunk affectedChunk,List<PermissionEntry> permissions,IPrincipal owner,ITextComponent title,ITextComponent subtitle,String origin) {
		this.affectedChunk = affectedChunk;
		this.permissionEntries = new ArrayList<>(permissions);
		this.owner = owner;
		this.zoneId = UuidUtil.getTimeBasedUuid();
		this.title = title;
		this.subtitle = subtitle;
		this.origin = ResourceLocation.makeResourceLocation(origin);
		if(zones.putIfAbsent(this.affectedChunk, this)!=null)
			throw new IllegalArgumentException("Chunk Already Exists");
	}
	
	public Zone(DataInputStream strm) throws IOException {
		int magic = strm.readInt();
		if(magic!=MAGIC)
			throw new IOException("File Rejected: Magic Mismatch");
		Version ver = Version.read(strm);
		if(ver.compareTo(specVersion)>1)
			throw new IOException("File Rejected: Unsupported Version");
		zoneId = new UUID(strm.readLong(),strm.readLong());
		owner = WorldguardRegistries.readPrincipal(strm);
		String titleStr = strm.readUTF();
		this.title = ITextComponent.Serializer.fromJson(titleStr);
		titleStr = strm.readUTF();
		this.title = ITextComponent.Serializer.fromJson(titleStr);
		this.origin = ResourceLocation.makeResourceLocation(strm.readUTF());
		World w = Gac14Core.getInstance().getServer().getWorld(DimensionType.byName(ResourceLocation.makeResourceLocation(strm.readUTF())));
		this.affectedChunk = w.getChunk(strm.readInt(), strm.readInt());
		int pcount = strm.readUnsignedShort();
		this.permissionEntries = new ArrayList<>();
		for(int i = 0;i<pcount;i++)
			this.permissionEntries.add(new PermissionEntry(strm));
		if(zones.putIfAbsent(this.affectedChunk, this)!=null)
			throw new IllegalArgumentException("Chunk Already Exists");
	}
	
	public void save(DataOutputStream strm) throws IOException{
		strm.writeInt(MAGIC);
		specVersion.write(strm);
		strm.writeLong(zoneId.getMostSignificantBits());
		strm.writeLong(zoneId.getLeastSignificantBits());
		owner.store(strm);
		strm.writeUTF(ITextComponent.Serializer.toJson(title));
		strm.writeUTF(ITextComponent.Serializer.toJson(subtitle));
		strm.writeUTF(origin.toString());
		World w = affectedChunk.getWorld();
		strm.writeUTF(w.getDimension().getType().getRegistryName().toString());
		strm.writeInt(affectedChunk.x);
		strm.writeInt(affectedChunk.z);
		strm.writeShort(this.permissionEntries.size());
		for(PermissionEntry entry:permissionEntries)
			entry.save(strm);
	}
	
	
	public void delete() {
		zones.put(this.affectedChunk, null);
		this.affectedChunk = null;
	}
	
	private static final Map<Chunk,Zone> zones = new HashMap<>();
	
	public static Zone getZoneForPosition(ChunkPos pos,World w) {
		return zones.get(w.getChunk(pos.x,pos.z));
	}
	
	public static Zone getZoneForPosition(BlockPos pos,World w) {
		return zones.get(w.getChunk(pos));
	}

}
