package github.chorman0773.gac14.worldguard.core.permission;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;

public class PermissionEntry {
	private IWorldguardPermission permission;
	private List<Rule> rules;
	
	public PermissionEntry(IWorldguardPermission permission,List<Rule> rules) {
		this.permission = permission;
		this.rules = new ArrayList<>(rules);
	}
	
	public PermissionEntry(DataInputStream strm) throws IOException {
		ResourceLocation loc = ResourceLocation.makeResourceLocation(strm.readUTF());
		permission = WorldguardRegistries.Permissions.getValue(loc);
		int numRules = strm.readUnsignedShort();
		rules = new ArrayList<>();
		for(int i = 0;i<numRules;i++)
			rules.add(new Rule(strm));
	}
	
	public List<Rule> getRules(){
		return Collections.unmodifiableList(rules);
	}
	
	public IWorldguardPermission getPermission() {
		return permission;
	}
	
	public void save(DataOutputStream strm) throws IOException{
		strm.writeUTF(permission.getPermissionName().toString());
		strm.writeShort(rules.size());
		for(Rule rule:rules)
			rule.write(strm);
	}
	
	public Event.Result getResult(UUID player){
		Event.Result res = Event.Result.DEFAULT;
		for(Rule r:rules)
			if(r.isDefaultRule())
				res = r.getResultFor();
			else if(r.matches(player))
				{res = r.getResultFor(); break;}
		if(res==Event.Result.DEFAULT)
			res = permission.getDefaultResult();
		return res;
	}

}
