//$Id$
package PojoClasses;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public abstract class MasterClass {
	public boolean join=false;
	public String tablenametwo,commonfield,wherecondition;
	public LinkedList<String> columnname;
	public LinkedHashMap<LinkedHashMap<String,Object>,Integer> where;
	public LinkedHashMap<String,Object> update;
	
	public MasterClass() {
		columnname=new LinkedList<String>();
    	where=new LinkedHashMap<LinkedHashMap<String,Object>,Integer>();
    	update=new LinkedHashMap<String, Object>();
	}
	

	abstract  public String gettable_name();
	
	abstract  public void setTableNameTwo(String table,String commonfield,String wherecondition);
	abstract  public String getTableNameTwo();
	
	public void setColumnNames(String col)
	{
		this.columnname.add(col);
	}
	
	public LinkedHashMap<LinkedHashMap<String,Object>,Integer>  getwhere() {

		return this.where;
	}
	
	public void setwhere(String key,Object value,Integer condi) {

		LinkedHashMap<String,Object> k=new LinkedHashMap<String, Object>();
		k.put(key,value );
		this.where.put(k, condi);
	}
	
	 
	public LinkedHashMap<String,Object> getupdate() {

		return this.update;
	}

	 
	public void setupdate(String key,Object value) {
		LinkedHashMap<String,Object> k=new LinkedHashMap<String, Object>();
		this.update.put(key,value);
	}
	
}
