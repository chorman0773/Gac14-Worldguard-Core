package github.chorman0773.gac14.worldguard.core.permission;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;

public class Rule {
	private Event.Result resultFor;
	private List<IPrincipal> principals;
	private boolean isDefault;
	
	public Rule(DataInputStream strm) throws IOException {
		int target = strm.readUnsignedByte();
		if((target&0x80)!=0)
			isDefault = true;
		if((target&0x7f)==1)
			resultFor = Event.Result.ALLOW;
		else if((target&0x7f)==2)
			resultFor = Event.Result.DENY;
		else if((target&0x7f)==0)
			resultFor = Event.Result.DEFAULT;
		else
			throw new IOException("Format Error");
		if(!isDefault) {
			int pcount = strm.readUnsignedShort();
			principals = new ArrayList<>();
			for(int i = 0;i<pcount;i++) {
				String ptype = strm.readUTF();
				ResourceLocation loc = ResourceLocation.makeResourceLocation(ptype);
				 IPrincipalProvider provider = WorldguardRegistries.PrincipalProviders.getValue(loc);
				 int bytes = strm.readUnsignedShort();
				 if(provider==null)
					 strm.skipBytes(bytes);
				 else
					 principals.add(provider.load(strm));
			}
		}
	}
	
	public Rule(Event.Result res) {
		this.resultFor = res;
		this.isDefault = true;
	}
	
	public Rule(Event.Result res,List<IPrincipal> principals) {
		this.resultFor = res;
		this.principals = new ArrayList<>(principals);
		this.isDefault = false;
	}
	
	public boolean isDefaultRule() {
		return isDefault;
	}
	
	public Event.Result getResultFor(){
		return resultFor;
	}
	
	public List<IPrincipal> getPrincipals(){
		return Collections.unmodifiableList(principals);
	}
	
	public void write(DataOutputStream strm) throws IOException {
		int target;
		if(resultFor==Event.Result.ALLOW)
			target = 1;
		else if(resultFor==Event.Result.DENY)
			target = 2;
		else 
			target = 0;
		if(isDefault)
			target |= 0x80;
		strm.writeByte(target);
		if(!isDefault) {
			strm.writeShort(principals.size());
			for(IPrincipal principal:principals)
				principal.store(strm);
		}
	}
	

}
