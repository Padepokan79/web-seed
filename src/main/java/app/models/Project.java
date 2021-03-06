package app.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

import com.google.common.base.Strings;

import core.io.model.PagingParams;

@Table("project")
@IdName("project_id")
@BelongsToParents({
	@BelongsTo(foreignKeyName="sdm_id", parent = Sdm.class)
})

public class Project extends Model {
	
	@SuppressWarnings("rawtypes")
	public static List<Map> search(String project_id, PagingParams pagingParams) {	
		List<Object> params = new ArrayList<Object>();		
		StringBuilder query = new StringBuilder();		

		query.append("select * from project");
		
		if (!Strings.isNullOrEmpty(project_id)) {
			query.append(" where project_id like ?");
			params.add("%"+ project_id +"%");
		}
		
		if (!Strings.isNullOrEmpty(pagingParams.filterQuery())) {
			query.append(" AND ");
			query.append(pagingParams.filterQuery());
			params.addAll(Arrays.asList(pagingParams.filterParams()));
		}
		
		if (!Strings.isNullOrEmpty(pagingParams.orderBy())) {
			query.append(" order by ");
			query.append(pagingParams.orderBy());
			
			if (pagingParams.limit() != null && pagingParams.offset() != null) {
				query.append(" OFFSET ");
				query.append(pagingParams.offset());
				query.append(" ROWS");

				query.append(" FETCH NEXT ");
				query.append(pagingParams.limit());
				query.append(" ROWS ONLY");
			}
		}
				
		return Base.findAll(query.toString(), new Object[params.size()]);
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Map> groupByProjectName () {
		List<Object> params = new ArrayList<Object>();
		StringBuilder query = new StringBuilder();
		query.append("SELECT PROJECT_ID, PROJECT_NAME\r\n" + 
				"FROM project\r\n" + 
				"GROUP BY PROJECT_NAME\r\n" + 
				";\r\n" + 
				"");
		System.out.println("query : "+query.toString());
		List<Map> listdata = Base.findAll(query.toString(), params.toArray(new Object[params.size()])); 
		
		return listdata;
	}
}
