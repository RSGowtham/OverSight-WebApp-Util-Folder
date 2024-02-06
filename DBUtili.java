//$Id$
package DBconnection;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import PojoClasses.MasterClass;

enum DatabaseOperation {
    INSERT("INSERT INTO %s (%s) VALUES (%s) WHERE %s"),
    SELECT("SELECT %s FROM %s INNER JOIN %s ON %s = %s WHERE %s"),
    UPDATE("UPDATE %s SET %s WHERE %s"),
    DELETE("DELETE FROM %s WHERE %s");

    private String sqlTemplate;

    DatabaseOperation(String sqlTemplate) {
        this.sqlTemplate = sqlTemplate;
    }

    public String getSqlTemplate() {
        return sqlTemplate;
    }
}
public class DBUtili{
	
	public static <Child extends MasterClass > boolean insert(Child obj)
	{
		
		//LinkedList column
		LinkedList<String> colum=obj.columnname;
		
		int size=colum.size();
		
		if(size<1)
		{
			return false;
		}
		
		// ? mark 
		String ques=questionMarksBuilder(size);
		
		// column names
		String columns=columnBuilder(colum);
		
		// Table name
		String table_name=obj.gettable_name();
		
		
		String where=null;
		if(obj.getwhere().size()>1)
		{
			where=conditionBuilder(obj,true);
		}	
		
		// Query
		
		String query;
		if(where!=null)
		{
			query=String.format(DatabaseOperation.INSERT.getSqlTemplate(),table_name,columns,ques,where);
		}
		else
		{
		
			String fullquery=String.format(DatabaseOperation.INSERT.getSqlTemplate(), table_name,columns,ques,where);
			int e=fullquery.indexOf("WHERE");
			query=fullquery.substring(0,e);
 
		}
		
		System.out.println(query);
		return executer(query, obj, colum);
	}
	
	public static <Child extends MasterClass >boolean select(Child obj)
	{
		//LinkedList column
		LinkedList<String> colum=obj.columnname;
		int size=colum.size();
		String columns="*";
		if(size>=1)
		{
			columns=columnBuilder(colum);	
		}
		// Table name
		String table1=obj.gettable_name();	
		// Where Condition	
		String where=null;
		if(obj.getwhere().size()>1)
		{
			where=conditionBuilder(obj,true);
		}	
		
		// Query
		String query;
 
		if(obj.join)
		{
			
			String table2=obj.getTableNameTwo();
			String commonfield=obj.commonfield;

			query=String.format(DatabaseOperation.SELECT.getSqlTemplate(), columns,table1,table2,table1+commonfield,table2+commonfield,table1+where);
			
		}
		else
		{	
		
		
		
		if(where!=null)
		{
			String temp=String.format(DatabaseOperation.SELECT.getSqlTemplate(),columns,table1,null,null,"end",where);
			query=temp.replace("INNER JOIN null ON null = end", "");
		}
		
		else
		{
			String fullquery=String.format(DatabaseOperation.SELECT.getSqlTemplate(), columns,table1,null,null,"end",where);
			int e=fullquery.indexOf("INNER");
			query=fullquery.substring(0,e);
		}
		
		}
		System.out.println(query);
		return executer(query, obj, colum);
	}
	
	public static <Child extends MasterClass >boolean update(Child obj)
	{
		//LinkedList column
		LinkedList<String> colum=obj.columnname;
		int size=colum.size();
		if(size<1)
		{
			return false;
		}	
		// Table name
		String table_name=obj.gettable_name();	
		// Where Condition
		String where=null;
		if(obj.getwhere().size()>1)
		{
			where=conditionBuilder(obj,true);
		}	
			
		// Query
		String query;
		// SET operation
		String set=conditionBuilder(obj,false);
		
		if(where!=null)
		{
			query=String.format(DatabaseOperation.UPDATE.getSqlTemplate(),table_name,set,where);
		}
		else
		{
			return false;
		}
		
		System.out.println(query);
		return executer(query, obj, colum);
	}
	
	public static <Child extends MasterClass >boolean delete(Child obj)
	{

		String table_name=obj.gettable_name();	
		// Where Condition
		String where=null;
		if(obj.getwhere().size()>1)
		{
			where=conditionBuilder(obj,true);
		}
		// Query
		String query;
		if(where!=null)
		{
			query=String.format(DatabaseOperation.DELETE.getSqlTemplate(),table_name,where);
		}
		else
		{
			return false;
		}
		
		boolean state=false;
		System.out.println(query);
		
		try(Connection con = JDBCconnect.connect();
			    PreparedStatement pst = con.prepareStatement(query); )
			{
			
			if(pst.executeUpdate()>=0)
			{
				state=true;
			}
			
			}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return state;
	}	

// Where & Update String Builder
	
   public static <Child extends MasterClass> String conditionBuilder(Child obj,boolean updateOrWhere)
   {
	   
	   StringBuilder query=new StringBuilder();
	   
	   if(updateOrWhere)
	   {	
		   LinkedHashMap<String,Object> values=obj.getwhere();
	   
	   }
	   else
	   {
		   LinkedHashMap<String,Object> con=obj.getupdate();
		   int size=con.size()-1;
		   for(String key : con.keySet())
		   {
			   query.append(key+"=");
			   Object va=con.get(key);
			   if(va instanceof String)
			   {
				   query.append("'"+con.get(key)+"'");
			   }
			   else
			   {
				   query.append(con.get(key));
			   }
			   if(size>0)
			   {
				   query.append(",");
				   size--;
			   }
		        if(size>0 )
		        {
		        	query.append(",");
		        	size--;
		        }
		   }   
	   }
	   
	   return new String(query);
   }
	
	
	
	
	
// Query Runner
	public static <Child extends MasterClass >boolean executer (String query,Child obj,LinkedList<String> col)
	{
		boolean state=false;
		if(col.size()==0)
		{
			return state;
		}
		
		
		try(Connection con = JDBCconnect.connect();
		    PreparedStatement pst = con.prepareStatement(query); )
		{
			
			Method mth[]=obj.getClass().getDeclaredMethods();
			
			for(int index=0;index<col.size();index++)
			{
				String getMethodName="get"+col.get(index);
				Method method = obj.getClass().getDeclaredMethod(getMethodName);				
				if(method.getName().endsWith(getMethodName)) 
				{
					pst.setObject(index+1, method.invoke(obj)); 
				}

			}
			
			if(pst.executeUpdate()>=0)
			{
				state=true;
			}
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return state;
	}
	
	
// Column Builder	
	
public static String columnBuilder(LinkedList<String> col)
	{
		StringBuilder c = new StringBuilder((String)col.getFirst());
		
		System.out.println("In Column Builder");
		
		try
		{
			if(col.size()>1)
			{
				for (int i = 1; i<col.size();i++)
				{
					c.append(","+(String)col.get(i));
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		return new String(c);
	}
	
// Column Question Mark Builder
	
	public static String questionMarksBuilder(int size)
	{
		StringBuilder v= new StringBuilder("?");
		if(size>1)
		{
			for(int i=1;i<size;i++)
			{
				v.append(",?");
			}
		}
		return new String(v);
	}
	

}

