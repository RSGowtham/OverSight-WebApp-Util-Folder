//$Id$
package PojoClasses;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Companies extends MasterClass{	
	
	private int company_id;
    private String company_name,gst_registration;
    private long created_time,modified_time;
    
    public Companies()
    {
    	super();
    }

    public String gettable_name()
    {
    	String table=this.getClass().getName();
    	return table.substring(table.indexOf(".")+1).toLowerCase();
    }
    
    public void setTableNameTwo(String table,String commonfield,String wherecondition)
    {
    	this.tablenametwo=table;
    	this.commonfield=commonfield;
    	this.wherecondition=wherecondition;
    	this.join=true;
    }
    
    public String getTableNameTwo()
    {
    	return this.tablenametwo;
    }
    
	public int getcompany_id() {
		return this.company_id;
	}
	public void setcompany_id(int company_id) {
		this.columnname.add("company_id");
		this.company_id = company_id;
	}
	public String getcompany_name() {
		return this.company_name;
	}
	public void setcompany_name(String company_name) {
		this.columnname.add("company_name");
		this.company_name = company_name;
	}
	public String getgst_registration() {
		return this.gst_registration;
	}
	public void setgst_registration(String gst_registration) {
		this.columnname.add("gst_registration");
		this.gst_registration = gst_registration;
	}
	public long getcreated_time() {
		return this.created_time;
	}
	public void setcreated_time(long created_time) {
		this.columnname.add("created_time");
		this.created_time = created_time;
	}
	public long getmodified_time() {
		return this.modified_time;
	}
	public void setmodified_time(long modified_time) {
		this.columnname.add("modified_time");
		this.modified_time = modified_time;
		
	}

}
