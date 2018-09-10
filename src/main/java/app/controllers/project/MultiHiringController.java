package app.controllers.project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.javalite.activeweb.annotations.POST;
import org.javalite.common.Convert;

import app.models.SdmSkill;
import app.models.Sdm;
import app.models.SdmHiring;
import app.models.Skill;
import app.models.SkillType;
import core.io.enums.ActionTypes;
import core.io.enums.HttpResponses;
import core.io.helper.JsonHelper;
import core.io.helper.MapHelper;
import core.io.helper.Validation;
import core.io.model.CorePage;
import core.io.model.DTOModel;
import core.io.model.PagingParams;
import core.javalite.controllers.CRUDController;


/*
 * Created by Khairil Anwar
 * 21 Agustus 2018
 *
 * Updated by Hendra Kurniawan
 * 7 September 2018
 */

public class MultiHiringController extends CRUDController<SdmHiring>{
	
	public class InputHiringDTO extends DTOModel{
		public int client_id;
		public int sdm_id;
		public int hirestat_id;
		
	}

	@POST
	public final void multiCreate() {
		try {
			Base.openTransaction();

			response().setActionType(ActionTypes.CREATE);
		
			Map<String, Object> mapRequest = getRequestBody();
			List<Map<String, Object>> params = MapHelper.castToListMap((List<Map>) mapRequest.get("listhiring"));
			List<Map<String, Object>> listHiring = new ArrayList<>();
			
			listHiring = validateRedudantInput(params);
			for (Map<String, Object> hiring : listHiring) {
				System.out.println("SDM Hiring : " + JsonHelper.toJson(hiring));
				InputHiringDTO sdmhiringDto = new InputHiringDTO ();
				sdmhiringDto.fromMap(hiring);

				System.out.println("SDM Hiring DTO : " + JsonHelper.toJson(sdmhiringDto.toMap()));

				SdmHiring sdmModel = new SdmHiring();
				sdmModel.fromMap(sdmhiringDto.toModelMap());
				
				Integer sdmId = Convert.toInteger(hiring.get("sdm_id"));
				Integer clientId = Convert.toInteger(hiring.get("client_id"));
				Integer hirestatId = Convert.toInteger(hiring.get("hirestat_id"));
				boolean cekData = false;
				
				List<Map> listdata = new ArrayList<>();
				listdata = SdmHiring.getDataSdmbyClient(clientId);
				
				//cek validasi : 1 sdm  hanya bisa 1 kali hire di sebuah perusahaan sebelum 
				for(Map dataHire : listdata) {
					
					if(sdmId == dataHire.get("sdm_id") && hirestatId != 5) {
						messages().notify();
						cekData = true;
					}
				}
				
				if (cekData == false) {
					sdmModel.insert();
					System.out.println("Inserted Hiring : " + sdmhiringDto.sdm_id);
				}
			}
			
			response().setResponseBody(HttpResponses.ON_SUCCESS_CREATE, listHiring);

			Base.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			response().setResponseBody(e);
			Base.rollbackTransaction();
		}

		sendResponse();
	}

	public static List<Map<String, Object>> validateRedudantInput(List<Map<String, Object>> listHiring){
		List<Map<String, Object>> newListHiring = new ArrayList<>();
		
		int banyakData = listHiring.size();
		int [] sdmIdRedudant = new int[banyakData];
		int [] sdmId = new int[banyakData];
		int index=0;
	    
		for (Map<String, Object> list : listHiring) {
			sdmIdRedudant[index] = Convert.toInteger(list.get("sdm_id"));
		index++;	
		}
		
		index=0;
		Set<Integer> store = new HashSet<>(); 
		
		for(Integer sdm : sdmIdRedudant) {
			if(store.add(sdm) == true) {
				sdmId[index] = sdm;
				index++;
			}
		}
		
		int [] sdmIdFilter = new int[index];
		for(index=0; index < sdmIdFilter.length; index++) {
			sdmIdFilter[index] = sdmId[index];
			System.out.println("sdm filter :" + sdmIdFilter[index]);
		}
		index=0;
		for(Map<String, Object> list : listHiring) {
			int sdm = Convert.toInteger(list.get("sdm_id"));
			if(sdmId[index] == sdm) {
				newListHiring.add(list); 
						index++;
			}
			
		}
		
		return newListHiring;
	}
	
	/* (non-Javadoc)
	 * @see core.javalite.controllers.CRUDController#customInsertValidation(org.javalite.activejdbc.Model)
	 */
	@Override
	public SdmHiring customInsertValidation(SdmHiring item) throws Exception {
		// TODO Auto-generated method stub
		Integer sdmId = item.getInteger("sdm_id");
		Integer hireStatId = item.getInteger("hirestat_id");
		Integer clientId = item.getInteger("client_id");
		
		List<Map> listdata = new ArrayList<>();
		listdata = SdmHiring.getDataSdmbyClient(clientId);
		
		for(Map dataHire : listdata) {
			if(sdmId == dataHire.get("sdm_id")) {
				Validation.required(null, "Woi");
			}
		}
		
		
		return super.customInsertValidation(item);
	}
	
}
