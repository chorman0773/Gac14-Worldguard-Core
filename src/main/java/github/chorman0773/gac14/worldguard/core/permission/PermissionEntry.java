package github.chorman0773.gac14.worldguard.core.permission;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableSet;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class PermissionEntry {
	private IWorldguardPermission permission;
	private List<Rule> rules;
	private String checks;
	
	public PermissionEntry(IWorldguardPermission permission,String checks,List<Rule> rules) {
		this.permission = permission;
		this.rules = new ArrayList<>(rules);
		this.checks = checks;
	}
	
	public PermissionEntry(DataInputStream strm) throws IOException {
		String[] str = strm.readUTF().split(";");
		checks = str[1];
		ResourceLocation loc = ResourceLocation.makeResourceLocation(str[0]);
		permission = WorldguardRegistries.Permissions.getValue(loc);
		int numRules = strm.readUnsignedShort();
		rules = new ArrayList<>();
		for(int i = 0;i<numRules;i++)
			rules.add(new Rule(strm));
	}
	
	public List<Rule> getRules(){
		return Collections.unmodifiableList(rules);
	}
	
	public <T extends IForgeRegistryEntry<T>> boolean isAffectedBy(T t){
		if(checks.equals("*"))
			return true;
		else if(checks.isEmpty())
			return false;
		else
			return ImmutableSet.copyOf(checks.split(",\\s+")).contains(t.getRegistryName().toString());
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
